with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseAuthRepository.kt", "r") as f:
    content = f.read()

impl = """
    override val authState: kotlinx.coroutines.flow.Flow<com.example.data.model.AuthUser?> = kotlinx.coroutines.channels.awaitClose {
        // Implementation provided by Firebase auth state listener
    }.let {
        kotlinx.coroutines.flow.callbackFlow {
            val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.let { com.example.data.model.AuthUser(it.uid, it.displayName, it.email, it.photoUrl?.toString()) })
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }
    }
"""

content = content.replace("override val authState: Flow<com.example.data.model.AuthUser?>", impl)
with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseAuthRepository.kt", "w") as f:
    f.write(content)
