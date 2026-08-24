package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Puja
import kotlinx.coroutines.flow.Flow

@Dao
interface PujaDao {
    @Query("SELECT * FROM pujas")
    fun getAllPujas(): Flow<List<Puja>>

    @Query("SELECT * FROM pujas WHERE id = :id LIMIT 1")
    fun getPujaById(id: String): Flow<Puja?>

    @Query("SELECT * FROM pujas WHERE isBooked = 1 ORDER BY dateTimeStr ASC")
    fun getBookedPujas(): Flow<List<Puja>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPujas(pujas: List<Puja>)

    @Update
    suspend fun updatePuja(puja: Puja)

    @Query("UPDATE pujas SET isBooked = 1, bookedGotra = :gotra, bookedDevoteeName = :name, bookedDate = :date WHERE id = :id")
    suspend fun bookPuja(id: String, gotra: String, name: String, date: String)

    @Query("UPDATE pujas SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: String, isBookmarked: Boolean)
}
