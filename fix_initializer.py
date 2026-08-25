import re

with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseInitializer.kt", "r") as f:
    fi = f.read()

fi = fi.replace("FirebaseAuthRepository", "FirebaseAuthRepositoryImpl")
fi = fi.replace("FirestoreCatalogRepository", "FirestoreCatalogRepositoryImpl")
fi = fi.replace("FirestoreUserRepository", "FirestoreUserRepositoryImpl")
fi = fi.replace("FirebaseStorageRepository", "FirebaseStorageRepositoryImpl")
fi = fi.replace("PushNotificationRepository", "PushNotificationRepositoryImpl")

fi = fi.replace("DefaultFirebaseAuthRepositoryImpl", "FirebaseAuthRepositoryImpl")
fi = fi.replace("DefaultFirestoreCatalogRepositoryImpl", "FirestoreCatalogRepositoryImpl")
fi = fi.replace("DefaultFirestoreUserRepositoryImpl", "FirestoreUserRepositoryImpl")
fi = fi.replace("DefaultFirebaseStorageRepositoryImpl", "FirebaseStorageRepositoryImpl")
fi = fi.replace("DefaultPushNotificationRepositoryImpl", "PushNotificationRepositoryImpl")

# Remove the notification channels call since it's not in the stub
fi = re.sub(r"pushNotificationRepository\.createNotificationChannels\(context\)", "", fi)

# Fix the FCM token block which might reference getFcmToken
fi = re.sub(r"CoroutineScope\(Dispatchers\.IO\)\.launch \{[\s\S]*?\}", "", fi)

with open("shared/src/androidMain/kotlin/com/example/data/remote/firebase/FirebaseInitializer.kt", "w") as f:
    f.write(fi)
