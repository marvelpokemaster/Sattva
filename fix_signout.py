with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("    fun signOut() {\n        viewModelScope.launch { com.example.di.AppDependencies.authRepository.signOut() }\n        _toastMessage.value = \"Signed out successfully.\"\n    }", "    fun signOut() {\n        viewModelScope.launch { \n            com.example.di.AppDependencies.authRepository.signOut() \n            _toastMessage.value = \"Signed out successfully.\"\n        }\n    }")

with open("shared/src/commonMain/kotlin/com/example/features/main/SattvaViewModel.kt", "w") as f:
    f.write(content)
