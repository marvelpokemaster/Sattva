with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    sr = f.read()

sr = sr.replace("SimpleDateFormat(\"dd MMM yyyy\", Locale.getDefault()).format(Date())", 'kotlinx.datetime.Clock.System.now().toString().substringBefore("T")')
sr = sr.replace("import java.text.SimpleDateFormat\n", "")
sr = sr.replace("import java.util.Date\n", "")
sr = sr.replace("import java.util.Locale\n", "")

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(sr)
