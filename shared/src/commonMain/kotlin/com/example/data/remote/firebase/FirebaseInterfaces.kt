package com.example.data.remote.firebase

import com.example.data.model.*
import com.example.data.remote.firebase.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: AuthUser?
    val authState: Flow<AuthUser?>
    val currentUserId: String
    suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser>
    suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser>
    suspend fun signInAnonymously(displayName: String = "Devotee"): Result<AuthUser>
    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>
    suspend fun getAuthToken(): String?
    suspend fun signOut()
}

interface CatalogRepository {
    fun observePujas(): Flow<List<FirestorePuja>>
    suspend fun getPujas(): Result<List<FirestorePuja>>
    fun observeGaushalas(): Flow<List<FirestoreGaushala>>
    suspend fun getGaushalas(): Result<List<FirestoreGaushala>>
    suspend fun getAnimals(gaushalaId: String? = null): Result<List<FirestoreAnimal>>
    fun observeAnimals(gaushalaId: String? = null): Flow<List<FirestoreAnimal>>
    fun observeDailyWisdom(): Flow<List<FirestoreDailyContent>>
    suspend fun getPuja(id: String): FirestorePuja?
    suspend fun getGaushala(id: String): FirestoreGaushala?
    suspend fun seedInitialDataIfEmpty(): Result<Unit>
}

interface UserRepository {
    fun observeUserProfile(uid: String): Flow<FirestoreUser?>
    suspend fun getUserProfile(uid: String): Result<FirestoreUser?>
    suspend fun saveOrUpdateUserProfile(
        uid: String,
        displayName: String? = null,
        email: String? = null,
        phoneNumber: String? = null,
        avatarUrl: String? = null,
        city: String? = null,
        gotra: String? = null,
        nakshatra: String? = null,
        rashi: String? = null,
        fcmToken: String? = null
    ): Result<Unit>
    suspend fun updateSpiritualIdentity(uid: String, gotra: String, nakshatra: String, rashi: String): Result<Unit>
    suspend fun updateFcmToken(uid: String, token: String): Result<Unit>
    fun observeFamilyMembers(uid: String): Flow<List<FirestoreFamilyMember>>
    suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String>
    suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit>
    fun observeBookmarks(uid: String): Flow<Set<String>>
    suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit>
    fun observePujaBookings(uid: String): Flow<List<FirestorePujaBooking>>
    suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String>
    fun observeSevaContributions(uid: String): Flow<List<FirestoreSevaContribution>>
    suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String>
}

interface StorageRepository {
    suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): Result<String>
}

interface NotificationRepository {
    suspend fun registerDeviceToken(userId: String, token: String)
    suspend fun getFcmToken(): Result<String>
    suspend fun subscribeToTopic(topic: String): Result<Unit>
    suspend fun unsubscribeFromTopic(topic: String): Result<Unit>
}
