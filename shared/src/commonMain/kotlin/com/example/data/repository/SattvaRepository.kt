package com.example.data.repository


import com.example.data.local.AppDatabase
import com.example.data.model.AnimalResident
import com.example.data.model.DailyWisdom
import com.example.data.model.FamilyMember
import com.example.data.model.Gaushala
import com.example.data.model.PanchangInfo
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile

import com.example.data.remote.firebase.model.FirestorePujaBooking
import com.example.data.remote.firebase.model.FirestoreSevaContribution
import com.example.data.model.AuthUser
import com.example.di.AppDependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SattvaRepository(
    private val database: AppDatabase = AppDependencies.database,
    private val authRepo: com.example.data.remote.firebase.AuthRepository = AppDependencies.authRepository,
    private val catalogRepo: com.example.data.remote.firebase.CatalogRepository = AppDependencies.catalogRepository,
    private val userRepo: com.example.data.remote.firebase.UserRepository = AppDependencies.userRepository,
    private val storageRepo: com.example.data.remote.firebase.StorageRepository = AppDependencies.storageRepository,
    private val pushRepo: com.example.data.remote.firebase.NotificationRepository = AppDependencies.notificationRepository
) {
    private val TAG = "SattvaRepository"
    private val scope = CoroutineScope(Dispatchers.IO)

    private val pujaDao = database.pujaDao()
    private val gaushalaDao = database.gaushalaDao()
    private val sevaDao = database.sevaDao()
    private val userProfileDao = database.userProfileDao()

    // Dynamic family members & bookmark state synced with Firestore when logged in
    private val _syncedFamilyMembers = MutableStateFlow<List<FamilyMember>>(getDefaultFamilyMembers())
    val familyMembers: Flow<List<FamilyMember>> = _syncedFamilyMembers.asStateFlow()

    // Auth state flow
    val authState: Flow<AuthUser?> = authRepo.authState
    val currentFirebaseUser: AuthUser? get() = null
    val isUserSignedIn: Boolean get() = true

    // Catalog Flows (Cached in Room, populated from Firestore)
    val allPujas: Flow<List<Puja>> = pujaDao.getAllPujas()
    val bookedPujas: Flow<List<Puja>> = pujaDao.getBookedPujas()
    fun getPujaById(id: String): Flow<Puja?> = pujaDao.getPujaById(id)

    val allGaushalas: Flow<List<Gaushala>> = gaushalaDao.getAllGaushalas()
    val supportedGaushalas: Flow<List<Gaushala>> = gaushalaDao.getSupportedGaushalas()
    fun getGaushalaById(id: String): Flow<Gaushala?> = gaushalaDao.getGaushalaById(id)

    val allAnimals: Flow<List<AnimalResident>> = gaushalaDao.getAllAnimals()
    val urgentAnimals: Flow<List<AnimalResident>> = gaushalaDao.getUrgentAnimals()
    fun getAnimalsByGaushala(gaushalaId: String): Flow<List<AnimalResident>> = gaushalaDao.getAnimalsByGaushala(gaushalaId)
    fun getAnimalById(id: String): Flow<AnimalResident?> = gaushalaDao.getAnimalById(id)

    val allContributions: Flow<List<SevaContribution>> = sevaDao.getAllContributions()
    val sevaCount: Flow<Int> = sevaDao.getSevaCount()
    val totalContributed: Flow<Int> = sevaDao.getTotalAmountContributed()

    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    init {
        // Observe auth state changes to synchronize user-scoped Firestore data
        scope.launch {
            authRepo.authState.collect { authUser ->
                if (authUser != null) {
                    val uid = authUser.uid
                    println("DEBUG $TAG: " + "User authenticated ($uid), initializing Firestore listener synchronization...")

                    // 1. Sync User Profile from Firestore to Room cache
                    userRepo.observeUserProfile(uid).collect { firestoreUser ->
                        if (firestoreUser != null) {
                            userProfileDao.insertProfile(com.example.data.model.UserProfile(firestoreUser.uid, firestoreUser.displayName ?: "", firestoreUser.email ?: "", firestoreUser.avatarUrl ?: ""))
                        } else {
                            // First time sign in: create initial profile in Firestore
                            val initialProfile = UserProfile(
                                id = uid,
                                name = authUser.displayName ?: "Devotee",
                                location = "Mumbai, India",
                                gotra = "Kashyapa",
                                nakshatra = "Rohini",
                                rashi = "Vrishabha (Taurus)",
                                avatarUrl = authUser.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200"
                            )
                            userProfileDao.insertProfile(initialProfile)
                            userRepo.saveOrUpdateUserProfile(
                                uid = uid,
                                name = initialProfile.name,
                                email = authUser.email,
                                photoUrl = initialProfile.avatarUrl
                            )
                        }
                    }
                }
            }
        }

        // Observe family members for authenticated user
        scope.launch {
            authRepo.authState.collect { authUser ->
                if (authUser != null) {
                    userRepo.observeFamilyMembers(authUser.uid).collect { members ->
                        if (members.isNotEmpty()) {
                            _syncedFamilyMembers.value = members.map { it.toFamilyMember() }
                        } else {
                            _syncedFamilyMembers.value = getDefaultFamilyMembers()
                        }
                    }
                } else {
                    _syncedFamilyMembers.value = getDefaultFamilyMembers()
                }
            }
        }

        // Observe user bookmarks to update local Room cache
        scope.launch {
            authRepo.authState.collect { authUser ->
                if (authUser != null) {
                    userRepo.observeBookmarks(authUser.uid).collect { bookmarkedIds ->
                        val currentPujas = pujaDao.getAllPujas().firstOrNull() ?: emptyList()
                        currentPujas.forEach { puja ->
                            val shouldBeBookmarked = bookmarkedIds.contains(puja.id)
                            if (puja.isBookmarked != shouldBeBookmarked) {
                                pujaDao.toggleBookmark(puja.id, shouldBeBookmarked)
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun syncCatalogFromFirestore() {
        try {
            // Seed Firestore if empty
            // catalogRepo.seedInitialDataIfEmpty()

            // Fetch Pujas from Firestore & update Room cache
            catalogRepo.getPujas().onSuccess { firestorePujas ->
                if (firestorePujas.isNotEmpty()) {
                    val currentRoomPujas = pujaDao.getAllPujas().firstOrNull() ?: emptyList()
                    val mappedPujas = firestorePujas.map { fp ->
                        val existing = currentRoomPujas.find { it.id == fp.id }
                        fp.toPuja(
                            isBookmarked = existing?.isBookmarked ?: false,
                            isBooked = existing?.isBooked ?: false,
                            bookedDate = existing?.bookedDate ?: ""
                        )
                    }
                    pujaDao.insertPujas(mappedPujas)
                }
            }

            // Fetch Gaushalas & update Room cache
            catalogRepo.getGaushalas().onSuccess { firestoreGaushalas ->
                if (firestoreGaushalas.isNotEmpty()) {
                    val currentRoomGaushalas = gaushalaDao.getAllGaushalas().firstOrNull() ?: emptyList()
                    val mappedGaushalas = firestoreGaushalas.map { fg ->
                        val existing = currentRoomGaushalas.find { it.id == fg.id }
                        fg.toGaushala(isSupported = existing?.isSupported ?: false)
                    }
                    gaushalaDao.insertGaushalas(mappedGaushalas)
                }
            }

            // Fetch Animals & update Room cache
            catalogRepo.getAnimals().onSuccess { firestoreAnimals ->
                if (firestoreAnimals.isNotEmpty()) {
                    val currentRoomAnimals = gaushalaDao.getAllAnimals().firstOrNull() ?: emptyList()
                    val mappedAnimals = firestoreAnimals.map { fa ->
                        val existing = currentRoomAnimals.find { it.id == fa.id }
                        fa.toAnimalResident(isFavorite = existing?.isFavorite ?: false)
                    }
                    gaushalaDao.insertAnimals(mappedAnimals)
                }
            }
        } catch (e: Exception) {
            println("WARN $TAG: " + "Catalog sync fallback to local cache: ${e.message}")
        }
    }

    suspend fun bookPuja(id: String, gotra: String, name: String, date: String, aiSankalpa: String = "") {
        // 1. Update local Room database
        pujaDao.bookPuja(id, gotra, name, date)
        userProfileDao.incrementPujaCount()

        // 2. If user is signed in to Firebase, create a PENDING booking in Firestore
        val user = authRepo.currentUser
        if (user != null) {
            val booking = FirestorePujaBooking(
                pujaId = id,
                devoteeName = name,
                gotra = gotra,
                bookingDateStr = kotlinx.datetime.Clock.System.now().toString().substringBefore("T"),
                scheduledDateStr = date,
                aiGeneratedSankalpa = aiSankalpa,
                status = "PENDING",
                paymentStatus = "PENDING"
            )
            userRepo.createPendingPujaBooking(user.uid, booking)
        }
    }

    suspend fun togglePujaBookmark(id: String, isBookmarked: Boolean) {
        pujaDao.toggleBookmark(id, isBookmarked)
        val user = authRepo.currentUser
        if (user != null) {
            userRepo.setBookmark(user.uid, id, "PUJA", isBookmarked)
        }
    }

    suspend fun contributeToAnimal(animalId: String, amount: Int, animalName: String, category: String) {
        gaushalaDao.contributeToAnimal(animalId, amount)
        val contribution = SevaContribution(
            title = "Seva for $animalName",
            targetType = "ANIMAL",
            targetName = animalName,
            amountRupees = amount,
            sevaCategory = category,
            dateStr = "Today"
        )
        sevaDao.insertContribution(contribution)
        userProfileDao.addContribution(amount)

        val user = authRepo.currentUser
        if (user != null) {
            val firestoreContribution = FirestoreSevaContribution(
                title = "Seva for $animalName",
                targetType = "ANIMAL",
                targetId = animalId,
                targetName = animalName,
                amountRupees = amount,
                sevaCategory = category,
                dateStr = kotlinx.datetime.Clock.System.now().toString().substringBefore("T"),
                paymentStatus = "PENDING"
            )
            userRepo.createPendingSevaContribution(user.uid, firestoreContribution)
        }
    }

    suspend fun contributeToGaushala(gaushalaId: String, amount: Int, gaushalaName: String, category: String) {
        gaushalaDao.markSupported(gaushalaId)
        val contribution = SevaContribution(
            title = "Sanctuary Support: $gaushalaName",
            targetType = "GAUSHALA",
            targetName = gaushalaName,
            amountRupees = amount,
            sevaCategory = category,
            dateStr = "Today"
        )
        sevaDao.insertContribution(contribution)
        userProfileDao.addContribution(amount)

        val user = authRepo.currentUser
        if (user != null) {
            val firestoreContribution = FirestoreSevaContribution(
                title = "Sanctuary Support: $gaushalaName",
                targetType = "GAUSHALA",
                targetId = gaushalaId,
                targetName = gaushalaName,
                amountRupees = amount,
                sevaCategory = category,
                dateStr = kotlinx.datetime.Clock.System.now().toString().substringBefore("T"),
                paymentStatus = "PENDING"
            )
            userRepo.createPendingSevaContribution(user.uid, firestoreContribution)
        }
    }

    suspend fun toggleAnimalFavorite(id: String, isFav: Boolean) {
        gaushalaDao.toggleFavorite(id, isFav)
        val user = authRepo.currentUser
        if (user != null) {
            userRepo.setBookmark(user.uid, id, "ANIMAL", isFav)
        }
    }

    suspend fun updateSpiritualIdentity(gotra: String, nakshatra: String, rashi: String) {
        userProfileDao.updateSpiritualIdentity(gotra, nakshatra, rashi)
        val user = authRepo.currentUser
        if (user != null) {
            userRepo.updateSpiritualIdentity(user.uid, gotra, nakshatra, rashi)
        }
    }

    suspend fun addFamilyMember(member: FamilyMember) {
        val user = authRepo.currentUser
        if (user != null) {
            userRepo.addFamilyMember(user.uid, member)
        }
        _syncedFamilyMembers.value = _syncedFamilyMembers.value + member
    }

    suspend fun uploadAvatar(bytes: ByteArray, contentType: String = "image/jpeg"): Result<String> {
        return Result.success("simulated_avatar_url")
    }

    // Static / Daily Vedic Content
    fun getTodayPanchang(): PanchangInfo {
        return PanchangInfo(
            tithi = "Shukla Paksha Dashami",
            nakshatra = "Rohini Nakshatra",
            paksha = "Shukla",
            auspiciousTiming = "Abhijit Muhurta: 11:58 AM - 12:48 PM",
            rahuKaal = "04:30 PM - 06:00 PM"
        )
    }

    fun getTodayWisdom(): DailyWisdom {
        return DailyWisdom(
            quote = "Karma-yoga is a supreme secret indeed.",
            source = "Bhagavad Gita (Chapter 2, Verse 50)",
            commentary = "One who is engaged in devotional service rids himself of both good and bad actions even in this life. Therefore strive for Yoga, which is the art of all work.",
            sanskritShloka = "बुद्धियुक्तो जहातीह उभे सुकृतदुष्कृते । तस्माद्योगाय युज्यस्व योगः कर्मसु कौशलम् ॥"
        )
    }

    fun getDefaultFamilyMembers(): List<FamilyMember> {
        return listOf(
            FamilyMember(name = "Priya Desai", relation = "Spouse", gotra = "Kashyapa Gotra"),
            FamilyMember(name = "Rohan Desai", relation = "Son", gotra = "Kashyapa Gotra"),
            FamilyMember(name = "Meera Desai", relation = "Daughter", gotra = "Kashyapa Gotra")
        )
    }

    suspend fun seedInitialDataIfEmpty() {
        val existingProfile = userProfileDao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            userProfileDao.insertProfile(UserProfile())
        }
        // Run sync from Firestore in background
        syncCatalogFromFirestore()
    }
}
