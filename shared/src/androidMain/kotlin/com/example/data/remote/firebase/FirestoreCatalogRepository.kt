package com.example.data.remote.firebase

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
