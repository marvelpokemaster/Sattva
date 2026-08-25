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

    lateinit var authRepository: FirebaseAuthRepository
        private set

    lateinit var catalogRepository: FirestoreCatalogRepository
        private set

    lateinit var userRepository: FirestoreUserRepository
        private set

    lateinit var storageRepository: FirebaseStorageRepository
        private set

    lateinit var pushNotificationRepository: PushNotificationRepository
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
        authRepository = DefaultFirebaseAuthRepository()
        catalogRepository = DefaultFirestoreCatalogRepository()
        userRepository = DefaultFirestoreUserRepository()
        storageRepository = DefaultFirebaseStorageRepository()
        pushNotificationRepository = DefaultPushNotificationRepository()

        // Configure notification channels for FCM
        pushNotificationRepository.createNotificationChannels(context)

        // Retrieve initial FCM token in background if available and link to current user if signed in
        CoroutineScope(Dispatchers.IO).launch {
            try {
                pushNotificationRepository.getFcmToken().onSuccess { token ->
                    Log.d(TAG, "Initial FCM Token obtained: $token")
                    val currentUid = authRepository.currentUserId
                    if (currentUid.isNotBlank()) {
                        userRepository.updateFcmToken(currentUid, token)
                    }
                }.onFailure { err ->
                    Log.d(TAG, "FCM token initialization deferred: ${err.message}")
                }
            } catch (e: Exception) {
                Log.d(TAG, "FCM token retrieval skipped: ${e.message}")
            }
        }
    }
}
