package com.example.data.remote.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.tasks.await

/**
 * Storage paths definitions for Utsavam / Sattva assets in Firebase Storage.
 */
object StoragePaths {
    fun userAvatar(userId: String): String = "users/$userId/avatar.jpg"
    fun animalPhoto(animalId: String, fileName: String): String = "animals/$animalId/$fileName"
    fun gaushalaImage(gaushalaId: String, fileName: String): String = "gaushalas/$gaushalaId/$fileName"
    fun pujaProof(pujaId: String, bookingId: String, fileName: String): String = "pujas/$pujaId/proofs/$bookingId/$fileName"
    fun pujaVideo(pujaId: String, bookingId: String, fileName: String): String = "pujas/$pujaId/videos/$bookingId/$fileName"
    fun certificate(documentId: String, fileName: String): String = "documents/$documentId/$fileName"
}

/**
 * Repository interface for interacting with Firebase Cloud Storage.
 * Provides safe upload, download URL generation, and deletion abstractions.
 */
interface FirebaseStorageRepository {
    suspend fun uploadBytes(path: String, data: ByteArray, mimeType: String? = null): Result<String>
    suspend fun uploadUri(path: String, fileUri: Uri, mimeType: String? = null): Result<String>
    suspend fun getDownloadUrl(path: String): Result<String>
    suspend fun deleteFile(path: String): Result<Unit>
}

class DefaultFirebaseStorageRepository(
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : FirebaseStorageRepository {

    override suspend fun uploadBytes(path: String, data: ByteArray, mimeType: String?): Result<String> {
        return try {
            val storageRef = storage.reference.child(path)
            val metadata = mimeType?.let {
                StorageMetadata.Builder().setContentType(it).build()
            }

            if (metadata != null) {
                storageRef.putBytes(data, metadata).await()
            } else {
                storageRef.putBytes(data).await()
            }

            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadUri(path: String, fileUri: Uri, mimeType: String?): Result<String> {
        return try {
            val storageRef = storage.reference.child(path)
            val metadata = mimeType?.let {
                StorageMetadata.Builder().setContentType(it).build()
            }

            if (metadata != null) {
                storageRef.putFile(fileUri, metadata).await()
            } else {
                storageRef.putFile(fileUri).await()
            }

            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDownloadUrl(path: String): Result<String> {
        return try {
            val storageRef = storage.reference.child(path)
            val url = storageRef.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteFile(path: String): Result<Unit> {
        return try {
            val storageRef = storage.reference.child(path)
            storageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
