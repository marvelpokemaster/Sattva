package com.example.features.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.components.ImageWithPlaceholder
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceIvory
import com.example.data.model.AnimalResident

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search

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

    val needsSupport = filteredAnimals.filter { it.neededRupees > 0 || it.isUrgent }
    val nativeBreeds = filteredAnimals.filter { it.breed != "Desi" || it.story.contains("indigenous", ignoreCase = true) || it.story.contains("native", ignoreCase = true) }
    
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(20.dp)) {
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

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, breed, or story...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = DeepMoss) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceIvory,
                        unfocusedContainerColor = SurfaceIvory,
                        focusedBorderColor = DeepMoss,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Urgent", "Native Breeds", "Recovering").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        Card(
                            onClick = { selectedFilter = filter },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DeepMoss else SurfaceIvory
                            ),
                            shape = RoundedCornerShape(16.dp)
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
                Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                    Text("No animals found matching your search.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            if (needsSupport.isNotEmpty() && selectedFilter == "All") {
                item {
                    SectionHeader("Needs Urgent Support")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(needsSupport) { animal ->
                            EditorialAnimalCard(animal, onAnimalClick, onSupportClick, modifier = Modifier.width(280.dp))
                        }
                    }
                }
            }
            
            if (nativeBreeds.isNotEmpty() && selectedFilter == "All") {
                item {
                    SectionHeader("Native & Indigenous Breeds")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(nativeBreeds) { animal ->
                            EditorialAnimalCard(animal, onAnimalClick, onSupportClick, modifier = Modifier.width(280.dp))
                        }
                    }
                }
            }
            
            item {
                SectionHeader("All Residents (${filteredAnimals.size})")
            }
            
            items(filteredAnimals) { animal ->
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    EditorialAnimalCard(animal, onAnimalClick, onSupportClick, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = DeepMoss,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp)
    )
}

@Composable
fun EditorialAnimalCard(
    animal: AnimalResident,
    onClick: (String) -> Unit,
    onSupport: (AnimalResident) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick(animal.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                ImageWithPlaceholder(
                    model = animal.imageUrl,
                    contentDescription = animal.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                if (animal.isUrgent) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RitualClay)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Urgent Care", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = animal.name,
                        fontFamily = SerifFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = animal.healthStatus,
                        fontSize = 12.sp,
                        color = if (animal.healthStatus == "Healthy") DeepMoss else RitualClay,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = animal.story,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { onSupport(animal) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sponsor Care", color = Color.White)
                }
            }
        }
    }
}
