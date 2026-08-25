with open("shared/build.gradle.kts", "r") as f:
    content = f.read()

content = content.replace("implementation(libs.ktor.client.darwin)", "implementation(libs.ktor.client.darwin)\n            implementation(\"androidx.sqlite:sqlite-bundled:2.5.0-alpha06\")")

with open("shared/build.gradle.kts", "w") as f:
    f.write(content)
