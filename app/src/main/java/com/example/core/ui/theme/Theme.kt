package com.example.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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
    primary = PrimaryContainerColor,
    onPrimary = OnSurfaceColor,
    primaryContainer = PrimaryColor,
    onPrimaryContainer = OnPrimaryColor,
    secondary = SecondaryFixedDim,
    onSecondary = OnSurfaceColor,
    secondaryContainer = RitualClayDark,
    onSecondaryContainer = SecondaryContainerColor,
    tertiary = TertiaryFixedDim,
    onTertiary = DeepMoss,
    tertiaryContainer = DeepMoss,
    onTertiaryContainer = TertiaryContainerColor,
    background = OnSurfaceColor,
    onBackground = SurfaceColor,
    surface = Color(0xFF222323),
    onSurface = SurfaceColor,
    surfaceVariant = Color(0xFF333433),
    onSurfaceVariant = OutlineVariant,
    outline = OutlineVariant,
    outlineVariant = OutlineColor,
    error = ErrorContainerColor,
    onError = OnErrorContainerColor
)

@Composable
fun SattvaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
