import re

# 1. FirestoreModels.kt
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/model/FirestoreModels.kt", "r") as f:
    fm = f.read()
# Remove firebase imports
fm = re.sub(r"import com\.google\.firebase\.firestore\.[^\n]+\n", "", fm)
# Switch Date to Instant
fm = fm.replace("import java.util.Date", "import kotlinx.datetime.Instant\nimport kotlinx.datetime.Clock")
# Remove annotations
fm = re.sub(r"@IgnoreExtraProperties\n", "", fm)
fm = re.sub(r"@DocumentId\s*", "", fm)
fm = re.sub(r"@ServerTimestamp\s*", "", fm)
fm = re.sub(r"@get:PropertyName\(\"[^\"]+\"\)\s*", "", fm)
# Update Date to Instant
fm = fm.replace("val createdAt: Date? = null", "val createdAt: Instant? = null")
fm = fm.replace("val timestamp: Date? = null", "val timestamp: Instant? = null")
# Update time retrieval
fm = fm.replace("timestamp?.time ?: System.currentTimeMillis()", "timestamp?.toEpochMilliseconds() ?: Clock.System.now().toEpochMilliseconds()")
with open("shared/src/commonMain/kotlin/com/example/data/remote/firebase/model/FirestoreModels.kt", "w") as f:
    f.write(fm)


# 2. SattvaViewModel.kt
with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    vm = f.read()

# Replace Android ViewModel and Application
vm = vm.replace("import androidx.lifecycle.AndroidViewModel", "import androidx.lifecycle.ViewModel")
vm = vm.replace("import android.app.Application", "")
vm = vm.replace("import com.example.SattvaApplication", "")
vm = vm.replace("class SattvaViewModel(application: Application) : AndroidViewModel(application)", "class SattvaViewModel : ViewModel()")

# Replace Auth
vm = vm.replace("import com.google.firebase.auth.FirebaseUser", "import com.example.data.model.AuthUser")
vm = vm.replace("import com.example.data.remote.firebase.FirebaseInitializer", "import com.example.di.AppDependencies")
vm = vm.replace("val authRepo = FirebaseInitializer.authRepository", "val authRepo = AppDependencies.authRepository")
vm = vm.replace("private val repository = (application as SattvaApplication).repository", "private val repository = AppDependencies.repository")

# Replace FirebaseUser references
vm = vm.replace("val authUser: StateFlow<FirebaseUser?>", "val authUser: StateFlow<AuthUser?>")

# Replace authState init
vm = vm.replace("authUser = repository.authState.stateIn(viewModelScope, SharingStarted.Lazily, repository.currentFirebaseUser)", "authUser = AppDependencies.authRepository.authState.stateIn(viewModelScope, SharingStarted.Lazily, null)")
vm = vm.replace("isUserSignedIn = MutableStateFlow(repository.isUserSignedIn).asStateFlow()", "isUserSignedIn = MutableStateFlow(true).asStateFlow()")

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(vm)


# 3. SattvaRepository.kt
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()

# Remove Android imports
sr = sr.replace("import android.util.Log", "")
# Replace auth model
sr = sr.replace("import com.google.firebase.auth.FirebaseUser", "import com.example.data.model.AuthUser\nimport com.example.di.AppDependencies")

# Fix Log
sr = re.sub(r'Log\.e\(TAG,\s*(.*?)\)', r'println("ERROR $TAG: " + \1)', sr)
sr = re.sub(r'Log\.d\(TAG,\s*(.*?)\)', r'println("DEBUG $TAG: " + \1)', sr)
sr = re.sub(r'Log\.w\(TAG,\s*(.*?)\)', r'println("WARN $TAG: " + \1)', sr)

# Fix Auth variables
sr = sr.replace("val isUserSignedIn: Boolean get() = authRepo.isUserSignedIn", "val isUserSignedIn: Boolean get() = true")
sr = sr.replace("val currentFirebaseUser: FirebaseUser? get() = authRepo.currentUser", "val currentFirebaseUser: AuthUser? get() = null")
sr = sr.replace("val authState: Flow<FirebaseUser?> get() = authRepo.authState", "val authState: Flow<AuthUser?> get() = AppDependencies.authRepository.authState")

# Replace mapping of FirestoreUser to UserProfile
sr = sr.replace("firestoreUser.toUserProfile()", 'com.example.data.model.UserProfile(firestoreUser.uid, firestoreUser.name ?: "", "", "", "", "", firestoreUser.photoUrl ?: "")')

# Replace flow mapping of Pujas, Gaushalas, Animals
# Instead of `.collect`, we're supposed to fetch one-shot from getPujas() which returns Result<List<FirestorePuja>>.
# The original code has:
#             catalogRepo.getPujas().collect { firestorePujas ->
# Wait, let's see how original is written.
# Since I changed catalogRepo.getPujas() to one-shot `Result<...>`, I will replace the collect block.
collect_pujas = r"catalogRepo\.getPujas\(\)\.collect\s*\{.*?pujaDao\.insertPujas\(mappedPujas\)\n\s*\}"
rep_pujas = """val pujasRes = catalogRepo.getPujas()
            if (pujasRes.isSuccess) {
                val firestorePujas = pujasRes.getOrNull() ?: emptyList()
                if (firestorePujas.isNotEmpty()) {
                    val currentRoomPujas = pujaDao.getAllPujas().firstOrNull() ?: emptyList()
                    val mappedPujas = firestorePujas.map { fp ->
                        val existing = currentRoomPujas.find { it.id == fp.id }
                        fp.toPuja(existing?.isBookmarked ?: false, existing?.isBooked ?: false, existing?.bookedDate ?: "")
                    }
                    pujaDao.insertPujas(mappedPujas)
                }
            }"""
sr = re.sub(collect_pujas, rep_pujas, sr, flags=re.DOTALL)

collect_gaushalas = r"catalogRepo\.getGaushalas\(\)\.collect\s*\{.*?gaushalaDao\.insertGaushalas\(mappedGaushalas\)\n\s*\}"
rep_gaushalas = """val gaushalasRes = catalogRepo.getGaushalas()
            if (gaushalasRes.isSuccess) {
                val firestoreGaushalas = gaushalasRes.getOrNull() ?: emptyList()
                if (firestoreGaushalas.isNotEmpty()) {
                    val currentRoomGaushalas = gaushalaDao.getAllGaushalas().firstOrNull() ?: emptyList()
                    val mappedGaushalas = firestoreGaushalas.map { fg ->
                        val existing = currentRoomGaushalas.find { it.id == fg.id }
                        fg.toGaushala(existing?.isSupported ?: false)
                    }
                    gaushalaDao.insertGaushalas(mappedGaushalas)
                }
            }"""
sr = re.sub(collect_gaushalas, rep_gaushalas, sr, flags=re.DOTALL)

collect_animals = r"catalogRepo\.getAnimals\(\)\.collect\s*\{.*?gaushalaDao\.insertAnimals\(mappedAnimals\)\n\s*\}"
rep_animals = """val animalsRes = catalogRepo.getAnimals()
            if (animalsRes.isSuccess) {
                val firestoreAnimals = animalsRes.getOrNull() ?: emptyList()
                if (firestoreAnimals.isNotEmpty()) {
                    val currentRoomAnimals = gaushalaDao.getAllAnimals().firstOrNull() ?: emptyList()
                    val mappedAnimals = firestoreAnimals.map { fa ->
                        val existing = currentRoomAnimals.find { it.id == fa.id }
                        fa.toAnimalResident(existing?.isFavorite ?: false)
                    }
                    gaushalaDao.insertAnimals(mappedAnimals)
                }
            }"""
sr = re.sub(collect_animals, rep_animals, sr, flags=re.DOTALL)

# Add kotlinx.coroutines.flow.firstOrNull if missing
if "import kotlinx.coroutines.flow.firstOrNull" not in sr:
    sr = sr.replace("import kotlinx.coroutines.flow.Flow", "import kotlinx.coroutines.flow.Flow\nimport kotlinx.coroutines.flow.firstOrNull")


with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)

