package com.example.core.config

import platform.Foundation.NSUserDefaults

actual fun getLocalThemePreference(): ThemePreference {
    val saved = NSUserDefaults.standardUserDefaults.stringForKey("theme_pref")
    return try {
        saved?.let { ThemePreference.valueOf(it) } ?: ThemePreference.SYSTEM
    } catch (e: Exception) {
        ThemePreference.SYSTEM
    }
}

actual fun setLocalThemePreference(pref: ThemePreference) {
    NSUserDefaults.standardUserDefaults.setObject(pref.name, "theme_pref")
}
