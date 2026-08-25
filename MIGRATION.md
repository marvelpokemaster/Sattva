# Kotlin Multiplatform Migration Audit & Progress

## Current Architecture
- **Entry Point**: `MainActivity.kt` wrapping a Material 3 `Scaffold`.
- **UI Framework**: Pure Jetpack Compose. No XML or Android Views are mixed in.
- **Navigation**: Manual, state-driven approach (`Crossfade` over an `enum class MainTab`) controlled by `SattvaViewModel`. `androidx.navigation` is absent, simplifying UI migration.
- **State Management**: A single `SattvaViewModel` backed by `AndroidViewModel`.
- **Persistence**: Room Database (`2.7.0`) caching data locally.
- **Cloud/Backend**: Native Android Google Firebase SDKs (Auth, Firestore, Storage, Messaging).
- **AI/Networking**: Retrofit2 pointing to a FastAPI proxy.
- **Image Loading**: Coil `2.7.0` (`AsyncImage`).

## New Module Architecture (AGP 9+ Compliant)
To comply with AGP 9.1.1 requirements, we avoided mixing `com.android.application` and `kotlin("multiplatform")` in a single module. The project is now structured as:
```
Sattva/
├── androidApp/           # com.android.application (Pure Android entry & existing Android code)
├── iosApp/               # Xcode project placeholder
└── shared/               # com.android.library + kotlin("multiplatform") + compose-plugin
```

## What Has Already Migrated
- **Module Split**: The core physical separation has been completed. The existing Android code is safely isolated in `androidApp/`.
- **Compose Multiplatform Setup**: `shared/` is configured with `commonMain`, `androidMain`, and `iosMain`.
- **Shared App Entry**: A minimal Compose Multiplatform `@Composable fun App()` exists in `shared` and is successfully wired into `androidApp/MainActivity.kt`.

## What Remains Android-Specific (Not Yet Migrated)
- The entire existing UI (`HomeScreen`, `ExploreScreen`, etc.) currently resides in `androidApp/`.
- Firebase Authentication, Firestore, Storage, and Messaging.
- Room Database (`AppDatabase.kt`).
- Retrofit networking (`BackendAiService.kt`).
- Coil `2.7.0` image loading.
- `SattvaViewModel` and `SattvaRepository`.

## Next Migration Step
Move the UI components (`SattvaTopBar`, `SattvaBottomNav`, `HomeScreen`, `ExploreScreen`, etc.) to `shared/src/commonMain/kotlin`. This will require upgrading Coil to `3.x` for multiplatform `AsyncImage` support and decoupling `SattvaViewModel` from `AndroidViewModel`.

## Known Blockers / Risks
- **Firebase SDK**: Moving Firebase logic to `commonMain` requires `dev.gitlive:firebase-kotlin-sdk`. Push notifications (FCM) may require significant iOS-specific implementation.
- **Room Multiplatform**: While Room 2.7.0 is used, adding the multiplatform `RoomDatabaseConstructor` and SQLite bundled driver (`androidx.sqlite:sqlite-bundled`) requires expect/actual boilerplate.
