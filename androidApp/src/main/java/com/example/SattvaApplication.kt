package com.example

import android.app.Application
import com.example.data.remote.firebase.FirebaseInitializer
import com.example.di.AppDependencies
import com.example.data.local.AppDatabase
import com.example.data.repository.SattvaRepository

class SattvaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Single centralized startup initialization for Firebase infrastructure
        FirebaseInitializer.initialize(this)
        
        AppDependencies.database = com.example.data.local.AppDatabaseBuilder.getInstance(this)
        AppDependencies.authRepository = FirebaseInitializer.authRepository
        AppDependencies.catalogRepository = FirebaseInitializer.catalogRepository
        AppDependencies.userRepository = FirebaseInitializer.userRepository
        AppDependencies.storageRepository = FirebaseInitializer.storageRepository
        AppDependencies.notificationRepository = FirebaseInitializer.pushNotificationRepository
        AppDependencies.repository = SattvaRepository(AppDependencies.database, AppDependencies.authRepository, AppDependencies.catalogRepository, AppDependencies.userRepository, AppDependencies.storageRepository, AppDependencies.notificationRepository)
    }
}
