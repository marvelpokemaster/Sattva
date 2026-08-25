package com.example.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    primaryContainer = PrimaryContainerColor,
    onPrimaryContainer = OnPrimaryContainerColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    secondaryContainer = SecondaryContainerColor,
    onSecondaryContainer = OnSecondaryContainerColor,
    tertiary = TertiaryColor,
    onTertiary = OnTertiaryColor,
    tertiaryContainer = TertiaryContainerColor,
    onTertiaryContainer = OnTertiaryContainerColor,
    background = SurfaceColor,
    onBackground = OnSurfaceColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineColor,
    outlineVariant = OutlineVariant,
    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainerColor,
    onErrorContainer = OnErrorContainerColor
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryColor,
    onPrimary = DarkOnPrimaryColor,
    primaryContainer = DarkPrimaryContainerColor,
    onPrimaryContainer = DarkOnPrimaryContainerColor,
    secondary = DarkSecondaryColor,
    onSecondary = DarkOnSecondaryColor,
    secondaryContainer = DarkSecondaryContainerColor,
    onSecondaryContainer = DarkOnSecondaryContainerColor,
    tertiary = DarkTertiaryColor,
    onTertiary = DarkOnTertiaryColor,
    tertiaryContainer = DarkTertiaryContainerColor,
    onTertiaryContainer = DarkOnTertiaryContainerColor,
    background = DarkSurfaceColor,
    onBackground = DarkOnSurfaceColor,
    surface = DarkSurfaceColor,
    onSurface = DarkOnSurfaceColor,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutlineColor,
    outlineVariant = DarkOutlineVariant,
    error = ErrorContainerColor,
    onError = OnErrorContainerColor
)

@Composable
fun SattvaTheme(
    content: @Composable () -> Unit
) {
    val themePref by com.example.core.config.ThemeSettings.themePreference.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    
    val darkTheme = when (themePref) {
        com.example.core.config.ThemePreference.LIGHT -> false
        com.example.core.config.ThemePreference.DARK -> true
        com.example.core.config.ThemePreference.SYSTEM -> isSystemDark
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
