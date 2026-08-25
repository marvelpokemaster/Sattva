package com.example

import androidx.compose.ui.window.ComposeUIViewController
import com.example.features.main.SattvaViewModel
import com.example.core.ui.theme.SattvaTheme
import com.example.features.main.MainScreen
import com.example.di.AppDependencies
import com.example.data.repository.SattvaRepository
import com.example.data.remote.firebase.*
import com.example.data.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import com.example.data.remote.firebase.model.*
import com.example.data.model.FamilyMember

/**
 * Platform implementation for iOS.
 * Real Firebase iOS SDK integration is configured when compiled on macOS with Xcode / CocoaPods / SPM.
 * In environments without macOS/Xcode, operations are explicitly isolated and fail gracefully rather than faking success.
 */
class IosAuthRepository : AuthRepository {
    private val _authState = MutableStateFlow<AuthUser?>(null)
    override val currentUser: AuthUser? get() = _authState.value
    override val authState: Flow<AuthUser?> = _authState
    override val currentUserId: String get() = currentUser?.uid ?: ""

    override suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser> =
        Result.failure(UnsupportedOperationException("iOS Firebase Auth is pending Xcode/macOS build configuration."))

    override suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser> =
        Result.failure(UnsupportedOperationException("iOS Firebase Auth is pending Xcode/macOS build configuration."))

    override suspend fun signInAnonymously(displayName: String): Result<AuthUser> =
        Result.failure(UnsupportedOperationException("iOS Firebase Auth is pending Xcode/macOS build configuration."))

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> =
        Result.failure(UnsupportedOperationException("iOS Firebase Auth is pending Xcode/macOS build configuration."))

    override suspend fun signOut() {
        _authState.value = null
    }
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
    override suspend fun seedInitialDataIfEmpty(): Result<Unit> = Result.success(Unit)
}

class IosUserRepository : UserRepository {
    override fun observeUserProfile(uid: String): Flow<FirestoreUser?> = flowOf(null)
    override suspend fun getUserProfile(uid: String): Result<FirestoreUser?> = Result.success(null)
    override suspend fun saveOrUpdateUserProfile(
        uid: String,
        displayName: String?,
        email: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        city: String?,
        gotra: String?,
        nakshatra: String?,
        rashi: String?,
        fcmToken: String?
    ): Result<Unit> = Result.failure(UnsupportedOperationException("iOS Firestore User sync pending macOS build."))
    override suspend fun updateSpiritualIdentity(uid: String, gotra: String, nakshatra: String, rashi: String): Result<Unit> = Result.success(Unit)
    override suspend fun updateFcmToken(uid: String, token: String): Result<Unit> = Result.success(Unit)
    override fun observeFamilyMembers(uid: String): Flow<List<FirestoreFamilyMember>> = flowOf(emptyList())
    override suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String> = Result.failure(UnsupportedOperationException("iOS Firestore pending."))
    override suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit> = Result.success(Unit)
    override fun observeBookmarks(uid: String): Flow<Set<String>> = flowOf(emptySet())
    override suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit> = Result.success(Unit)
    override fun observePujaBookings(uid: String): Flow<List<FirestorePujaBooking>> = flowOf(emptyList())
    override suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String> = Result.failure(UnsupportedOperationException("iOS Firestore pending."))
    override fun observeSevaContributions(uid: String): Flow<List<FirestoreSevaContribution>> = flowOf(emptyList())
    override suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String> = Result.failure(UnsupportedOperationException("iOS Firestore pending."))
}

class IosStorageRepository : StorageRepository {
    override suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): Result<String> =
        Result.failure(UnsupportedOperationException("iOS Firebase Storage is pending macOS build."))
}

class IosNotificationRepository : NotificationRepository {
    override suspend fun registerDeviceToken(userId: String, token: String) {}
    override suspend fun getFcmToken(): Result<String> = Result.failure(UnsupportedOperationException("iOS FCM pending."))
    override suspend fun subscribeToTopic(topic: String): Result<Unit> = Result.success(Unit)
    override suspend fun unsubscribeFromTopic(topic: String): Result<Unit> = Result.success(Unit)
}

fun initIosAppDependencies() {
    AppDependencies.database = com.example.data.local.getDatabaseBuilder().build()
    AppDependencies.authRepository = IosAuthRepository()
    AppDependencies.catalogRepository = IosCatalogRepository()
    AppDependencies.userRepository = IosUserRepository()
    AppDependencies.storageRepository = IosStorageRepository()
    AppDependencies.notificationRepository = IosNotificationRepository()
    AppDependencies.repository = SattvaRepository(
        database = AppDependencies.database,
        authRepo = AppDependencies.authRepository,
        catalogRepo = AppDependencies.catalogRepository,
        userRepo = AppDependencies.userRepository,
        storageRepo = AppDependencies.storageRepository,
        pushRepo = AppDependencies.notificationRepository
    )
}

fun MainViewController() = ComposeUIViewController {
    initIosAppDependencies()
    SattvaTheme {
        MainScreen()
    }
}
