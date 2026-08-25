package com.example.data.remote.firebase

class FirebaseStorageRepositoryImpl : StorageRepository {
    override suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): Result<String> = Result.success("")
}
