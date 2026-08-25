package com.example.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    // Glassmorphism effect uses a translucent surface color and a subtle border
    val glassColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .clip(shape)
            .background(glassColor)
            .border(1.dp, borderColor, shape)
    ) {
        content()
    }
}
