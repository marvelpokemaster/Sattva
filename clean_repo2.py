with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    content = f.read()

content = content.replace("TODO()", "com.example.di.AppDependencies.catalogRepository", 1)
content = content.replace("TODO()", "com.example.di.AppDependencies.userRepository", 1)
content = content.replace("TODO()", "com.example.di.AppDependencies.storageRepository", 1)
content = content.replace("TODO()", "com.example.di.AppDependencies.notificationRepository", 1)
content = content.replace("Log.d(", "println(")
content = content.replace("Log.e(", "println(")

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(content)
