with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    content = f.read()

content = content.replace("firestoreUser.toUserProfile()", "UserProfile(firestoreUser.uid, firestoreUser.name ?: \"\", firestoreUser.email ?: \"\", firestoreUser.photoUrl ?: \"\")")
content = content.replace("firebaseUser.id", "firebaseUser.uid")
content = content.replace("catalogRepo.getPujas()", "catalogRepo.getPujas().getOrNull() ?: emptyList()")
content = content.replace("it.toPuja()", "Puja(it.id, it.title, \"\", it.templeName, it.location, it.date, \"\", \"\", it.price, it.imageUrl, \"\", \"\", \"\", \"\", \"\")")
content = content.replace("catalogRepo.getGaushalas()", "catalogRepo.getGaushalas().getOrNull() ?: emptyList()")
content = content.replace("it.toGaushala()", "Gaushala(it.id, it.name, it.location, it.state, 100, 0, \"\", it.imageUrl, \"\", 0, 0, 0)")
content = content.replace("catalogRepo.getAnimals()", "catalogRepo.getAnimals().getOrNull() ?: emptyList()")
content = content.replace("it.toAnimalResident()", "AnimalResident(it.id, it.gaushalaId, it.name, it.age, it.healthStatus, \"\", it.imageUrl, it.story, it.monthlyGoal, 0)")
content = content.replace("seedInitialDataIfEmpty()", "true")

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(content)

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    content = f.read()
content = content.replace("val authUser: StateFlow<UserProfile?>", "val authUser: StateFlow<com.example.data.model.AuthUser?>")
content = content.replace("com.example.di.AppDependencies.authRepository.signInWithEmail", "com.example.di.AppDependencies.authRepository.signInWithEmail(email, password)")
content = content.replace("com.example.di.AppDependencies.authRepository.signUpWithEmail", "com.example.di.AppDependencies.authRepository.signUpWithEmail(email, password, name)")
content = content.replace("com.example.di.AppDependencies.authRepository.signInAnonymously", "com.example.di.AppDependencies.authRepository.signInAnonymously()")
with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(content)
