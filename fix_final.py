import re

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    vm = f.read()

vm = vm.replace("import com.example.data.remote.firebase.FirebaseAuthRepository", "")
vm = vm.replace("import com.example.data.remote.firebase.FirebaseInitializer", "import com.example.di.AppDependencies")
vm = vm.replace("val authRepo: FirebaseAuthRepository = FirebaseInitializer.authRepository", "val authRepo = AppDependencies.authRepository")
vm = vm.replace("val authRepo = FirebaseInitializer.authRepository", "val authRepo = AppDependencies.authRepository")
vm = vm.replace("AppDatabase.getInstance(application)", "AppDependencies.databaseBuilder.build()")

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(vm)


with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()

sr = sr.replace("private val authRepo: com.example.data.remote.firebase.FirebaseAuthRepository", "private val authRepo = com.example.di.AppDependencies.authRepository")
sr = sr.replace("private val authRepo: FirebaseAuthRepository", "private val authRepo = AppDependencies.authRepository")
sr = sr.replace("import com.example.data.remote.firebase.FirebaseAuthRepository", "")
sr = sr.replace("authRepo.currentUser", "authRepo.currentUser") # currentUser is in AuthRepository

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)
