package com.example.core.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemePreference {
    SYSTEM, LIGHT, DARK
}

expect fun getLocalThemePreference(): ThemePreference
expect fun setLocalThemePreference(pref: ThemePreference)

object ThemeSettings {
    private val _themePreference = MutableStateFlow(getLocalThemePreference())
    val themePreference: StateFlow<ThemePreference> = _themePreference.asStateFlow()

    fun updateTheme(pref: ThemePreference) {
        setLocalThemePreference(pref)
        _themePreference.value = pref
    }
}
