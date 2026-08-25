import os, glob, re

for file_path in glob.glob("shared/src/androidMain/kotlin/com/example/data/remote/firebase/*.kt"):
    with open(file_path, "r") as f:
        content = f.read()

    # Need to override: val currentUser, val currentUserId, suspend fun signInWithGoogle, etc.
    # We can just blindly replace 'fun ' and 'val ' with 'override fun ' and 'override val ' inside these classes if they are in the interface
    if "FirebaseAuthRepository" in content:
        content = re.sub(r'val currentUser', r'override val currentUser', content)
        content = re.sub(r'val currentUserId', r'override val currentUserId', content)
        content = re.sub(r'suspend fun signInWithGoogle', r'override suspend fun signInWithGoogle', content)
        content = re.sub(r'suspend fun signOut', r'override suspend fun signOut', content)
    if "FirestoreCatalogRepository" in content:
        content = re.sub(r'fun observePujas', r'override fun observePujas', content)
        content = re.sub(r'fun observeGaushalas', r'override fun observeGaushalas', content)
        content = re.sub(r'fun observeAnimals', r'override fun observeAnimals', content)
        content = re.sub(r'fun observeDailyWisdom', r'override fun observeDailyWisdom', content)
        content = re.sub(r'suspend fun getPuja', r'override suspend fun getPuja', content)
        content = re.sub(r'suspend fun getGaushala', r'override suspend fun getGaushala', content)
    if "FirestoreUserRepository" in content:
        content = re.sub(r'fun observeUserProfile', r'override fun observeUserProfile', content)
        content = re.sub(r'suspend fun updateUserProfile', r'override suspend fun updateUserProfile', content)
        content = re.sub(r'suspend fun addFamilyMember', r'override suspend fun addFamilyMember', content)
        content = re.sub(r'suspend fun bookPuja', r'override suspend fun bookPuja', content)
        content = re.sub(r'suspend fun recordSeva', r'override suspend fun recordSeva', content)
    if "FirebaseStorageRepository" in content:
        content = re.sub(r'suspend fun uploadProfilePicture', r'override suspend fun uploadProfilePicture', content)
    if "PushNotificationRepository" in content:
        content = re.sub(r'suspend fun registerDeviceToken', r'override suspend fun registerDeviceToken', content)

    # Clean up double overrides if they exist
    content = content.replace("override override", "override")
    
    with open(file_path, "w") as f:
        f.write(content)
