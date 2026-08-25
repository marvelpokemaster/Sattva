package com.example.di

import com.example.data.repository.SattvaRepository
import com.example.data.remote.firebase.*

object AppDependencies {
    lateinit var database: com.example.data.local.AppDatabase
    lateinit var repository: SattvaRepository
    lateinit var authRepository: AuthRepository
    lateinit var catalogRepository: CatalogRepository
    lateinit var userRepository: UserRepository
    lateinit var storageRepository: StorageRepository
    lateinit var notificationRepository: NotificationRepository
}
