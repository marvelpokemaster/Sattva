package com.example.features.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.components.*
import com.example.core.ui.theme.*
import com.example.data.model.AnimalResident

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDiscoveryScreen(
    animals: List<AnimalResident>,
    isLoading: Boolean,
    onBack: () -> Unit = {},
    onAnimalClick: (String) -> Unit,
    onSupportClick: (AnimalResident) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredAnimals = remember(animals, searchQuery, selectedFilter) {
        animals.filter { a ->
            val matchesQuery = searchQuery.isBlank() ||
                    a.name.contains(searchQuery, ignoreCase = true) ||
                    a.breed.contains(searchQuery, ignoreCase = true) ||
                    a.story.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Urgent" -> a.isUrgent || a.neededRupees > 0
                "Native Breeds" -> a.breed != "Desi" || a.story.contains("native", ignoreCase = true) || a.story.contains("indigenous", ignoreCase = true)
                "Recovering" -> a.healthStatus.contains("Recovering", ignoreCase = true)
                "Healthy" -> a.healthStatus.contains("Healthy", ignoreCase = true)
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    val urgentResidents = filteredAnimals.filter { it.isUrgent || it.neededRupees > 0 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = DesignTokens.Spacing.bottomNavClearance)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DesignTokens.Spacing.screenEdge, vertical = DesignTokens.Spacing.md)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepMoss)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Meet the Animals",
                        fontFamily = SerifFontFamily,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, breed, or story...", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = DeepMoss) },
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
                    listOf("All", "Urgent", "Native Breeds", "Recovering").forEach { filter ->
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
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepMoss)
                }
            }
        } else if (filteredAnimals.isEmpty()) {
            item {
                EmptyState(
                    title = "No Residents Found",
                    subtitle = "Try adjusting your search criteria.",
                    icon = Icons.Default.Search
                )
            }
        } else {
            if (filteredAnimals.size == 1) {
                val singleAnimal = filteredAnimals.first()
                item {
                    SectionHeader(title = "Featured Resident")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DesignTokens.Spacing.screenEdge)
                    ) {
                        FeaturedResidentCard(
                            animal = singleAnimal,
                            onClick = { onAnimalClick(singleAnimal.id) },
                            onSupport = { onSupportClick(singleAnimal) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                // Urgent Carousel if filtering "All" and multiple urgent animals exist
                if (urgentResidents.isNotEmpty() && selectedFilter == "All") {
                    item {
                        SectionHeader(title = "Needs Urgent Care")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = DesignTokens.Spacing.screenEdge),
                            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.md)
                        ) {
                            items(urgentResidents) { animal ->
                                FeaturedResidentCard(
                                    animal = animal,
                                    onClick = { onAnimalClick(animal.id) },
                                    onSupport = { onSupportClick(animal) },
                                    modifier = Modifier.width(DesignTokens.Dimensions.carouselCardWidth)
                                )
                            }
                        }
                    }
                }

                item {
                    SectionHeader(title = "All Cattle Residents (${filteredAnimals.size})")
                }

                items(filteredAnimals, key = { it.id }) { animal ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DesignTokens.Spacing.screenEdge, vertical = DesignTokens.Spacing.xs)
                    ) {
                        CompactAnimalCard(
                            animal = animal,
                            onClick = { onAnimalClick(animal.id) },
                            onSupport = { onSupportClick(animal) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Featured Resident Card for urgent care carousel.
 */
@Composable
fun FeaturedResidentCard(
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
                .height(DesignTokens.Dimensions.carouselImageHeight)
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
                        .padding(8.dp)
                        .clip(RoundedCornerShape(DesignTokens.Radii.xs))
                        .background(RitualClay)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text("Urgent Care", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(modifier = Modifier.padding(DesignTokens.Spacing.md)) {
            Text(
                text = animal.name,
                fontFamily = SerifFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${animal.breed} • ${animal.healthStatus}",
                fontSize = 12.sp,
                color = if (animal.healthStatus == "Healthy") DeepMoss else RitualClay,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))

            Button(
                onClick = onSupport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(DesignTokens.Radii.sm),
                colors = ButtonDefaults.buttonColors(containerColor = DeepMoss)
            ) {
                Text("Sponsor", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/**
 * Compact Animal Card: Horizontal list card with 90x90 thumbnail on left, metadata + Sponsor button on right.
 */
@Composable
fun CompactAnimalCard(
    animal: AnimalResident,
    onClick: () -> Unit,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompactListCard(
        imageUrl = animal.imageUrl,
        title = animal.name,
        subtitle = "${animal.breed} • ${animal.ageStr}",
        badgeText = if (animal.isUrgent) "Urgent" else animal.healthStatus,
        badgeColor = if (animal.healthStatus == "Healthy") DeepMoss else RitualClay,
        metaText = "Support from ₹200",
        actionText = "Sponsor",
        onActionClick = onSupport,
        onClick = onClick,
        thumbnailSize = 90.dp,
        modifier = modifier
    )
}

/**
 * Editorial Animal Card alias for backward compatibility.
 */
@Composable
fun EditorialAnimalCard(
    animal: AnimalResident,
    onClick: (String) -> Unit,
    onSupport: (AnimalResident) -> Unit,
    modifier: Modifier = Modifier
) {
    FeaturedResidentCard(
        animal = animal,
        onClick = { onClick(animal.id) },
        onSupport = { onSupport(animal) },
        modifier = modifier
    )
}
