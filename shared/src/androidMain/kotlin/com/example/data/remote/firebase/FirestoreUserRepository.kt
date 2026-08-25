package com.example.data.remote.firebase

import android.util.Log
import com.example.data.model.FamilyMember
import com.example.data.remote.firebase.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreUserRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserRepository {

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

            if (updates.isNotEmpty()) {
                docRef.set(updates, SetOptions.merge()).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user profile: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateSpiritualIdentity(uid: String, gotra: String, nakshatra: String, rashi: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).set(
                mapOf("gotra" to gotra, "nakshatra" to nakshatra, "rashi" to rashi),
                SetOptions.merge()
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFcmToken(uid: String, token: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).set(
                mapOf("fcmToken" to token),
                SetOptions.merge()
            ).await()
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
        val registration = firestore.collection("users").document(uid).collection("family_members")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing family members: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val members = snapshot.documents.mapNotNull { it.toObject(FirestoreFamilyMember::class.java) }
                    trySend(members)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String> {
        return try {
            val docRef = firestore.collection("users").document(uid).collection("family_members").document()
            val firestoreMember = FirestoreFamilyMember(
                id = docRef.id,
                name = member.name,
                relation = member.relation,
                gotra = member.gotra,
                nakshatra = member.nakshatra
            )
            docRef.set(firestoreMember).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).collection("family_members").document(memberId).delete().await()
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
        val registration = firestore.collection("users").document(uid).collection("bookmarks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing bookmarks: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val ids = snapshot.documents.map { it.id }.toSet()
                    trySend(ids)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit> {
        return try {
            val docRef = firestore.collection("users").document(uid).collection("bookmarks").document(targetId)
            if (isBookmarked) {
                docRef.set(FirestoreBookmark(targetId = targetId, targetType = targetType)).await()
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
        val registration = firestore.collection("users").document(uid).collection("puja_bookings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing puja bookings: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val bookings = snapshot.documents.mapNotNull { it.toObject(FirestorePujaBooking::class.java) }
                    trySend(bookings)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String> {
        return try {
            val docRef = firestore.collection("users").document(uid).collection("puja_bookings").document()
            val finalBooking = booking.copy(bookingId = docRef.id)
            docRef.set(finalBooking).await()
            Result.success(docRef.id)
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
        val registration = firestore.collection("users").document(uid).collection("seva_contributions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing seva contributions: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val contributions = snapshot.documents.mapNotNull { it.toObject(FirestoreSevaContribution::class.java) }
                    trySend(contributions)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String> {
        return try {
            val docRef = firestore.collection("users").document(uid).collection("seva_contributions").document()
            val finalContribution = contribution.copy(contributionId = docRef.id)
            docRef.set(finalContribution).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
