import os, glob, re

# Find all kt files in shared/src/commonMain
kt_files = glob.glob("shared/src/commonMain/kotlin/**/*.kt", recursive=True)

for file_path in kt_files:
    with open(file_path, "r") as f:
        content = f.read()

    # Imports
    if "java.util.Date" in content or "Date(" in content or "Date=" in content or " Date" in content or "<Date>" in content:
        content = content.replace("java.util.Date", "kotlinx.datetime.Instant")
        content = content.replace("Date()", "kotlinx.datetime.Clock.System.now()")
        content = re.sub(r'\bDate\b', 'Instant', content)
        
    if "java.util.Locale" in content:
        # replace Locale.getDefault() or Locale.ENGLISH with some workaround if needed, 
        # but let's just use a stub for commonMain or expect/actual
        pass 

    if "java.text.SimpleDateFormat" in content:
        # We need to replace SimpleDateFormat with kotlinx-datetime equivalents. 
        pass 
        
    with open(file_path, "w") as f:
        f.write(content)

# Android files
kt_files_android = glob.glob("shared/src/androidMain/kotlin/**/*.kt", recursive=True)
for file_path in kt_files_android:
    with open(file_path, "r") as f:
        content = f.read()
    if "java.util.Date" in content or "Date(" in content or "Date=" in content or " Date" in content or "<Date>" in content:
        # Since domain models use Instant, we need to convert Date to Instant in Firebase models
        content = content.replace("import java.util.Date", "import java.util.Date\nimport kotlinx.datetime.Instant\nimport kotlinx.datetime.toKotlinInstant\nimport kotlinx.datetime.toJavaInstant")
        content = re.sub(r'(\w+)\.date\(\)', r'\1.date()?.toKotlinInstant()', content) # if firebase has date
    with open(file_path, "w") as f:
        f.write(content)
