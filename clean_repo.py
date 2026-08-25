import re

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    content = f.read()

content = content.replace("import android.util.Log", "import com.example.core.util.Log")
content = content.replace("import com.google.firebase.auth.FirebaseUser", "")
content = content.replace("FirebaseAuthRepository", "AuthRepository")
content = content.replace("FirestoreCatalogRepository", "CatalogRepository")
content = content.replace("FirestoreUserRepository", "UserRepository")
content = content.replace("FirebaseStorageRepository", "StorageRepository")
content = content.replace("PushNotificationRepository", "NotificationRepository")
content = content.replace("FirebaseInitializer.authRepository", "com.example.di.AppDependencies.authRepository")
content = content.replace("FirebaseInitializer.catalogRepository", "TODO()") # We will initialize this through AppDependencies soon or remove default args
content = content.replace("FirebaseInitializer.userRepository", "TODO()")
content = content.replace("FirebaseInitializer.storageRepository", "TODO()")
content = content.replace("FirebaseInitializer.pushNotificationRepository", "TODO()")
content = content.replace("import com.example.data.remote.firebase.FirebaseInitializer", "")

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(content)
