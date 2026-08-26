package com.example.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.*
import com.example.data.model.DailyWisdom
import com.example.data.model.PanchangInfo

/**
 * Standard Card container with uniform elevation, radius, and background color.
 */
@Composable
fun StandardCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = SurfaceIvory,
    elevation: Dp = DesignTokens.Elevation.default,
    shape: RoundedCornerShape = RoundedCornerShape(DesignTokens.Radii.lg),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = if (onClick != null) modifier.clickable { onClick() } else modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

/**
 * Standard Image Card with 16:9 or custom aspect ratio image header, overlay badges, and formatted body.
 */
@Composable
fun ImageCard(
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier,
    imageHeight: Dp = DesignTokens.Dimensions.carouselImageHeight,
    badgeText: String? = null,
    badgeColor: Color = RitualClay,
    subtitle: String? = null,
    subtitleIcon: ImageVector? = null,
    metaText: String? = null,
    metaIcon: ImageVector? = null,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    footer: @Composable (ColumnScope.() -> Unit)? = null
) {
    StandardCard(modifier = modifier, onClick = onClick) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            ImageWithPlaceholder(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (!badgeText.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(DesignTokens.Spacing.md)
                        .clip(RoundedCornerShape(DesignTokens.Radii.sm))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(DesignTokens.Spacing.lg)) {
            Text(
                text = title,
                fontFamily = SerifFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.xs))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (subtitleIcon != null) {
                        Icon(
                            imageVector = subtitleIcon,
                            contentDescription = null,
                            tint = RitualClay,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!metaText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.xs))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (metaIcon != null) {
                        Icon(
                            imageVector = metaIcon,
                            contentDescription = null,
                            tint = MutedGold,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = metaText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            if (footer != null) {
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.md))
                footer()
            }
        }
    }
}

/**
 * Standard Compact List Card with horizontal layout (thumbnail on left, metadata + compact CTA on right).
 * Fits naturally in viewports so devotees can see multiple items at once.
 */
@Composable
fun CompactListCard(
    imageUrl: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    thumbnailSize: Dp = DesignTokens.Dimensions.compactThumbnailSize,
    badgeText: String? = null,
    badgeColor: Color = DeepMoss,
    metaText: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    StandardCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(DesignTokens.Radii.md),
        elevation = DesignTokens.Elevation.subtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(thumbnailSize)
                    .clip(RoundedCornerShape(DesignTokens.Radii.md))
            ) {
                ImageWithPlaceholder(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(DesignTokens.Spacing.md))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (!badgeText.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(DesignTokens.Radii.xs))
                            .background(badgeColor.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = title,
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!metaText.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = metaText,
                        fontSize = 11.sp,
                        color = RitualClay,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!actionText.isNullOrBlank() && onActionClick != null) {
                Spacer(modifier = Modifier.width(DesignTokens.Spacing.sm))
                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(DesignTokens.Radii.sm),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = actionText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Standard Section Header with title and optional action button.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    titleColor: Color = DeepMoss
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DesignTokens.Spacing.screenEdge, vertical = DesignTokens.Spacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = SerifFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = titleColor
        )

        if (!actionText.isNullOrBlank() && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    color = RitualClay,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

/**
 * Standard Stat Card for metrics display.
 */
@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    valueColor: Color = RitualClay
) {
    StandardCard(
        modifier = modifier,
        containerColor = SurfaceIvory,
        elevation = DesignTokens.Elevation.subtle,
        shape = RoundedCornerShape(DesignTokens.Radii.md)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = valueColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.xs))
            }
            Text(
                text = value,
                fontFamily = SerifFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = valueColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PanchangCard(
    panchang: PanchangInfo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(DesignTokens.Radii.lg)),
        color = SurfaceContainerLow,
        tonalElevation = DesignTokens.Elevation.subtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DesignTokens.Spacing.lg, vertical = DesignTokens.Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.md)
            ) {
                Box(
                    modifier = Modifier
                        .size(DesignTokens.Dimensions.iconBadgeSize)
                        .clip(CircleShape)
                        .background(MutedGold.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Panchang Solar Icon",
                        tint = MutedGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "${panchang.tithi} • ${panchang.nakshatra}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = panchang.auspiciousTiming,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WisdomQuoteCard(
    wisdom: DailyWisdom,
    onAskAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    StandardCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DesignTokens.Radii.xl),
        elevation = DesignTokens.Elevation.default
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.xl)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S VEDIC WISDOM",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(DesignTokens.Radii.md))
                        .background(RitualClay.copy(alpha = 0.1f))
                        .clickable { onAskAi() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = RitualClay,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Reflect with AI",
                        fontSize = 11.sp,
                        color = RitualClay,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.md))

            if (wisdom.sanskritShloka.isNotBlank()) {
                Text(
                    text = wisdom.sanskritShloka,
                    fontFamily = SerifFontFamily,
                    fontSize = 14.sp,
                    color = DeepMoss,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))
            }

            Text(
                text = "“${wisdom.quote}”",
                fontFamily = SerifFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "— ${wisdom.source}",
                style = MaterialTheme.typography.bodySmall,
                color = RitualClay,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))

            Text(
                text = wisdom.commentary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ImpactSummaryCard(
    sevasCompleted: Int,
    totalContributedRupees: Int,
    onViewProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    StandardCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onViewProfile,
        shape = RoundedCornerShape(DesignTokens.Radii.xl),
        elevation = DesignTokens.Elevation.subtle,
        containerColor = Color(0xFFF9F5F1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.xl),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "YOUR SPIRITUAL IMPACT",
                    style = MaterialTheme.typography.labelSmall,
                    color = RitualClay,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Living Sanatana Dharma through active Seva",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$sevasCompleted",
                        fontFamily = SerifFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = RitualClay
                    )
                    Text(
                        text = "Sevas Done",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val formatted = if (totalContributedRupees >= 1000) {
                        "₹${(totalContributedRupees / 100) / 10.0}k"
                    } else {
                        "₹$totalContributedRupees"
                    }
                    Text(
                        text = formatted,
                        fontFamily = SerifFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = DeepMoss
                    )
                    Text(
                        text = "Contributed",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TrustScoreBadge(
    scorePercent: Int,
    tier: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(DesignTokens.Radii.md))
            .background(DeepMoss.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Verified Sanctuary",
            tint = DeepMoss,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$scorePercent% Trust • $tier",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = DeepMoss
        )
    }
}

@Composable
fun SevaGoalBar(
    raisedAmount: Int,
    goalAmount: Int,
    label: String = "Funding Progress",
    modifier: Modifier = Modifier
) {
    val progress = if (goalAmount > 0) (raisedAmount.toFloat() / goalAmount).coerceIn(0f, 1f) else 0f
    val percent = (progress * 100).toInt()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$percent% Met (₹$raisedAmount of ₹$goalAmount)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = RitualClay
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = RitualClay,
            trackColor = RitualClay.copy(alpha = 0.15f),
            strokeCap = StrokeCap.Round
        )
    }
}
