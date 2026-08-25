import re
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()

constructor_regex = r"class SattvaRepository\([^)]*\)"
new_constructor = """class SattvaRepository(
    private val database: AppDatabase = AppDependencies.databaseBuilder.build(),
    private val authRepo: com.example.data.remote.firebase.AuthRepository = AppDependencies.authRepository,
    private val catalogRepo: com.example.data.remote.firebase.CatalogRepository = AppDependencies.catalogRepository,
    private val userRepo: com.example.data.remote.firebase.UserRepository = AppDependencies.userRepository,
    private val storageRepo: com.example.data.remote.firebase.StorageRepository = AppDependencies.storageRepository,
    private val pushRepo: com.example.data.remote.firebase.NotificationRepository = AppDependencies.notificationRepository
)"""

sr = re.sub(constructor_regex, new_constructor, sr)

# Also fix flow authState type:
sr = sr.replace("val authState: Flow<FirebaseUser?> = authRepo.authState", "val authState: Flow<AuthUser?> = authRepo.authState")

# Remove all FirebaseInitializer imports
sr = re.sub(r"import com\.example\.data\.remote\.firebase\.FirebaseInitializer.*\n", "", sr)
sr = re.sub(r"import com\.example\.data\.remote\.firebase\.FirestoreCatalogRepository.*\n", "", sr)
sr = re.sub(r"import com\.example\.data\.remote\.firebase\.FirestoreUserRepository.*\n", "", sr)
sr = re.sub(r"import com\.example\.data\.remote\.firebase\.FirebaseStorageRepository.*\n", "", sr)
sr = re.sub(r"import com\.example\.data\.remote\.firebase\.PushNotificationRepository.*\n", "", sr)

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)
