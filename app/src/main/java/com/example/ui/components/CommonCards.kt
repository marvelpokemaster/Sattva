package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyWisdom
import com.example.data.model.PanchangInfo
import com.example.ui.theme.DeepMoss
import com.example.ui.theme.MutedGold
import com.example.ui.theme.RitualClay
import com.example.ui.theme.SageLight
import com.example.ui.theme.SerifFontFamily
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceIvory
import com.example.ui.theme.TertiaryColor

@Composable
fun PanchangCard(
    panchang: PanchangInfo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = SurfaceContainerLow,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
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
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceIvory
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
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
                        .clip(RoundedCornerShape(12.dp))
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

            Spacer(modifier = Modifier.height(12.dp))

            if (wisdom.sanskritShloka.isNotBlank()) {
                Text(
                    text = wisdom.sanskritShloka,
                    fontFamily = SerifFontFamily,
                    fontSize = 14.sp,
                    color = DeepMoss,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
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

            Spacer(modifier = Modifier.height(8.dp))

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
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F5F1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                    Text(
                        text = "₹${totalContributedRupees / 1000.0}k",
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
            .clip(RoundedCornerShape(12.dp))
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
