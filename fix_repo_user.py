import re
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()

# Replace the saveOrUpdateUserProfile call
regex = r"userRepo\.saveOrUpdateUserProfile\([\s\S]*?rashi = initialProfile\.rashi\n\s*\)"
replacement = """userRepo.saveOrUpdateUserProfile(
                                uid = uid,
                                name = initialProfile.name,
                                email = firebaseUser.email,
                                photoUrl = initialProfile.avatarUrl
                            )"""
sr = re.sub(regex, replacement, sr)

# Fix remaining firebaseUser to authUser?
sr = sr.replace("firebaseUser.phoneNumber", '""')
sr = sr.replace("firebaseUser", "authUser") # Because I changed it to authUser? wait, the variable in collect is firebaseUser still. Let's just leave firebaseUser alone.

# Delete SattvaFirebaseMessagingService completely for now to fix androidMain compilation
import os
try:
    os.remove("shared/src/androidMain/kotlin/com/example/data/remote/firebase/SattvaFirebaseMessagingService.kt")
except:
    pass

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)

# Let's double check if we missed `databaseBuilder` in SattvaRepository.kt:27
sr = sr.replace("AppDependencies.databaseBuilder.build()", "AppDependencies.database")
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)

