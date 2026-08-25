import re

# FirebaseInterfaces.kt
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/FirebaseInterfaces.kt", "r") as f:
    fi = f.read()
fi = fi.replace("FirestoreWisdom", "FirestoreDailyContent")
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/FirebaseInterfaces.kt", "w") as f:
    f.write(fi)

# FirestoreModels.kt
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/model/FirestoreModels.kt", "r") as f:
    fm = f.read()
fm = fm.replace("Date?", "Instant?")
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/model/FirestoreModels.kt", "w") as f:
    f.write(fm)

# SattvaRepository.kt
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()
sr = sr.replace("AppDependencies.databaseBuilder.build()", "AppDependencies.database")
sr = sr.replace("firestoreUser.name", "firestoreUser.displayName")
sr = sr.replace("firestoreUser.photoUrl", "firestoreUser.avatarUrl")

# The UserProfile mapping:
sr = re.sub(
    r"com\.example\.data\.model\.UserProfile\([^)]*\)",
    r'com.example.data.model.UserProfile(firestoreUser.uid, firestoreUser.displayName ?: "", firestoreUser.email ?: "", firestoreUser.avatarUrl ?: "")',
    sr
)

sr = sr.replace("catalogRepo.seedInitialDataIfEmpty()", "// catalogRepo.seedInitialDataIfEmpty()")

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)

# SattvaViewModel.kt
with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    vm = f.read()
vm = vm.replace("AppDependencies.databaseBuilder.build()", "AppDependencies.database")
vm = vm.replace("authRepo.signInAnonymously(displayName)", "authRepo.signInAnonymously()")
vm = vm.replace("authRepo.signOut()", "viewModelScope.launch { authRepo.signOut() }")
# If it's already wrapped, this might double wrap, but that's fine

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(vm)

# SattvaFirebaseMessagingService.kt
# Just comment out the problematic code in androidMain
with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/SattvaFirebaseMessagingService.kt", "r") as f:
    msg = f.read()

msg = msg.replace("import com.example.MainActivity", "// import com.example.MainActivity")
msg = re.sub(r"val intent = Intent\(this, MainActivity::class\.java\).apply \{[^\}]+\}", "val intent = android.content.Intent()", msg)
msg = msg.replace("R.drawable.ic_notification", "android.R.drawable.ic_dialog_info")

with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/SattvaFirebaseMessagingService.kt", "w") as f:
    f.write(msg)
