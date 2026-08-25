import re

# FirestoreModels.kt
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/model/FirestoreModels.kt", "r") as f:
    fm = f.read()

fm = re.sub(r"import com\.google\.firebase\.firestore\..*\n", "", fm)
fm = fm.replace("import java.util.Date", "import kotlinx.datetime.Instant")
fm = re.sub(r"@IgnoreExtraProperties\s*", "", fm)
fm = re.sub(r"@DocumentId\s*", "", fm)
fm = re.sub(r"@ServerTimestamp\s*", "", fm)
fm = re.sub(r"@get:PropertyName\(\".*?\"\)\s*", "", fm)
fm = fm.replace("val createdAt: Date?", "val createdAt: Instant?")
fm = fm.replace("val timestamp: Date?", "val timestamp: Instant?")
fm = fm.replace("timestamp?.time ?: System.currentTimeMillis()", "timestamp?.toEpochMilliseconds() ?: kotlinx.datetime.Clock.System.now().toEpochMilliseconds()")

with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/model/FirestoreModels.kt", "w") as f:
    f.write(fm)

# SattvaRepository.kt
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()

sr = sr.replace("import android.util.Log", "")
sr = sr.replace("import com.google.firebase.auth.FirebaseUser", "import com.example.data.model.AuthUser")
sr = sr.replace("Log.e(TAG, ", "println(\"ERROR $TAG: \" + ")
sr = sr.replace("Log.d(TAG, ", "println(\"DEBUG $TAG: \" + ")

# In SattvaRepository, replace authRepo.authState.collect -> authRepo.currentUser.collect (wait, we changed AuthRepository to authState: Flow<AuthUser?>)
sr = sr.replace("authRepo.authState.collect", "com.example.di.AppDependencies.authRepository.authState.collect")
sr = sr.replace("val currentFirebaseUser: FirebaseUser? get() = authRepo.currentUser", "")
sr = sr.replace("firebaseUser.uid", "firebaseUser.uid") # keep uid, we use uid in AuthUser
sr = sr.replace("firestoreUser.toUserProfile()", "com.example.data.model.UserProfile(firestoreUser.uid, firestoreUser.name ?: \"\", firestoreUser.email ?: \"\", firestoreUser.photoUrl ?: \"\")")
sr = sr.replace("firestoreUser.photoUrl", "firestoreUser.avatarUrl") # wait, firestoreUser has photoUrl, but I'm mapping it.

# Firebase Interfaces change for getPujas()
sr = sr.replace("catalogRepo.getPujas()", "catalogRepo.getPujas().getOrNull() ?: emptyList()")
sr = sr.replace("catalogRepo.getGaushalas()", "catalogRepo.getGaushalas().getOrNull() ?: emptyList()")
sr = sr.replace("catalogRepo.getAnimals()", "catalogRepo.getAnimals().getOrNull() ?: emptyList()")
# wait, .onSuccess does not work on emptyList().
# In original code, it was `catalogRepo.getPujas().collect { ... }` or `onSuccess`?
# In androidApp, catalogRepo.getPujas() returned Result<List<FirestorePuja>>. And they did .onSuccess
# We'll regex the whole block again.
block_regex = r"catalogRepo\.getPujas\(\)\.onSuccess\s*\{.*?gaushalaDao\.insertAnimals\(mappedAnimals\)\n\s*\}"
replacement = """
            val pujasRes = catalogRepo.getPujas()
            if (pujasRes.isSuccess) {
                val firestorePujas = pujasRes.getOrNull() ?: emptyList()
                if (firestorePujas.isNotEmpty()) {
                    val currentRoomPujas = pujaDao.getAllPujas().firstOrNull() ?: emptyList()
                    val mappedPujas = firestorePujas.map { fp ->
                        val existing = currentRoomPujas.find { it.id == fp.id }
                        fp.toPuja(
                            isBookmarked = existing?.isBookmarked ?: false,
                            isBooked = existing?.isBooked ?: false,
                            bookedDate = existing?.bookedDate ?: ""
                        )
                    }
                    pujaDao.insertPujas(mappedPujas)
                }
            }

            val gaushalasRes = catalogRepo.getGaushalas()
            if (gaushalasRes.isSuccess) {
                val firestoreGaushalas = gaushalasRes.getOrNull() ?: emptyList()
                if (firestoreGaushalas.isNotEmpty()) {
                    val currentRoomGaushalas = gaushalaDao.getAllGaushalas().firstOrNull() ?: emptyList()
                    val mappedGaushalas = firestoreGaushalas.map { fg ->
                        val existing = currentRoomGaushalas.find { it.id == fg.id }
                        fg.toGaushala(isSupported = existing?.isSupported ?: false)
                    }
                    gaushalaDao.insertGaushalas(mappedGaushalas)
                }
            }

            val animalsRes = catalogRepo.getAnimals()
            if (animalsRes.isSuccess) {
                val firestoreAnimals = animalsRes.getOrNull() ?: emptyList()
                if (firestoreAnimals.isNotEmpty()) {
                    val currentRoomAnimals = gaushalaDao.getAllAnimals().firstOrNull() ?: emptyList()
                    val mappedAnimals = firestoreAnimals.map { fa ->
                        val existing = currentRoomAnimals.find { it.id == fa.id }
                        fa.toAnimalResident(isFavorite = existing?.isFavorite ?: false)
                    }
                    gaushalaDao.insertAnimals(mappedAnimals)
                }
            }
"""
sr = re.sub(block_regex, replacement.strip(), sr, flags=re.DOTALL)

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)

# SattvaViewModel.kt
with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    vm = f.read()

vm = vm.replace("import androidx.lifecycle.AndroidViewModel", "import androidx.lifecycle.ViewModel")
vm = vm.replace("import android.app.Application", "")
vm = vm.replace("import com.google.firebase.auth.FirebaseUser", "import com.example.data.model.AuthUser")
vm = vm.replace("class SattvaViewModel(application: Application) : AndroidViewModel(application)", "class SattvaViewModel : ViewModel()")
vm = vm.replace("private val repository = (application as com.example.SattvaApplication).repository", "private val repository = com.example.di.AppDependencies.repository")
vm = vm.replace("val authUser: StateFlow<FirebaseUser?>", "val authUser: StateFlow<AuthUser?>")
vm = vm.replace("authUser = repository.authState.stateIn(viewModelScope, SharingStarted.Lazily, repository.currentFirebaseUser)", "authUser = com.example.di.AppDependencies.authRepository.authState.stateIn(viewModelScope, SharingStarted.Lazily, null)")
vm = vm.replace("isUserSignedIn = MutableStateFlow(repository.isUserSignedIn).asStateFlow()", "isUserSignedIn = MutableStateFlow(true).asStateFlow()")
vm = vm.replace("import com.example.data.remote.firebase.FirebaseInitializer", "")
vm = vm.replace("val authRepo = FirebaseInitializer.authRepository", "val authRepo = com.example.di.AppDependencies.authRepository")

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(vm)

