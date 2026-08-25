package com.example.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.data.model.AnimalResident
import com.example.data.model.Gaushala
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile

@Database(
    entities = [
        Puja::class,
        Gaushala::class,
        AnimalResident::class,
        SevaContribution::class,
        UserProfile::class
    ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pujaDao(): PujaDao
    abstract fun gaushalaDao(): GaushalaDao
    abstract fun sevaDao(): SevaDao
    abstract fun userProfileDao(): UserProfileDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

