package com.example.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Architectural abstraction for Firebase Authentication.
 * Exposes auth state, current user, ID tokens, and sign-in / sign-out capabilities.
 */
interface FirebaseAuthRepository {
    val authState: Flow<FirebaseUser?>
    val currentUser: FirebaseUser?
    val isUserSignedIn: Boolean
    val currentUserId: String

    suspend fun getIdToken(forceRefresh: Boolean = false): Result<String?>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>
    suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): Result<FirebaseUser>
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser>
    suspend fun signUpWithEmail(email: String, password: String, displayName: String): Result<FirebaseUser>
    suspend fun signInAnonymously(displayName: String = "Devotee"): Result<FirebaseUser>
    fun signOut()
}

class DefaultFirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : FirebaseAuthRepository {

    private val TAG = "FirebaseAuthRepo"

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        // Emit initial value
        trySend(auth.currentUser)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override val currentUser: FirebaseUser?
        get() = try {
            auth.currentUser
        } catch (e: Exception) {
            null
        }

    override val isUserSignedIn: Boolean
        get() = currentUser != null

    override val currentUserId: String
        get() = currentUser?.uid ?: ""

    override suspend fun getIdToken(forceRefresh: Boolean): Result<String?> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(IllegalStateException("No authenticated Firebase user found"))
            val tokenResult = user.getIdToken(forceRefresh).await()
            Result.success(tokenResult.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Google sign-in"))
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithGoogle failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Phone sign-in"))
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithPhoneCredential failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Email sign-in"))
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithEmail failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String, displayName: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Sign up"))
            
            if (displayName.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                user.updateProfile(profileUpdates).await()
            }
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "signUpWithEmail failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(displayName: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInAnonymously().await()
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Firebase user was null after Anonymous sign-in"))
            
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates).await()
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "signInAnonymously failed: ${e.message}")
            Result.failure(e)
        }
    }

    override fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "signOut failed: ${e.message}")
        }
    }
}
