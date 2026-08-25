import re
with open("shared/src/commonMain/kotlin/com/example/features/profile/ProfileScreen.kt", "r") as f:
    ps = f.read()

ps = re.sub(r"data class AuthUser\([^)]*\)\n", "", ps)
ps = ps.replace("import com.example.data.model.UserProfile", "import com.example.data.model.UserProfile\nimport com.example.data.model.AuthUser")

with open("shared/src/commonMain/kotlin/com/example/features/profile/ProfileScreen.kt", "w") as f:
    f.write(ps)
