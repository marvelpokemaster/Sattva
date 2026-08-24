package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.MutedGold
import com.example.ui.theme.RitualClay
import com.example.ui.theme.SerifFontFamily
import com.example.ui.theme.SurfaceColor

@Composable
fun SattvaTopBar(
    avatarUrl: String,
    onAvatarClick: () -> Unit,
    onAiClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    hasNotification: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(RitualClay.copy(alpha = 0.1f))
                .clickable { onAvatarClick() }
                .testTag("avatar_button"),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "User Profile Avatar",
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Center Sattva Brand
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sattva",
                fontFamily = SerifFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = RitualClay,
                letterSpacing = 1.sp
            )
            Text(
                text = "SPIRITUAL • CULTURAL • SEVA",
                style = MaterialTheme.typography.labelSmall,
                color = MutedGold,
                letterSpacing = 1.5.sp,
                fontSize = 8.5.sp
            )
        }

        // Actions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Vedic AI Sparkle
            IconButton(
                onClick = onAiClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MutedGold.copy(alpha = 0.15f))
                    .testTag("ai_top_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Rishi Vedic AI",
                    tint = RitualClay,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Notification Bell
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (hasNotification) {
                            Badge(
                                containerColor = RitualClay,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
