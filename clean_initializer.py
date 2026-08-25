with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseInitializer.kt", "r") as f:
    lines = f.readlines()

out = []
skip = False
for line in lines:
    if "Retrieve initial FCM token" in line:
        skip = True
        break
    out.append(line)

out.append("    }\n}\n")

with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseInitializer.kt", "w") as f:
    f.writelines(out)

