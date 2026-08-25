package com.example.data.remote.firebase

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.tasks.await

class FirebaseStorageRepositoryImpl(
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : StorageRepository {

    override suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): Result<String> {
        return try {
            val storageRef = storage.reference.child("users/$userId/avatar.jpg")
            val metadata = StorageMetadata.Builder().setContentType("image/jpeg").build()
            storageRef.putBytes(imageBytes, metadata).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Log.e("FirebaseStorageRepo", "Upload failed: ${e.message}")
            Result.failure(e)
        }
    }
}
