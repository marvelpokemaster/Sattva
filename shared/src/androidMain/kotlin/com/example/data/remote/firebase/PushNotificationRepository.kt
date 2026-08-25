package com.example.data.remote.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object NotificationChannels {
    const val CHANNEL_PUJA_ID = "utsavam_puja_updates"
    const val CHANNEL_PUJA_NAME = "Puja & Ritual Updates"
    const val CHANNEL_PUJA_DESC = "Notifications regarding booked pujas, live links, and sankalpa completion."

    const val CHANNEL_SEVA_ID = "utsavam_seva_updates"
    const val CHANNEL_SEVA_NAME = "Seva & Sanctuary Updates"
    const val CHANNEL_SEVA_DESC = "Updates on Gaushala care, rescued animals, and medical assistance."

    const val CHANNEL_ANNOUNCEMENTS_ID = "utsavam_announcements"
    const val CHANNEL_ANNOUNCEMENTS_NAME = "Festivals & Panchang"
    const val CHANNEL_ANNOUNCEMENTS_DESC = "Daily Panchang alerts, upcoming festivals, and auspicious timings."
}

class PushNotificationRepositoryImpl(
    private val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()
) : NotificationRepository {

    override suspend fun registerDeviceToken(userId: String, token: String) {
        try {
            FirebaseFirestoreUserRepository().updateFcmToken(userId, token)
        } catch (e: Exception) {
            Log.e("PushNotificationRepo", "Failed to register device token: ${e.message}")
        }
    }

    private fun FirebaseFirestoreUserRepository() = FirestoreUserRepositoryImpl()

    override suspend fun getFcmToken(): Result<String> {
        return try {
            val token = messaging.token.await()
            Result.success(token)
        } catch (e: Exception) {
            Log.e("PushNotificationRepo", "Failed to retrieve FCM token: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun subscribeToTopic(topic: String): Result<Unit> {
        return try {
            messaging.subscribeToTopic(topic).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unsubscribeFromTopic(topic: String): Result<Unit> {
        return try {
            messaging.unsubscribeFromTopic(topic).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                ?: return

            val channels = listOf(
                NotificationChannel(
                    NotificationChannels.CHANNEL_PUJA_ID,
                    NotificationChannels.CHANNEL_PUJA_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = NotificationChannels.CHANNEL_PUJA_DESC
                    enableVibration(true)
                },
                NotificationChannel(
                    NotificationChannels.CHANNEL_SEVA_ID,
                    NotificationChannels.CHANNEL_SEVA_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = NotificationChannels.CHANNEL_SEVA_DESC
                },
                NotificationChannel(
                    NotificationChannels.CHANNEL_ANNOUNCEMENTS_ID,
                    NotificationChannels.CHANNEL_ANNOUNCEMENTS_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = NotificationChannels.CHANNEL_ANNOUNCEMENTS_DESC
                }
            )

            notificationManager.createNotificationChannels(channels)
            Log.d("PushNotificationRepo", "Notification channels initialized successfully")
        }
    }
}
