package com.example.features.home
import com.example.features.puja.PujaItemCard
import com.example.features.gaushala.GaushalaCard

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Gaushala
import com.example.data.model.Puja
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.MutedGold
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceContainerLow
import com.example.core.ui.theme.SurfaceIvory

data class ExploreCategory(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun ExploreScreen(
    pujas: List<Puja>,
    gaushalas: List<Gaushala>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onPujaClick: (String) -> Unit,
    onGaushalaClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        ExploreCategory("Vedic Pujas", Icons.Default.LocalFireDepartment, RitualClay),
        ExploreCategory("Gaushalas", Icons.Default.Pets, DeepMoss),
        ExploreCategory("Ancient Temples", Icons.Default.AccountBalance, MutedGold),
        ExploreCategory("Sacred Festivals", Icons.Default.Celebration, Color(0xFFD97706)),
        ExploreCategory("Spiritual Wisdom", Icons.Default.Spa, DeepMoss)
    )

    val filteredPujas = if (searchQuery.isBlank()) pujas else {
        pujas.filter { it.title.contains(searchQuery, ignoreCase = true) || it.templeName.contains(searchQuery, ignoreCase = true) }
    }

    val filteredGaushalas = if (searchQuery.isBlank()) gaushalas else {
        gaushalas.filter { it.name.contains(searchQuery, ignoreCase = true) || it.location.contains(searchQuery, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        // Header & Search
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Explore Sattva",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Discover sacred tirthas, rituals, and verified sanctuaries across India.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_input"),
                    placeholder = {
                        Text(
                            text = "Search Pujas, Gaushalas, Temples, Festivals...",
                            fontSize = 13.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = RitualClay
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLow,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = RitualClay,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Quick Category Icons
                Text(
                    text = "Categories",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { cat ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceIvory)
                                .clickable { onSearchChange(cat.title.split(" ").first()) }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(cat.color.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.title,
                                    tint = cat.color,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = cat.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Recommended Pujas
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Sacred Pujas & Anushthans",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        items(filteredPujas, key = { "puja_${it.id}" }) { puja ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                PujaItemCard(
                    puja = puja,
                    onClick = { onPujaClick(puja.id) }
                )
            }
        }

        // Recommended Gaushalas
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Verified Sanctuaries (Gaushalas)",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        items(filteredGaushalas, key = { "gaushala_${it.id}" }) { gaushala ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                GaushalaCard(
                    gaushala = gaushala,
                    onClick = { onGaushalaClick(gaushala.id) },
                    onSupport = { onGaushalaClick(gaushala.id) }
                )
            }
        }
    }
}
