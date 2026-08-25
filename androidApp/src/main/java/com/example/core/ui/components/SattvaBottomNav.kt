package com.example.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import com.example.features.main.MainTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.MutedGold
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SurfaceIvory

@Composable
fun SattvaBottomNav(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = Color(0x33000000),
                    ambientColor = Color(0x1A000000)
                ),
            shape = RoundedCornerShape(32.dp),
            color = SurfaceIvory,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    label = "Home",
                    iconSelected = Icons.Filled.Home,
                    iconUnselected = Icons.Outlined.Home,
                    isSelected = selectedTab == MainTab.HOME,
                    onClick = { onTabSelected(MainTab.HOME) },
                    testTag = "nav_home"
                )

                BottomNavItem(
                    label = "Pujas",
                    iconSelected = Icons.Filled.Explore,
                    iconUnselected = Icons.Outlined.Explore,
                    isSelected = selectedTab == MainTab.EXPLORE,
                    onClick = { onTabSelected(MainTab.EXPLORE) },
                    testTag = "nav_explore"
                )

                BottomNavItem(
                    label = "Seva",
                    iconSelected = Icons.Filled.Favorite,
                    iconUnselected = Icons.Outlined.FavoriteBorder,
                    isSelected = selectedTab == MainTab.SEVA,
                    onClick = { onTabSelected(MainTab.SEVA) },
                    testTag = "nav_seva"
                )

                BottomNavItem(
                    label = "Rishi AI",
                    iconSelected = Icons.Filled.AutoAwesome,
                    iconUnselected = Icons.Outlined.AutoAwesome,
                    isSelected = selectedTab == MainTab.VEDIC_AI,
                    onClick = { onTabSelected(MainTab.VEDIC_AI) },
                    testTag = "nav_vedic_ai"
                )

                BottomNavItem(
                    label = "Profile",
                    iconSelected = Icons.Filled.Person,
                    iconUnselected = Icons.Outlined.Person,
                    isSelected = selectedTab == MainTab.PROFILE,
                    onClick = { onTabSelected(MainTab.PROFILE) },
                    testTag = "nav_profile"
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tintColor by animateColorAsState(
        targetValue = if (isSelected) RitualClay else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        label = "nav_color"
    )
    val backgroundPillColor by animateColorAsState(
        targetValue = if (isSelected) RitualClay.copy(alpha = 0.12f) else Color.Transparent,
        label = "nav_pill"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundPillColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) iconSelected else iconUnselected,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = tintColor,
                maxLines = 1
            )
        }
    }
}
