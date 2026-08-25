package com.example.data.local

import android.content.Context
import androidx.room.Room

object AppDatabaseBuilder {
    fun getInstance(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "sattva_spiritual_db"
        ).fallbackToDestructiveMigration().build()
    }
}
