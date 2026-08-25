package com.example.data.remote.firebase

import com.example.data.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FirebaseAuthRepositoryImpl : AuthRepository {
    private val _authState = MutableStateFlow<AuthUser?>(AuthUser("dummy", "Devotee", "", ""))
    override val currentUser: AuthUser? get() = _authState.value
    override val authState: Flow<AuthUser?> = _authState
    override val currentUserId: String = "dummy"

    override suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signInAnonymously(): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> = Result.success(currentUser!!)
    override suspend fun signOut() {}
}
