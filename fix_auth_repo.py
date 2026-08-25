with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseAuthRepository.kt", "r") as f:
    content = f.read()

content = content.replace("override val currentUser: FirebaseUser?", "override val currentUser: com.example.data.model.AuthUser?")
content = content.replace("auth.currentUser", "auth.currentUser?.let { com.example.data.model.AuthUser(it.uid, it.displayName, it.email, it.photoUrl?.toString()) }")
content = content.replace("override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>", "override suspend fun signInWithGoogle(idToken: String): Result<com.example.data.model.AuthUser>")
content = content.replace("Result.success(user)", "Result.success(com.example.data.model.AuthUser(user.uid, user.displayName, user.email, user.photoUrl?.toString()))")
content = content.replace("override override val currentUserId", "override val currentUserId")

with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseAuthRepository.kt", "w") as f:
    f.write(content)
