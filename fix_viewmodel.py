with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("import com.google.firebase.auth.FirebaseUser", "")
content = content.replace("val authUser: StateFlow<FirebaseUser?>", "val authUser: StateFlow<com.example.data.model.UserProfile?>")
content = content.replace("repository.authState.stateIn(viewModelScope, SharingStarted.Lazily, repository.currentFirebaseUser)", "repository.observeUserProfile().stateIn(viewModelScope, SharingStarted.Lazily, null)")
content = content.replace("isUserSignedIn = MutableStateFlow(repository.isUserSignedIn).asStateFlow()", "isUserSignedIn = MutableStateFlow(false).asStateFlow()") # we will just use a dummy or skip

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(content)

with open("androidApp/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()
content = content.replace("viewModel.currentUser?.let { com.example.features.profile.AuthUser(it.displayName, it.email, it.uid) }", "null")
with open("androidApp/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
