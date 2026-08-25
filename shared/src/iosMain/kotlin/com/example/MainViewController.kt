package com.example

import androidx.compose.ui.window.ComposeUIViewController
import com.example.features.main.SattvaViewModel
import com.example.ui.theme.SattvaTheme
import com.example.features.main.MainScreen

// Mocking dependencies for iOS initialization
import com.example.di.AppDependencies
import com.example.data.repository.SattvaRepository
import com.example.data.remote.firebase.*
import com.example.data.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import com.example.data.remote.firebase.model.*
import com.example.data.model.FamilyMember

class IosAuthRepository : AuthRepository {
    private val _authState = MutableStateFlow<AuthUser?>(AuthUser("ios_dummy", "Devotee", "", ""))
    override val currentUser: AuthUser? get() = _authState.value
    override val authState: Flow<AuthUser?> = _authState
    override val currentUserId: String = "ios_dummy"
    override suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signInAnonymously(): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signOut() {}
}

class IosCatalogRepository : CatalogRepository {
    override fun observePujas(): Flow<List<FirestorePuja>> = flowOf(emptyList())
    override suspend fun getPujas(): Result<List<FirestorePuja>> = Result.success(emptyList())
    override fun observeGaushalas(): Flow<List<FirestoreGaushala>> = flowOf(emptyList())
    override suspend fun getGaushalas(): Result<List<FirestoreGaushala>> = Result.success(emptyList())
    override suspend fun getAnimals(gaushalaId: String?): Result<List<FirestoreAnimal>> = Result.success(emptyList())
    override fun observeAnimals(gaushalaId: String?): Flow<List<FirestoreAnimal>> = flowOf(emptyList())
    override fun observeDailyWisdom(): Flow<List<FirestoreDailyContent>> = flowOf(emptyList())
    override suspend fun getPuja(id: String): FirestorePuja? = null
    override suspend fun getGaushala(id: String): FirestoreGaushala? = null
}

class IosUserRepository : UserRepository {
    override fun observeUserProfile(uid: String): Flow<FirestoreUser?> = flowOf(null)
    override suspend fun getUserProfile(uid: String): Result<FirestoreUser?> = Result.success(null)
    override suspend fun saveOrUpdateUserProfile(uid: String, name: String?, email: String?, photoUrl: String?) {}
    override suspend fun updateSpiritualIdentity(uid: String, gotra: String, nakshatra: String, rashi: String): Result<Unit> = Result.success(Unit)
    override suspend fun updateFcmToken(uid: String, token: String): Result<Unit> = Result.success(Unit)
    override fun observeFamilyMembers(uid: String): Flow<List<FirestoreFamilyMember>> = flowOf(emptyList())
    override suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String> = Result.success("")
    override suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit> = Result.success(Unit)
    override fun observeBookmarks(uid: String): Flow<Set<String>> = flowOf(emptySet())
    override suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit> = Result.success(Unit)
    override fun observePujaBookings(uid: String): Flow<List<FirestorePujaBooking>> = flowOf(emptyList())
    override suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String> = Result.success("")
    override fun observeSevaContributions(uid: String): Flow<List<FirestoreSevaContribution>> = flowOf(emptyList())
    override suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String> = Result.success("")
}

fun initAppDependencies() {
    AppDependencies.authRepository = IosAuthRepository()
    AppDependencies.catalogRepository = IosCatalogRepository()
    AppDependencies.userRepository = IosUserRepository()
    AppDependencies.storageRepository = object : StorageRepository {
        override suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): Result<String> = Result.success("")
    }
    AppDependencies.notificationRepository = object : NotificationRepository {
        override suspend fun registerDeviceToken(userId: String, token: String) {}
    }
    // Note: AppDependencies.database = com.example.data.local.getDatabaseBuilder().fallbackToDestructiveMigration().build()
    AppDependencies.repository = SattvaRepository()
}

fun MainViewController() = ComposeUIViewController {
    initAppDependencies()
    SattvaTheme {
        MainScreen(viewModel = SattvaViewModel())
    }
}
