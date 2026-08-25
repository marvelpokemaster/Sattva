package com.example.core.config

import android.content.Context
import android.content.SharedPreferences

var applicationContext: Context? = null

private val prefs: SharedPreferences?
    get() = applicationContext?.getSharedPreferences("sattva_settings", Context.MODE_PRIVATE)

actual fun getLocalThemePreference(): ThemePreference {
    val saved = prefs?.getString("theme_pref", ThemePreference.SYSTEM.name)
    return try {
        saved?.let { ThemePreference.valueOf(it) } ?: ThemePreference.SYSTEM
    } catch (e: Exception) {
        ThemePreference.SYSTEM
    }
}

actual fun setLocalThemePreference(pref: ThemePreference) {
    prefs?.edit()?.putString("theme_pref", pref.name)?.apply()
}
