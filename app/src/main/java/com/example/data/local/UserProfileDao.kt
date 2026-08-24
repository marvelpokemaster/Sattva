package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET gotra = :gotra, nakshatra = :nakshatra, rashi = :rashi WHERE id = 'primary_user'")
    suspend fun updateSpiritualIdentity(gotra: String, nakshatra: String, rashi: String)

    @Query("UPDATE user_profile SET pujasCount = pujasCount + 1 WHERE id = 'primary_user'")
    suspend fun incrementPujaCount()

    @Query("UPDATE user_profile SET totalContributedRupees = totalContributedRupees + :amount WHERE id = 'primary_user'")
    suspend fun addContribution(amount: Int)
}
