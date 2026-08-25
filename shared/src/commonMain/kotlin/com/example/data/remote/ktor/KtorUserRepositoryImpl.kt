package com.example.data.remote.ktor

import com.example.core.config.AppConfig
import com.example.data.model.FamilyMember
import com.example.data.remote.firebase.AuthRepository
import com.example.data.remote.firebase.UserRepository
import com.example.data.remote.firebase.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
private data class ProfileResponse(val profile: FirestoreUser?)

@Serializable
private data class DonationsResponse(val donations: List<FirestoreSevaContribution> = emptyList())

@Serializable
private data class DonationCreateResponse(val id: String, val status: String)

class KtorUserRepositoryImpl(private val authRepository: AuthRepository) : UserRepository {
    private val client = KtorClient.httpClient

    private suspend fun HttpRequestBuilder.addAuthHeader() {
        val token = authRepository.getAuthToken()
        if (token != null) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    override fun observeUserProfile(uid: String): Flow<FirestoreUser?> = flow {
        emit(getUserProfile(uid).getOrNull())
    }

    override suspend fun getUserProfile(uid: String): Result<FirestoreUser?> {
        return try {
            val response: ProfileResponse = client.get("${AppConfig.backendBaseUrl}/api/v1/profile") {
                addAuthHeader()
            }.body()
            Result.success(response.profile)
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
            val updateMap = mutableMapOf<String, String>()
            displayName?.let { updateMap["displayName"] = it }
            email?.let { updateMap["email"] = it }
            phoneNumber?.let { updateMap["phoneNumber"] = it }
            avatarUrl?.let { updateMap["avatarUrl"] = it }
            city?.let { updateMap["city"] = it }
            gotra?.let { updateMap["gotra"] = it }
            nakshatra?.let { updateMap["nakshatra"] = it }
            rashi?.let { updateMap["rashi"] = it }
            fcmToken?.let { updateMap["fcmToken"] = it }

            client.put("${AppConfig.backendBaseUrl}/api/v1/profile") {
                addAuthHeader()
                contentType(ContentType.Application.Json)
                setBody(updateMap)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSpiritualIdentity(uid: String, gotra: String, nakshatra: String, rashi: String): Result<Unit> {
        return saveOrUpdateUserProfile(uid = uid, gotra = gotra, nakshatra = nakshatra, rashi = rashi)
    }

    override suspend fun updateFcmToken(uid: String, token: String): Result<Unit> {
        return saveOrUpdateUserProfile(uid = uid, fcmToken = token)
    }

    // Unimplemented or local-only for now, as V1 focuses on Donations & Profile
    override fun observeFamilyMembers(uid: String): Flow<List<FirestoreFamilyMember>> = flow { emit(emptyList()) }
    override suspend fun addFamilyMember(uid: String, member: FamilyMember): Result<String> = Result.success("")
    override suspend fun removeFamilyMember(uid: String, memberId: String): Result<Unit> = Result.success(Unit)
    override fun observeBookmarks(uid: String): Flow<Set<String>> = flow { emit(emptySet()) }
    override suspend fun setBookmark(uid: String, targetId: String, targetType: String, isBookmarked: Boolean): Result<Unit> = Result.success(Unit)
    override fun observePujaBookings(uid: String): Flow<List<FirestorePujaBooking>> = flow { emit(emptyList()) }
    override suspend fun createPendingPujaBooking(uid: String, booking: FirestorePujaBooking): Result<String> = Result.success("")

    override fun observeSevaContributions(uid: String): Flow<List<FirestoreSevaContribution>> = flow {
        try {
            val response: DonationsResponse = client.get("${AppConfig.backendBaseUrl}/api/v1/donations") {
                addAuthHeader()
            }.body()
            emit(response.donations)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun createPendingSevaContribution(uid: String, contribution: FirestoreSevaContribution): Result<String> {
        return try {
            val bodyMap = mapOf(
                "targetType" to contribution.targetType,
                "targetId" to contribution.targetId,
                "targetName" to contribution.targetName,
                "amountRupees" to contribution.amountRupees,
                "sevaCategory" to contribution.sevaCategory
            )
            val response: DonationCreateResponse = client.post("${AppConfig.backendBaseUrl}/api/v1/donations") {
                addAuthHeader()
                contentType(ContentType.Application.Json)
                setBody(bodyMap)
            }.body()
            Result.success(response.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
