package com.example.data.remote.firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SattvaFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Token received: $token")
        
        // Persist token in Firestore
        kotlinx.coroutines.GlobalScope.launch {
            val user = com.example.data.remote.firebase.FirebaseInitializer.authRepository.currentUser
            if (user != null) {
                com.example.data.remote.firebase.FirebaseInitializer.userRepository.updateFcmToken(user.uid, token)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        // 1. Extract payload title & body
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: getString(R.string.app_name)

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: ""

        val channelType = remoteMessage.data["channel_type"] ?: "general"
        val channelId = when (channelType) {
            "puja" -> NotificationChannels.CHANNEL_PUJA_ID
            "seva" -> NotificationChannels.CHANNEL_SEVA_ID
            else -> NotificationChannels.CHANNEL_ANNOUNCEMENTS_ID
        }

        showNotification(title, body, channelId)
    }

    private fun showNotification(title: String, message: String, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "POST_NOTIFICATIONS permission not granted, skipping notification display")
                return
            }
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationId = (System.currentTimeMillis() % 100000).toInt()
        try {
            NotificationManagerCompat.from(this).notify(notificationId, notificationBuilder.build())
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException when showing notification", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error displaying notification", e)
        }
    }

    companion object {
        private const val TAG = "SattvaFCM"
    }
}
