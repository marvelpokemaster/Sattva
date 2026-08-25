package com.example.data.remote.firebase

import android.util.Log
import com.example.data.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private fun FirebaseUser.toAuthUser(): AuthUser {
    return AuthUser(
        uid = this.uid,
        displayName = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl?.toString()
    )
}

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    private val TAG = "FirebaseAuthRepo"

    override val authState: Flow<AuthUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toAuthUser())
        }
        auth.addAuthStateListener(listener)
        trySend(auth.currentUser?.toAuthUser())
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override val currentUser: AuthUser?
        get() = try {
            auth.currentUser?.toAuthUser()
        } catch (e: Exception) {
            null
        }

    override val currentUserId: String
        get() = currentUser?.uid ?: ""

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Google sign-in"))
            Result.success(user.toAuthUser())
        } catch (e: Exception) {
            Log.e(TAG, "signInWithGoogle failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, pass: String): Result<AuthUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), pass).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Email sign-in"))
            Result.success(user.toAuthUser())
        } catch (e: Exception) {
            Log.e(TAG, "signInWithEmail failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signUpWithEmail(email: String, pass: String, name: String): Result<AuthUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email.trim(), pass).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Sign up"))
            
            if (name.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profileUpdates).await()
            }
            Result.success(user.toAuthUser())
        } catch (e: Exception) {
            Log.e(TAG, "signUpWithEmail failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(displayName: String): Result<AuthUser> {
        return try {
            val authResult = auth.signInAnonymously().await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Anonymous sign-in"))
            
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates).await()
            Result.success(user.toAuthUser())
        } catch (e: Exception) {
            Log.e(TAG, "signInAnonymously failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "signOut failed: ${e.message}")
        }
    }
}
