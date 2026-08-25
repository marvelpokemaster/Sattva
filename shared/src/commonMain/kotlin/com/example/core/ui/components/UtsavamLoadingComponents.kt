package com.example.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceContainerLow

// ---------------------------------------------------------------------------
// Shimmer brush — reusable across all skeleton composables
// ---------------------------------------------------------------------------

@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFEAE7E5),
            Color(0xFFF5F3F1),
            Color(0xFFEAE7E5),
        ),
        start = Offset(translateAnim - 300f, 0f),
        end = Offset(translateAnim, 0f)
    )
}

// ---------------------------------------------------------------------------
// ShimmerBox — basic animated placeholder rectangle
// ---------------------------------------------------------------------------

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(shimmerBrush())
    )
}

// ---------------------------------------------------------------------------
// CardSkeleton — mimics a content card with image + text lines
// ---------------------------------------------------------------------------

@Composable
fun CardSkeleton(
    modifier: Modifier = Modifier,
    imageHeight: Dp = 160.dp,
    cornerRadius: Dp = 20.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0xFFFDFCF8))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image placeholder
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                cornerRadius = 0.dp
            )
            // Text lines
            Column(modifier = Modifier.padding(16.dp)) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp),
                    cornerRadius = 6.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(13.dp),
                    cornerRadius = 6.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(13.dp),
                    cornerRadius = 6.dp
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// SectionSkeleton — section header shimmer + 1 full-width card skeleton
// ---------------------------------------------------------------------------

@Composable
fun SectionSkeleton(
    modifier: Modifier = Modifier,
    imageHeight: Dp = 160.dp
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section title placeholder
        ShimmerBox(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .width(160.dp)
                .height(20.dp),
            cornerRadius = 6.dp
        )
        Spacer(modifier = Modifier.height(12.dp))
        CardSkeleton(
            modifier = Modifier.padding(horizontal = 20.dp),
            imageHeight = imageHeight
        )
    }
}

// ---------------------------------------------------------------------------
// HorizontalCardSkeletonRow — for horizontally-scrollable animal/gaushala rows
// ---------------------------------------------------------------------------

@Composable
fun HorizontalCardSkeletonRow(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 260.dp,
    imageHeight: Dp = 150.dp
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(2) {
            Box(
                modifier = Modifier
                    .width(cardWidth)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFDFCF8))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight),
                        cornerRadius = 0.dp
                    )
                    Column(modifier = Modifier.padding(14.dp)) {
                        ShimmerBox(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp), cornerRadius = 5.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerBox(modifier = Modifier.fillMaxWidth(0.8f).height(12.dp), cornerRadius = 5.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerBox(modifier = Modifier.fillMaxWidth().height(6.dp), cornerRadius = 3.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        ShimmerBox(modifier = Modifier.fillMaxWidth().height(38.dp), cornerRadius = 16.dp)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// EmptyState — shown only after confirmed-empty (not while loading)
// ---------------------------------------------------------------------------

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    icon: ImageVector = Icons.Default.Spa,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RitualClay.copy(alpha = 0.4f),
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontFamily = SerifFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}

// ---------------------------------------------------------------------------
// ErrorView — shown when a network/data error has occurred
// ---------------------------------------------------------------------------

@Composable
fun ErrorView(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Something went wrong",
            fontFamily = SerifFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = RitualClay)
            ) {
                Text("Try Again", color = Color.White)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// ImageWithPlaceholder — AsyncImage with shimmer placeholder + fade-in
// ---------------------------------------------------------------------------

@Composable
fun ImageWithPlaceholder(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderCornerRadius: Dp = 0.dp
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                ShimmerBox(
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = placeholderCornerRadius
                )
            }
            is AsyncImagePainter.State.Error -> {
                // Subtle error placeholder — same Aaryam surface color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = RitualClay.copy(alpha = 0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
