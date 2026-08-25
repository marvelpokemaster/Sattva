package com.example.`data`.local

import androidx.room.RoomDatabaseConstructor

public actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
  override fun initialize(): AppDatabase = com.example.`data`.local.AppDatabase_Impl()
}
