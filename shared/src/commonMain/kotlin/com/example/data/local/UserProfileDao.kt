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
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = :userId LIMIT 1")
    fun getUserProfileById(userId: String): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET gotra = :gotra, nakshatra = :nakshatra, rashi = :rashi")
    suspend fun updateSpiritualIdentity(gotra: String, nakshatra: String, rashi: String)

    @Query("UPDATE user_profile SET pujasCount = pujasCount + 1")
    suspend fun incrementPujaCount()

    @Query("UPDATE user_profile SET totalContributedRupees = totalContributedRupees + :amount")
    suspend fun addContribution(amount: Int)

    @Query("UPDATE user_profile SET avatarUrl = :avatarUrl")
    suspend fun updateAvatar(avatarUrl: String)
}
