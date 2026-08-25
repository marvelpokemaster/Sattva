import os

files = {
    "shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseAuthRepository.kt": """package com.example.data.remote.firebase

import com.example.data.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FirebaseAuthRepositoryImpl : AuthRepository {
    private val _authState = MutableStateFlow<AuthUser?>(AuthUser("dummy", "Devotee", "", ""))
    override val currentUser: AuthUser? get() = _authState.value
    override val authState: Flow<AuthUser?> = _authState
    override val currentUserId: String = "dummy"

    override suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signInAnonymously(): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signOut() {}
}
""",

    "shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirestoreCatalogRepository.kt": """package com.example.data.remote.firebase

import com.example.data.remote.firebase.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FirestoreCatalogRepositoryImpl : CatalogRepository {
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
""",

    "shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirestoreUserRepository.kt": """package com.example.data.remote.firebase

import com.example.data.model.FamilyMember
import com.example.data.remote.firebase.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FirestoreUserRepositoryImpl : UserRepository {
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
""",

    "shared/src/androidMain/kotlin/com/example/data/remote/firebase/PushNotificationRepository.kt": """package com.example.data.remote.firebase

class PushNotificationRepositoryImpl : NotificationRepository {
    override suspend fun registerDeviceToken(userId: String, token: String) {}
}
""",

    "shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseStorageRepository.kt": """package com.example.data.remote.firebase

class FirebaseStorageRepositoryImpl : StorageRepository {
    override suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): Result<String> = Result.success("")
}
"""
}

for path, content in files.items():
    with open(path, "w") as f:
        f.write(content)

