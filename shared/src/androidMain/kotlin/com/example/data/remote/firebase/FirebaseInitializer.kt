package com.example.data.remote.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clean entry point for Firebase initialization at application startup.
 * Handles graceful initialization so the app continues to function seamlessly
 * in all environments (with or without local google-services.json).
 */
object FirebaseInitializer {
    private const val TAG = "FirebaseInitializer"

    var isInitialized: Boolean = false
        private set

    lateinit var authRepository: FirebaseAuthRepositoryImpl
        private set

    lateinit var catalogRepository: FirestoreCatalogRepositoryImpl
        private set

    lateinit var userRepository: FirestoreUserRepositoryImpl
        private set

    lateinit var storageRepository: FirebaseStorageRepositoryImpl
        private set

    lateinit var pushNotificationRepository: PushNotificationRepositoryImpl
        private set

    fun initialize(context: Context) {
        try {
            // Check if default FirebaseApp is already initialized
            val app = FirebaseApp.initializeApp(context)
            isInitialized = app != null || FirebaseApp.getApps(context).isNotEmpty()
            Log.i(TAG, "Firebase initialized successfully. Active apps count: ${FirebaseApp.getApps(context).size}")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase automatic initialization deferred: ${e.message}")
            isInitialized = false
        }

        // Initialize repositories
        authRepository = FirebaseAuthRepositoryImpl()
        catalogRepository = FirestoreCatalogRepositoryImpl()
        userRepository = FirestoreUserRepositoryImpl()
        storageRepository = FirebaseStorageRepositoryImpl()
        pushNotificationRepository = PushNotificationRepositoryImpl()

        // Configure notification channels for FCM
        

    }
}
