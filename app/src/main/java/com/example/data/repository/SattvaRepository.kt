package com.example.data.repository

import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.model.AnimalResident
import com.example.data.model.DailyWisdom
import com.example.data.model.FamilyMember
import com.example.data.model.Gaushala
import com.example.data.model.PanchangInfo
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile
import com.example.data.remote.firebase.FirebaseAuthRepository
import com.example.data.remote.firebase.FirebaseInitializer
import com.example.data.remote.firebase.FirebaseStorageRepository
import com.example.data.remote.firebase.FirestoreCatalogRepository
import com.example.data.remote.firebase.FirestoreUserRepository
import com.example.data.remote.firebase.PushNotificationRepository
import com.example.data.remote.firebase.model.FirestorePujaBooking
import com.example.data.remote.firebase.model.FirestoreSevaContribution
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SattvaRepository(
    private val database: AppDatabase,
    private val authRepo: FirebaseAuthRepository = FirebaseInitializer.authRepository,
    private val catalogRepo: FirestoreCatalogRepository = FirebaseInitializer.catalogRepository,
    private val userRepo: FirestoreUserRepository = FirebaseInitializer.userRepository,
    private val storageRepo: FirebaseStorageRepository = FirebaseInitializer.storageRepository,
    private val pushRepo: PushNotificationRepository = FirebaseInitializer.pushNotificationRepository
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
    val authState: Flow<FirebaseUser?> = authRepo.authState
    val currentFirebaseUser: FirebaseUser? get() = authRepo.currentUser
    val isUserSignedIn: Boolean get() = authRepo.isUserSignedIn

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
            authRepo.authState.collect { firebaseUser ->
                if (firebaseUser != null) {
                    val uid = firebaseUser.uid
                    Log.d(TAG, "User authenticated ($uid), initializing Firestore listener synchronization...")

                    // 1. Sync User Profile from Firestore to Room cache
                    userRepo.observeUserProfile(uid).collect { firestoreUser ->
                        if (firestoreUser != null) {
                            userProfileDao.insertProfile(firestoreUser.toUserProfile())
                        } else {
                            // First time sign in: create initial profile in Firestore
                            val initialProfile = UserProfile(
                                id = uid,
                                name = firebaseUser.displayName ?: "Devotee",
                                location = "Mumbai, India",
                                gotra = "Kashyapa",
                                nakshatra = "Rohini",
                                rashi = "Vrishabha (Taurus)",
                                avatarUrl = firebaseUser.photoUrl?.toString() ?: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200"
                            )
                            userProfileDao.insertProfile(initialProfile)
                            userRepo.saveOrUpdateUserProfile(
                                uid = uid,
                                displayName = initialProfile.name,
                                email = firebaseUser.email,
                                phoneNumber = firebaseUser.phoneNumber,
                                avatarUrl = initialProfile.avatarUrl,
                                city = initialProfile.location,
                                gotra = initialProfile.gotra,
                                nakshatra = initialProfile.nakshatra,
                                rashi = initialProfile.rashi
                            )
                        }
                    }
                }
            }
        }

        // Observe family members for authenticated user
        scope.launch {
            authRepo.authState.collect { firebaseUser ->
                if (firebaseUser != null) {
                    userRepo.observeFamilyMembers(firebaseUser.uid).collect { members ->
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
            authRepo.authState.collect { firebaseUser ->
                if (firebaseUser != null) {
                    userRepo.observeBookmarks(firebaseUser.uid).collect { bookmarkedIds ->
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
            catalogRepo.seedInitialDataIfEmpty()

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
            Log.w(TAG, "Catalog sync fallback to local cache: ${e.message}")
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
                bookingDateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
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
                dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
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
                dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
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
        val user = authRepo.currentUser ?: return Result.failure(IllegalStateException("User not authenticated"))
        val path = "users/${user.uid}/avatars/profile_${System.currentTimeMillis()}.jpg"
        return storageRepo.uploadBytes(path, bytes, contentType).onSuccess { downloadUrl ->
            userProfileDao.updateProfile(
                UserProfile(
                    id = user.uid,
                    avatarUrl = downloadUrl
                )
            )
            userRepo.saveOrUpdateUserProfile(uid = user.uid, avatarUrl = downloadUrl)
        }
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
        val existingPujas = pujaDao.getAllPujas().firstOrNull()
        if (existingPujas.isNullOrEmpty()) {
            val initialPujas = listOf(
                Puja(
                    id = "maha_rudrabhishek",
                    title = "Maha Rudrabhishek",
                    specialTag = "Maha Shivratri Special",
                    templeName = "Kashi Vishwanath Temple, Varanasi",
                    location = "Kashi Vishwanath Temple, Varanasi",
                    dateTimeStr = "Mar 8, 5:30 AM IST",
                    durationStr = "2.5 Hours",
                    devoteesCount = "12.5k Devotees attending",
                    priceRupees = 2501,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOpqdNo42gJqDM8nBf0S-Hb22Q9tRGrWdjusk87abF3ywTcszYbUaaDwRjZ_O_q-L2aYmVY-Bl7IMXpzwqEvGnZrvr_f4OS3kecYgV_4HmNM5kAN2eXQHxpTzuQkJap3ol57kWh_-GN5ta8UgbQmOSYA96-mD2TrmVWP_V5LifI_9TKkIUiULJzAOIdNlzp0WzaSFwk-yWZdM0YCjvUY3f_9hnSGknRRk3soO_gznmp0zCUtITN40",
                    significance = "The Maha Rudrabhishek is a highly potent Vedic ritual dedicated to Lord Shiva. It involves bathing the Shiva Lingam with sacred offerings like milk, honey, ghee, and Gangajal while chanting the powerful Rudra Suktam. This sacred practice is believed to remove deeply rooted negative karmas, bring immense peace, and foster spiritual growth and material prosperity.",
                    priestName = "Pt. Rameshwar Shastri",
                    priestTitle = "Chief Priest, Kashi Vishwanath Mandir Trust",
                    priestExp = "30+ Years Experience in Vedic Rituals",
                    priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
                    category = "Upcoming",
                    isFeatured = true
                ),
                Puja(
                    id = "navagraha_shanti",
                    title = "Navagraha Shanti",
                    specialTag = "Popular",
                    templeName = "Trimbakeshwar Temple, Nashik",
                    location = "Trimbakeshwar Temple, Nashik",
                    dateTimeStr = "Every Tuesday, 7:00 AM IST",
                    durationStr = "3 Hours",
                    devoteesCount = "1.2k Devotees",
                    priceRupees = 1801,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCiOZCDJREiux2pK9I8vnrVJmJjpfKehH4pRC27fdQurVc07SxuFCEIS1H3auIdRI75sUxZZN9UvnHmIM2UrmRAteABz_DE0McuuXxH5FhZM1eT4O50vKSWTb09cOrX5mPt80vzwpcJLb_kvyJAExWO_YQcmgDDI2TNquH06hGPQzSlLkqfbOPesT2lZ9lojhwGhsHP2UdRtpxmm5QO0KButOKeFwrkU55MqcNJzdOOBbpOm8LocEE",
                    significance = "Navagraha Shanti homa balances planetary energies, removes astrological doshas, and invites harmony, good health, and professional prosperity into your household.",
                    priestName = "Acharya Vidyadhar Guruji",
                    priestTitle = "Vedacharya, Trimbakeshwar Jyotirlinga",
                    priestExp = "25+ Years Experience",
                    priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
                    category = "Popular"
                ),
                Puja(
                    id = "satyanarayan_katha",
                    title = "Satyanarayan Katha",
                    specialTag = "Special",
                    templeName = "Online Sankalp",
                    location = "Online Sankalp",
                    dateTimeStr = "Purnima (Full Moon), 6:00 PM",
                    durationStr = "1.5 Hours",
                    devoteesCount = "850 Devotees",
                    priceRupees = 1101,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBi_JP9tQ0dNw2yhnlQ6gkauFsmbIsm1MhGOXi7rgOJX60mw1G_grFCmSfeuKSbSuV4zONzJNiAY3XSrnud8PulQgQEzgEdqw2vUtDH-zWUQqx0rtbIk2qeZOIEVncp0u1XqK5FN-R2ezMS0sJYt4e5vtmB365bFwitV6sC61SJqO-atI47GbbuDIcnnUbcPFXRIba0COGXLicnttd-jpY95lUSneYIMQFoxwh_qH11v42ByUWka3U",
                    significance = "Sri Satyanarayan Puja is performed to seek the blessings of Lord Vishnu for domestic happiness, resolution of hurdles, and fulfillment of auspicious wishes.",
                    priestName = "Pt. Hariprasad Joshi",
                    priestTitle = "Vedic Scholar",
                    priestExp = "20+ Years Experience",
                    priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
                    category = "By Temple"
                ),
                Puja(
                    id = "maha_mrityunjaya_past",
                    title = "Maha Mrityunjaya Havan",
                    specialTag = "Completed",
                    templeName = "Kashi Vishwanath Temple, Varanasi",
                    location = "Kashi Vishwanath Temple, Varanasi",
                    dateTimeStr = "Oct 12, 6:00 AM IST",
                    durationStr = "3 Hours",
                    devoteesCount = "3.4k Devotees",
                    priceRupees = 3100,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBSmeJ1uJipFCYDSP4OEsK4XmSIpDRwHlNUckoF-Pk7nZva0St3zCsQ80glWljZA77QGRxW1oEqxAS9D7BNCSJUiytuPO3KuX3TY-8WzduDj0tTxIJkJvOdxuBEDOAcVTJGI3mPp8KwcWZ5uTi0s811eH6plBUs1fD6FtE2qN65IUVtYwwpN0nqmNck-bCBW-63aoYeS1gssRScax3MYLyIzw83Unznc8Wns320xUrJurWNqzSKgWw",
                    significance = "Consecrated Maha Mrityunjaya Japa and Havan invoking Mahadev for healing, longevity, and liberation from fear and distress.",
                    priestName = "Pt. Rameshwar Shastri",
                    priestTitle = "Chief Priest",
                    priestExp = "30+ Years",
                    priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
                    isBooked = true,
                    bookedDate = "Oct 12",
                    bookedGotra = "Kashyapa",
                    bookedDevoteeName = "Arjun Desai"
                )
            )
            pujaDao.insertPujas(initialPujas)
        }

        val existingGaushalas = gaushalaDao.getAllGaushalas().firstOrNull()
        if (existingGaushalas.isNullOrEmpty()) {
            val initialGaushalas = listOf(
                Gaushala(
                    id = "shri_krishna_gaushala",
                    name = "Shri Krishna Gaushala",
                    location = "Vrindavan",
                    state = "Uttar Pradesh",
                    trustScorePercent = 98,
                    animalsRescuedCount = 450,
                    transparencyTier = "Gold Tier",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-rVXkCb9UZsqmd1VN9FZaoCFYEKr0UYrWaDyMz4G5cu0RqLrysF4jCD-jPZip07NznS7G4GYt5pMurcrhIP1Vn2VqtcMgctuUSfzpu0OAA-Ipdvk4r9D_XwkMJ2K5aTB-vzNLlPL27fnJi7NDA6l0tt9JgyHfOldp3b_fgayEo2iuSVb83FYpJkpPx7mpsuLChQSxvGKg3ruya1Ct_DI0pSA5A4pV9313MQnmkwstgP8clw8ThPQ",
                    missionQuote = "Providing a lifelong, loving sanctuary for abandoned and injured cows. We believe every being deserves dignity, medical care, and a peaceful environment to thrive.",
                    fodderPercent = 65,
                    medicalPercent = 40,
                    shelterPercent = 85,
                    lat = 27.58,
                    lng = 77.70,
                    isSupported = true,
                    updatesCount = 2
                ),
                Gaushala(
                    id = "gopashtami_sanctuary",
                    name = "Gopashtami Sanctuary",
                    location = "Pushkar",
                    state = "Rajasthan",
                    trustScorePercent = 94,
                    animalsRescuedCount = 310,
                    transparencyTier = "Silver Tier",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC93W4mZ41E9rZ6R2jQp6aY88Ew6mH3wXoK6B6cZ0JgM7nL7vA2v9zN6yC8p4rX2mK9vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1",
                    missionQuote = "Dedicated to the rescue and organic care of native indigenous Gir and Tharparkar cow breeds across the sacred Pushkar desert region.",
                    fodderPercent = 50,
                    medicalPercent = 55,
                    shelterPercent = 70,
                    lat = 26.49,
                    lng = 74.55,
                    updatesCount = 3
                ),
                Gaushala(
                    id = "ganga_surabhi_dham",
                    name = "Ganga Surabhi Dham",
                    location = "Rishikesh",
                    state = "Uttarakhand",
                    trustScorePercent = 99,
                    animalsRescuedCount = 620,
                    transparencyTier = "Gold Tier",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-rVXkCb9UZsqmd1VN9FZaoCFYEKr0UYrWaDyMz4G5cu0RqLrysF4jCD-jPZip07NznS7G4GYt5pMurcrhIP1Vn2VqtcMgctuUSfzpu0OAA-Ipdvk4r9D_XwkMJ2K5aTB-vzNLlPL27fnJi7NDA6l0tt9JgyHfOldp3b_fgayEo2iuSVb83FYpJkpPx7mpsuLChQSxvGKg3ruya1Ct_DI0pSA5A4pV9313MQnmkwstgP8clw8ThPQ",
                    missionQuote = "Holistic rehabilitation combining natural Himalayan herbal treatments, fresh mountain grazing, and daily spiritual aarti for Gomata.",
                    fodderPercent = 75,
                    medicalPercent = 60,
                    shelterPercent = 90,
                    lat = 30.08,
                    lng = 78.26,
                    updatesCount = 5
                )
            )
            gaushalaDao.insertGaushalas(initialGaushalas)
        }

        val existingAnimals = gaushalaDao.getAllAnimals().firstOrNull()
        if (existingAnimals.isNullOrEmpty()) {
            val initialAnimals = listOf(
                AnimalResident(
                    id = "nandi_01",
                    gaushalaId = "shri_krishna_gaushala",
                    name = "Nandi",
                    ageStr = "3.5 Years",
                    healthStatus = "Recovering",
                    healthDescription = "Fractured left leg, currently on splint physiotherapy & daily herbal poultice.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA9kQ_J56K6b-67zQh6yM0GvJ7n8yC_8rT3pLmK6jN2v9bA4xZ7c8vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL",
                    story = "Rescued from a severe highway collision near Mathura. Thanks to timely veterinary attention and daily care by loving volunteers, Nandi is now standing on his own and walking gently with support.",
                    monthlyGoalRupees = 5000,
                    raisedRupees = 3250,
                    isUrgent = true,
                    isFavorite = true
                ),
                AnimalResident(
                    id = "surabhi_02",
                    gaushalaId = "shri_krishna_gaushala",
                    name = "Surabhi",
                    ageStr = "5 Years",
                    healthStatus = "Healthy",
                    healthDescription = "Active, gentle mother cow with a calf; thrives on green grass & jaggery.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3pZ1nL8vA4xZ7c8vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM",
                    story = "Surabhi was brought to the sanctuary after losing her way during a storm. She has since given birth to a healthy calf, Gopal, and is one of the friendliest residents in the sanctuary.",
                    monthlyGoalRupees = 4000,
                    raisedRupees = 4000,
                    isUrgent = false
                ),
                AnimalResident(
                    id = "kamadhenu_03",
                    gaushalaId = "ganga_surabhi_dham",
                    name = "Gauri",
                    ageStr = "8 Years",
                    healthStatus = "Critical Care",
                    healthDescription = "Elderly cow with severe cataract & joint arthritis requiring continuous medical care.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB_9yC8p4rX2mK9vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY",
                    story = "An elder Gomata rescued from abandonment. She receives special soft boiled mash, ayurvedic oil joint massages, and round-the-clock shelter attendant support.",
                    monthlyGoalRupees = 6500,
                    raisedRupees = 2100,
                    isUrgent = true
                )
            )
            gaushalaDao.insertAnimals(initialAnimals)
        }

        val existingProfile = userProfileDao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            userProfileDao.insertProfile(UserProfile())
        }

        // Run sync from Firestore in background
        syncCatalogFromFirestore()
    }
}
