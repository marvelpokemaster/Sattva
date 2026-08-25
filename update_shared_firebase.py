import re
with open("shared/build.gradle.kts", "r") as f:
    content = f.read()

firebase_deps = """
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.1.2"))
            implementation("com.google.firebase:firebase-auth")
            implementation("com.google.firebase:firebase-firestore")
            implementation("com.google.firebase:firebase-storage")
            implementation("com.google.firebase:firebase-messaging")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
"""

content = content.replace("implementation(libs.androidx.room.ktx)", "implementation(libs.androidx.room.ktx)\n" + firebase_deps)

with open("shared/build.gradle.kts", "w") as f:
    f.write(content)

