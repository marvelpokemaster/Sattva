package com.example.data.remote.firebase

import android.util.Log
import com.example.data.model.FamilyMember
import com.example.data.model.UserProfile
import com.example.data.remote.firebase.model.FirestoreBookmark
import com.example.data.remote.firebase.model.FirestoreFamilyMember
import com.example.data.remote.firebase.model.FirestorePujaBooking
import com.example.data.remote.firebase.model.FirestoreSevaContribution
import com.example.data.remote.firebase.model.FirestoreUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface FirestoreUserRepository {
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

    // Family Members
    fun observeFamilyMembers(uid: String): Flow<List<FirestoreFamilyMember>>
    suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String>
    suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit>

    // Bookmarks & Favorites
    fun observeBookmarks(uid: String): Flow<Set<String>>
    suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit>

    // Puja Bookings (Created as PENDING status, never client-side PAID)
    fun observePujaBookings(uid: String): Flow<List<FirestorePujaBooking>>
    suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String>

    // Seva Contributions (Created as PENDING status, never client-side PAID)
    fun observeSevaContributions(uid: String): Flow<List<FirestoreSevaContribution>>
    suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String>
}

class DefaultFirestoreUserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FirestoreUserRepository {

    private val TAG = "FirestoreUserRepo"

    override fun observeUserProfile(uid: String): Flow<FirestoreUser?> = callbackFlow {
        if (uid.isBlank()) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val docRef = firestore.collection("users").document(uid)
        val registration: ListenerRegistration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error observing user profile: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(FirestoreUser::class.java)
                trySend(user)
            } else {
                trySend(null)
            }
        }

        awaitClose { registration.remove() }
    }

    override suspend fun getUserProfile(uid: String): Result<FirestoreUser?> {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            Result.success(snapshot.toObject(FirestoreUser::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
    ): Result<Unit> {
        return try {
            val docRef = firestore.collection("users").document(uid)
            val updates = mutableMapOf<String, Any>()
            displayName?.let { updates["displayName"] = it }
            email?.let { updates["email"] = it }
            phoneNumber?.let { updates["phoneNumber"] = it }
            avatarUrl?.let { updates["avatarUrl"] = it }
            city?.let { updates["city"] = it }
            gotra?.let { updates["gotra"] = it }
            nakshatra?.let { updates["nakshatra"] = it }
            rashi?.let { updates["rashi"] = it }
            fcmToken?.let { updates["fcmToken"] = it }

            docRef.set(updates, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save or update user profile: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateSpiritualIdentity(uid: String, gotra: String, nakshatra: String, rashi: String): Result<Unit> {
        return try {
            val docRef = firestore.collection("users").document(uid)
            val map = mapOf(
                "gotra" to gotra,
                "nakshatra" to nakshatra,
                "rashi" to rashi
            )
            docRef.set(map, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFcmToken(uid: String, token: String): Result<Unit> {
        return try {
            if (uid.isNotBlank() && token.isNotBlank()) {
                firestore.collection("users").document(uid)
                    .set(mapOf("fcmToken" to token), SetOptions.merge()).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeFamilyMembers(uid: String): Flow<List<FirestoreFamilyMember>> = callbackFlow {
        if (uid.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = firestore.collection("users").document(uid).collection("family_members")
        val registration = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error observing family members: ${error.message}")
                return@addSnapshotListener
            }
            val members = snapshot?.documents?.mapNotNull { it.toObject(FirestoreFamilyMember::class.java) } ?: emptyList()
            trySend(members)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String> {
        return try {
            val memberId = UUID.randomUUID().toString().take(12)
            val firestoreMember = FirestoreFamilyMember(
                id = memberId,
                name = member.name,
                relation = member.relation,
                gotra = member.gotra,
                nakshatra = member.nakshatra
            )
            firestore.collection("users").document(uid)
                .collection("family_members").document(memberId)
                .set(firestoreMember).await()
            Result.success(memberId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid)
                .collection("family_members").document(memberId)
                .delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeBookmarks(uid: String): Flow<Set<String>> = callbackFlow {
        if (uid.isBlank()) {
            trySend(emptySet())
            close()
            return@callbackFlow
        }

        val ref = firestore.collection("users").document(uid).collection("bookmarks")
        val registration = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error observing bookmarks: ${error.message}")
                return@addSnapshotListener
            }
            val ids = snapshot?.documents?.map { it.id }?.toSet() ?: emptySet()
            trySend(ids)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit> {
        return try {
            val docRef = firestore.collection("users").document(uid)
                .collection("bookmarks").document(targetId)
            if (isBookmarked) {
                val bookmark = FirestoreBookmark(targetId = targetId, targetType = targetType)
                docRef.set(bookmark).await()
            } else {
                docRef.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observePujaBookings(uid: String): Flow<List<FirestorePujaBooking>> = callbackFlow {
        if (uid.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = firestore.collection("users").document(uid)
            .collection("puja_bookings")
            .orderBy("bookingDateStr", Query.Direction.DESCENDING)

        val registration = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // If orderBy index is not ready yet, fallback to unordered snapshot
                firestore.collection("users").document(uid).collection("puja_bookings")
                    .get()
                    .addOnSuccessListener { fallbackSnap ->
                        val bookings = fallbackSnap.documents.mapNotNull { it.toObject(FirestorePujaBooking::class.java) }
                        trySend(bookings)
                    }
                return@addSnapshotListener
            }
            val bookings = snapshot?.documents?.mapNotNull { it.toObject(FirestorePujaBooking::class.java) } ?: emptyList()
            trySend(bookings)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String> {
        return try {
            val bookingId = booking.bookingId.ifEmpty { "bk_" + UUID.randomUUID().toString().take(10) }
            val sanitized = booking.copy(
                bookingId = bookingId,
                status = "PENDING",
                paymentStatus = "PENDING" // Guard against client privilege escalation
            )
            firestore.collection("users").document(uid)
                .collection("puja_bookings").document(bookingId)
                .set(sanitized).await()
            Result.success(bookingId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeSevaContributions(uid: String): Flow<List<FirestoreSevaContribution>> = callbackFlow {
        if (uid.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = firestore.collection("users").document(uid)
            .collection("seva_contributions")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val registration = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                firestore.collection("users").document(uid).collection("seva_contributions")
                    .get()
                    .addOnSuccessListener { fallbackSnap ->
                        val items = fallbackSnap.documents.mapNotNull { it.toObject(FirestoreSevaContribution::class.java) }
                        trySend(items)
                    }
                return@addSnapshotListener
            }
            val items = snapshot?.documents?.mapNotNull { it.toObject(FirestoreSevaContribution::class.java) } ?: emptyList()
            trySend(items)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String> {
        return try {
            val contributionId = contribution.contributionId.ifEmpty { "seva_" + UUID.randomUUID().toString().take(10) }
            val sanitized = contribution.copy(
                contributionId = contributionId,
                paymentStatus = "PENDING" // Guard against client privilege escalation
            )
            firestore.collection("users").document(uid)
                .collection("seva_contributions").document(contributionId)
                .set(sanitized).await()
            Result.success(contributionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
