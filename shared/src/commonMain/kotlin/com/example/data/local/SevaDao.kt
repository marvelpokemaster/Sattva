package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SevaContribution
import kotlinx.coroutines.flow.Flow

@Dao
interface SevaDao {
    @Query("SELECT * FROM seva_contributions ORDER BY timestamp DESC")
    fun getAllContributions(): Flow<List<SevaContribution>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: SevaContribution): Long

    @Query("SELECT COUNT(*) FROM seva_contributions")
    fun getSevaCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(amountRupees), 0) FROM seva_contributions")
    fun getTotalAmountContributed(): Flow<Int>
}
