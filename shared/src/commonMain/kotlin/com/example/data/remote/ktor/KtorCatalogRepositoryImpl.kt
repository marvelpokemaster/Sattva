package com.example.data.remote.ktor

import com.example.core.config.AppConfig
import com.example.data.remote.firebase.CatalogRepository
import com.example.data.remote.firebase.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
private data class GaushalaResponse(
    val gaushalas: List<FirestoreGaushala> = emptyList(),
    val count: Int = 0
)

@Serializable
private data class AnimalResponse(
    val animals: List<FirestoreAnimal> = emptyList(),
    val count: Int = 0
)

@Serializable
private data class PujaResponse(
    val pujas: List<FirestorePuja> = emptyList(),
    val count: Int = 0
)

class KtorCatalogRepositoryImpl : CatalogRepository {
    private val client = KtorClient.httpClient

    override suspend fun getGaushalas(): Result<List<FirestoreGaushala>> {
        return try {
            val response: GaushalaResponse = client.get("${AppConfig.backendBaseUrl}/api/v1/catalog/gaushalas").body()
            Result.success(response.gaushalas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeGaushalas(): Flow<List<FirestoreGaushala>> = flow {
        emit(getGaushalas().getOrDefault(emptyList()))
    }

    override suspend fun getAnimals(gaushalaId: String?): Result<List<FirestoreAnimal>> {
        return try {
            val url = if (gaushalaId != null) {
                "${AppConfig.backendBaseUrl}/api/v1/catalog/animals?gaushalaId=$gaushalaId"
            } else {
                "${AppConfig.backendBaseUrl}/api/v1/catalog/animals"
            }
            val response: AnimalResponse = client.get(url).body()
            Result.success(response.animals)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeAnimals(gaushalaId: String?): Flow<List<FirestoreAnimal>> = flow {
        emit(getAnimals(gaushalaId).getOrDefault(emptyList()))
    }

    override suspend fun getPujas(): Result<List<FirestorePuja>> {
        return try {
            val response: PujaResponse = client.get("${AppConfig.backendBaseUrl}/api/v1/catalog/pujas").body()
            Result.success(response.pujas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observePujas(): Flow<List<FirestorePuja>> = flow {
        emit(getPujas().getOrDefault(emptyList()))
    }

    override fun observeDailyWisdom(): Flow<List<FirestoreDailyContent>> = flow {
        emit(emptyList())
    }

    override suspend fun getPuja(id: String): FirestorePuja? {
        val result = getPujas().getOrNull()
        return result?.find { it.id == id }
    }
    
    override suspend fun getGaushala(id: String): FirestoreGaushala? {
        val result = getGaushalas().getOrNull()
        return result?.find { it.id == id }
    }

    override suspend fun seedInitialDataIfEmpty(): Result<Unit> {
        return Result.success(Unit) // Handled by actual backend or manual admin
    }
}
