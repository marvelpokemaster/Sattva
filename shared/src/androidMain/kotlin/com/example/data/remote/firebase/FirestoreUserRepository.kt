package com.example.data.remote.firebase

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
