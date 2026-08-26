package com.example.features.gaushala

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.components.*
import com.example.core.ui.theme.*
import com.example.data.model.AnimalResident
import com.example.data.model.Gaushala
import com.example.data.model.WelfareUpdate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GaushalaDiscoveryScreen(
    gaushalas: List<Gaushala>,
    isLoading: Boolean,
    animals: List<AnimalResident> = emptyList(),
    welfareUpdates: List<WelfareUpdate> = emptyList(),
    onGaushalaClick: (String) -> Unit,
    onSupportGaushala: (Gaushala) -> Unit,
    onAnimalClick: (String) -> Unit = {},
    onSupportAnimal: (AnimalResident) -> Unit = {},
    onExploreAnimalsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredGaushalas = remember(gaushalas, searchQuery, selectedFilter) {
        gaushalas.filter { g ->
            val matchesQuery = searchQuery.isBlank() ||
                    g.name.contains(searchQuery, ignoreCase = true) ||
                    g.location.contains(searchQuery, ignoreCase = true) ||
                    g.state.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Kerala" -> g.state.contains("Kerala", ignoreCase = true) || g.location.contains("Kerala", ignoreCase = true)
                "Top Transparent" -> g.transparencyTier.contains("Gold", ignoreCase = true) || g.transparencyTier.contains("Platinum", ignoreCase = true)
                "Urgent Care" -> g.trustScorePercent >= 90
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    val featuredGaushala = filteredGaushalas.firstOrNull()
    val otherGaushalas = if (filteredGaushalas.size > 1) filteredGaushalas.drop(1) else emptyList()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = DesignTokens.Spacing.bottomNavClearance)
    ) {
        // Header & Search
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DesignTokens.Spacing.screenEdge, vertical = DesignTokens.Spacing.md)
            ) {
                Text(
                    text = "Sanctuaries",
                    fontFamily = SerifFontFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Preserving sacred indigenous cattle and transparent Gau-Seva across Bharat.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(DesignTokens.Spacing.lg))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, city, or state...", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = DeepMoss)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(DesignTokens.Radii.md),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceIvory,
                        unfocusedContainerColor = SurfaceIvory,
                        focusedBorderColor = DeepMoss,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(DesignTokens.Spacing.md))

                // Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.sm)
                ) {
                    listOf("All", "Top Transparent", "Kerala", "Urgent Care").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        Card(
                            onClick = { selectedFilter = filter },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DeepMoss else SurfaceIvory
                            ),
                            shape = RoundedCornerShape(DesignTokens.Radii.pill)
                        ) {
                            Text(
                                text = filter,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(DesignTokens.Spacing.lg))

                // Meet All Cattle Banner
                Card(
                    onClick = onExploreAnimalsClick,
                    shape = RoundedCornerShape(DesignTokens.Radii.lg),
                    colors = CardDefaults.cardColors(containerColor = DeepMoss),
                    modifier = Modifier.fillMaxWidth()
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(DesignTokens.Radii.sm))
                                    .background(Color.White.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🐮", fontSize = 18.sp)
                            }
                            Column {
                                Text(
                                    text = "Meet All Cattle Residents",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Indigenous breeds, urgent care & passports",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                        Text(
                            text = "Explore →",
                            color = MutedGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DeepMoss)
                }
            }
        } else if (filteredGaushalas.isEmpty()) {
            item {
                EmptyState(
                    title = "No Sanctuaries Found",
                    subtitle = "Try adjusting your search query or filter.",
                    icon = Icons.Default.LocationOn
                )
            }
        } else {
            // Featured Hero Sanctuary (Rendered ONCE)
            if (featuredGaushala != null) {
                item {
                    SectionHeader(
                        title = "Featured Sanctuary",
                        actionText = if (otherGaushalas.isNotEmpty()) "${filteredGaushalas.size} Total" else null
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DesignTokens.Spacing.screenEdge)
                    ) {
                        FeaturedGaushalaCard(
                            gaushala = featuredGaushala,
                            onClick = { onGaushalaClick(featuredGaushala.id) },
                            onSupport = { onSupportGaushala(featuredGaushala) }
                        )
                    }
                }
            }

            // BRANCHING: If ONLY 1 Gaushala exists, render rich editorial supporting sections instead of duplicate database cards
            if (otherGaushalas.isEmpty()) {
                // Section 1: Meet the Sanctuary Residents
                if (animals.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Sanctuary Residents",
                            actionText = "See All (${animals.size})",
                            onActionClick = onExploreAnimalsClick
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = DesignTokens.Spacing.screenEdge),
                            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.md)
                        ) {
                            items(animals) { animal ->
                                CompactResidentCard(
                                    animal = animal,
                                    onClick = { onAnimalClick(animal.id) },
                                    onSupport = { onSupportAnimal(animal) },
                                    modifier = Modifier.width(220.dp)
                                )
                            }
                        }
                    }
                }

                // Section 2: Trust & Transparency Guarantee
                item {
                    Spacer(modifier = Modifier.height(DesignTokens.Spacing.lg))
                    SectionHeader(title = "Trust & Transparency Guarantee")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DesignTokens.Spacing.screenEdge)
                    ) {
                        SanctuaryTrustBanner()
                    }
                }

                // Section 3: Recent Welfare Updates
                if (welfareUpdates.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(DesignTokens.Spacing.lg))
                        SectionHeader(title = "Recent Welfare Logs")
                    }
                    items(welfareUpdates.take(2)) { update ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DesignTokens.Spacing.screenEdge, vertical = DesignTokens.Spacing.xs)
                        ) {
                            CompactWelfareLogCard(update = update)
                        }
                    }
                }
            } else {
                // MULTI-GAUSHALA MODE: Show remaining sanctuaries (excluding featured hero)
                item {
                    Spacer(modifier = Modifier.height(DesignTokens.Spacing.lg))
                    SectionHeader(title = "Other Sanctuaries (${otherGaushalas.size})")
                }

                items(otherGaushalas, key = { it.id }) { gaushala ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DesignTokens.Spacing.screenEdge, vertical = DesignTokens.Spacing.sm)
                    ) {
                        CompactGaushalaCard(
                            gaushala = gaushala,
                            onClick = { onGaushalaClick(gaushala.id) },
                            onSupport = { onSupportGaushala(gaushala) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Featured Sanctuary Hero Card: Restrained 160dp image, verified badges, mission quote, and two clear actions.
 */
@Composable
fun FeaturedGaushalaCard(
    gaushala: Gaushala,
    onClick: () -> Unit,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    StandardCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(DesignTokens.Radii.lg),
        elevation = DesignTokens.Elevation.default
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignTokens.Dimensions.featuredImageHeight)
            ) {
                ImageWithPlaceholder(
                    model = gaushala.imageUrl,
                    contentDescription = gaushala.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DesignTokens.Spacing.md),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(DesignTokens.Radii.sm))
                            .background(Color.White.copy(alpha = 0.95f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = DeepMoss,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${gaushala.trustScorePercent}% Trust • ${gaushala.transparencyTier}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepMoss
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(DesignTokens.Radii.sm))
                            .background(DeepMoss.copy(alpha = 0.85f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${gaushala.animalsRescuedCount}+ Cattle",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(DesignTokens.Spacing.lg)) {
                Text(
                    text = gaushala.name,
                    fontFamily = SerifFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RitualClay,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${gaushala.location}, ${gaushala.state}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (gaushala.missionQuote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))
                    Text(
                        text = "“${gaushala.missionQuote}”",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.height(DesignTokens.Spacing.lg))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.md)
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        shape = RoundedCornerShape(DesignTokens.Radii.md),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepMoss),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Text("View Details", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onSupport,
                        shape = RoundedCornerShape(DesignTokens.Radii.md),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Text("Support Seva", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Compact Sanctuary Card: Horizontal list card for multiple sanctuaries in list view.
 */
@Composable
fun CompactGaushalaCard(
    gaushala: Gaushala,
    onClick: () -> Unit,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompactListCard(
        imageUrl = gaushala.imageUrl,
        title = gaushala.name,
        subtitle = "${gaushala.location}, ${gaushala.state}",
        badgeText = "${gaushala.transparencyTier} • ${gaushala.animalsRescuedCount}+ Cattle",
        badgeColor = DeepMoss,
        metaText = "${gaushala.trustScorePercent}% Trust Score",
        actionText = "Seva",
        onActionClick = onSupport,
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * Compact Resident Card for the "Meet the Residents" carousel.
 */
@Composable
private fun CompactResidentCard(
    animal: AnimalResident,
    onClick: () -> Unit,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    StandardCard(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(DesignTokens.Radii.md),
        elevation = DesignTokens.Elevation.subtle
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            ImageWithPlaceholder(
                model = animal.imageUrl,
                contentDescription = animal.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (animal.isUrgent) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(DesignTokens.Radii.xs))
                        .background(RitualClay)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text("Urgent", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(modifier = Modifier.padding(DesignTokens.Spacing.sm)) {
            Text(
                text = animal.name,
                fontFamily = SerifFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${animal.breed} • ${animal.healthStatus}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))

            Button(
                onClick = onSupport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                shape = RoundedCornerShape(DesignTokens.Radii.sm),
                colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Sponsor", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

/**
 * Trust & Transparency information banner for single sanctuary exploration.
 */
@Composable
private fun SanctuaryTrustBanner(
    modifier: Modifier = Modifier
) {
    StandardCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DesignTokens.Radii.lg),
        containerColor = SurfaceIvory,
        elevation = DesignTokens.Elevation.subtle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.lg)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = DeepMoss,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "100% Verified Sanctuary Standard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DeepMoss
                )
            }

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))

            Text(
                text = "Every rupee directly supports green fodder, ayurvedic vet care, and shelter maintenance. Certified with continuous audits and 80G tax deductions.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )
        }
    }
}

/**
 * Compact Welfare Log Card for recent timeline items.
 */
@Composable
private fun CompactWelfareLogCard(
    update: WelfareUpdate,
    modifier: Modifier = Modifier
) {
    StandardCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DesignTokens.Radii.md),
        elevation = DesignTokens.Elevation.subtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(DesignTokens.Radii.sm))
                    .background(RitualClay.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = RitualClay,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = update.eventType.ifBlank { "Welfare Log" },
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = update.dateStr,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = update.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
