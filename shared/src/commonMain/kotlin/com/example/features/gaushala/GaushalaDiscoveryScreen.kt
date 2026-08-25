package com.example.features.gaushala

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
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
import coil3.compose.AsyncImage
import com.example.data.model.Gaushala
import com.example.core.ui.components.CardSkeleton
import com.example.core.ui.components.EmptyState
import com.example.core.ui.components.ImageWithPlaceholder
import com.example.core.ui.components.TrustScoreBadge
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.MutedGold
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceContainerLow
import com.example.core.ui.theme.SurfaceIvory

@Composable
fun GaushalaDiscoveryScreen(
    gaushalas: List<Gaushala>,
    viewMode: String,
    isLoading: Boolean = false,
    onViewModeChange: (String) -> Unit,
    onGaushalaClick: (String) -> Unit,
    onSupportGaushala: (Gaushala) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Gaushala Discovery",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Support verified sanctuaries dedicated to the welfare and protection of cows across Bharat.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List / Map Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceContainerLow)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ViewModeButton(
                        text = "List",
                        icon = Icons.Default.List,
                        isSelected = viewMode == "List",
                        onClick = { onViewModeChange("List") }
                    )
                    ViewModeButton(
                        text = "Map View",
                        icon = Icons.Default.Map,
                        isSelected = viewMode == "Map",
                        onClick = { onViewModeChange("Map") }
                    )
                }
            }
        }

        // Map View Mock / Visualization Card if viewMode == "Map"
        if (viewMode == "Map") {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .height(280.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFE5ECE2))
                ) {
                    // Stylized Map Background with pins
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top info badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.9f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "📍 Showing ${gaushalas.size} verified sanctuaries across Bharat",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepMoss
                            )
                        }

                        // Map Pins
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            MapPin(name = "Vrindavan", count = "450 Cows", onClick = { onGaushalaClick("shri_krishna_gaushala") })
                            MapPin(name = "Pushkar", count = "210 Cows", onClick = { onGaushalaClick("gopashtami_sanctuary") })
                            MapPin(name = "Rishikesh", count = "850 Cows", onClick = { onGaushalaClick("nandini_welfare_trust") })
                        }

                        // Bottom notice
                        Text(
                            text = "Tap on any pin or select from the list below to provide Seva.",
                            fontSize = 11.sp,
                            color = DeepMoss,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        // Gaushala Items List
        // Gaushala Items List — skeleton while loading, empty state when confirmed empty
        if (gaushalas.isEmpty() && isLoading) {
            items(2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    CardSkeleton(imageHeight = 180.dp)
                }
            }
        } else if (gaushalas.isEmpty() && !isLoading) {
            item {
                EmptyState(
                    title = "No Gaushalas Found",
                    subtitle = "Verified sanctuaries will appear here once data is loaded.",
                    icon = androidx.compose.material.icons.Icons.Default.Pets
                )
            }
        } else {
            items(gaushalas, key = { it.id }) { gaushala ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    GaushalaCard(
                        gaushala = gaushala,
                        onClick = { onGaushalaClick(gaushala.id) },
                        onSupport = { onSupportGaushala(gaushala) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ViewModeButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) DeepMoss else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MapPin(
    name: String,
    count: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(RitualClay),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepMoss)
        Text(text = count, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun GaushalaCard(
    gaushala: Gaushala,
    onClick: () -> Unit,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("gaushala_card_${gaushala.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                ImageWithPlaceholder(
                    model = gaushala.imageUrl,
                    contentDescription = gaushala.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Trust Score Badge overlay
                TrustScoreBadge(
                    scorePercent = gaushala.trustScorePercent,
                    tier = gaushala.transparencyTier,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = gaushala.name,
                        fontFamily = SerifFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RitualClay,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${gaushala.location}, ${gaushala.state} • ${gaushala.animalsRescuedCount}+ Animals Rescued",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "“${gaushala.missionQuote}”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Sanctuary", fontSize = 13.sp, color = DeepMoss)
                    }

                    Button(
                        onClick = onSupport,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepMoss,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Support Seva", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
