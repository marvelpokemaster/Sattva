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

    lateinit var authRepository: AuthRepository
        private set

    lateinit var catalogRepository: CatalogRepository
        private set

    lateinit var userRepository: UserRepository
        private set

    lateinit var storageRepository: StorageRepository
        private set

    lateinit var pushNotificationRepository: NotificationRepository
        private set

    fun initialize(context: Context) {
        try {
            val app = FirebaseApp.initializeApp(context)
            isInitialized = app != null || FirebaseApp.getApps(context).isNotEmpty()
            Log.i(TAG, "Firebase initialized successfully. Active apps count: ${FirebaseApp.getApps(context).size}")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase automatic initialization deferred: ${e.message}")
            isInitialized = false
        }

        val authImpl = FirebaseAuthRepositoryImpl()
        val catalogImpl = com.example.data.remote.ktor.KtorCatalogRepositoryImpl()
        val userImpl = com.example.data.remote.ktor.KtorUserRepositoryImpl(authImpl)
        val storageImpl = FirebaseStorageRepositoryImpl()
        val pushImpl = PushNotificationRepositoryImpl()

        authRepository = authImpl
        catalogRepository = catalogImpl
        userRepository = userImpl
        storageRepository = storageImpl
        pushNotificationRepository = pushImpl

        // Configure notification channels for FCM
        pushImpl.createNotificationChannels(context)

        // Retrieve initial FCM token in background if available
        CoroutineScope(Dispatchers.IO).launch {
            try {
                pushImpl.getFcmToken().onSuccess { token ->
                    Log.d(TAG, "Initial FCM Token obtained: $token")
                    val currentUid = authImpl.currentUserId
                    if (currentUid.isNotBlank()) {
                        userImpl.updateFcmToken(currentUid, token)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "FCM token retrieval skipped: ${e.message}")
            }
        }
    }
}
