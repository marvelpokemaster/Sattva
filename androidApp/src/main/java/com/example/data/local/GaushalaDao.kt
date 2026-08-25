package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AnimalResident
import com.example.data.model.Gaushala
import kotlinx.coroutines.flow.Flow

@Dao
interface GaushalaDao {
    @Query("SELECT * FROM gaushalas")
    fun getAllGaushalas(): Flow<List<Gaushala>>

    @Query("SELECT * FROM gaushalas WHERE id = :id LIMIT 1")
    fun getGaushalaById(id: String): Flow<Gaushala?>

    @Query("SELECT * FROM gaushalas WHERE isSupported = 1")
    fun getSupportedGaushalas(): Flow<List<Gaushala>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGaushalas(gaushalas: List<Gaushala>)

    @Update
    suspend fun updateGaushala(gaushala: Gaushala)

    @Query("UPDATE gaushalas SET isSupported = 1 WHERE id = :id")
    suspend fun markSupported(id: String)

    // Animal Residents
    @Query("SELECT * FROM animal_residents")
    fun getAllAnimals(): Flow<List<AnimalResident>>

    @Query("SELECT * FROM animal_residents WHERE gaushalaId = :gaushalaId")
    fun getAnimalsByGaushala(gaushalaId: String): Flow<List<AnimalResident>>

    @Query("SELECT * FROM animal_residents WHERE id = :id LIMIT 1")
    fun getAnimalById(id: String): Flow<AnimalResident?>

    @Query("SELECT * FROM animal_residents WHERE isUrgent = 1")
    fun getUrgentAnimals(): Flow<List<AnimalResident>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimals(animals: List<AnimalResident>)

    @Query("UPDATE animal_residents SET raisedRupees = raisedRupees + :amount WHERE id = :id")
    suspend fun contributeToAnimal(id: String, amount: Int)

    @Query("UPDATE animal_residents SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: String, isFav: Boolean)
}
