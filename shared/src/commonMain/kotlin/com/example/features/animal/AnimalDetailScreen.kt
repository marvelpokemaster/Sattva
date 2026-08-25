package com.example.features.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.components.ImageWithPlaceholder
import com.example.core.ui.components.GlassSurface
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceIvory
import com.example.data.model.AnimalResident

@Composable
fun AnimalDetailScreen(
    animalId: String,
    animal: AnimalResident?,
    onBack: () -> Unit,
    onAdoptClick: () -> Unit,
    onDonateClick: (Int) -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (animal == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = DeepMoss)
        }
        return
    }

    val scrollState = rememberScrollState()
    
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            // Hero Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                ImageWithPlaceholder(
                    model = animal.imageUrl,
                    contentDescription = animal.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Top gradient for back button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)))
                )
                
                // Bottom gradient for text readability
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .align(Alignment.BottomCenter)
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                )
                
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 8.dp)
                        .size(48.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                GlassSurface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(end = 16.dp, top = 8.dp)
                        .align(Alignment.TopEnd),
                    shape = CircleShape
                ) {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (animal.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (animal.isFavorite) RitualClay else Color.White
                        )
                    }
                }
                
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = animal.name,
                        fontFamily = SerifFontFamily,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = animal.ageStr,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 15.sp
                        )
                        Text(text = " • ", color = Color.White.copy(alpha = 0.6f))
                        Text(
                            text = animal.healthStatus,
                            color = if (animal.healthStatus == "Healthy") Color(0xFF81C784) else Color(0xFFE57373),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            
            // Passport Body
            Column(modifier = Modifier.padding(20.dp)) {
                // Story
                Text("Rescue Story", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = animal.story,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Breed Conservation snippet if native
                if (animal.story.contains("native", ignoreCase = true) || animal.story.contains("indigenous", ignoreCase = true) || animal.story.contains("Vechur", ignoreCase = true) || animal.story.contains("Kasaragod", ignoreCase = true)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Indigenous Breed Conservation", fontWeight = FontWeight.Bold, color = DeepMoss)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Supporting ${animal.name} helps preserve India's vital native breeds which are naturally adapted to the local climate and highly resilient.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Care at a Glance
                Text("Care at a Glance", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CareMetricCard(title = "Health", subtitle = animal.healthDescription, modifier = Modifier.weight(1f))
                    CareMetricCard(title = "Diet", subtitle = "Custom nutrition plan", modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Current Needs
                Text("Support ${animal.name}", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                MicroDonationOption(amount = "₹200", description = "Provides green fodder for 1 week", onClick = { onDonateClick(200) })
                Spacer(modifier = Modifier.height(8.dp))
                MicroDonationOption(amount = "₹500", description = "Covers essential deworming & medicines", onClick = { onDonateClick(500) })
                Spacer(modifier = Modifier.height(8.dp))
                MicroDonationOption(amount = "₹1,000", description = "Funds a complete veterinary checkup", onClick = { onDonateClick(1000) })
            }
        }
        
        // Bottom Adoption Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Adoption Sponsorship", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Starting at ₹2,000/mo", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    onClick = onAdoptClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Adopt ${animal.name}")
                }
            }
        }
    }
}

@Composable
fun CareMetricCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
        }
    }
}

@Composable
fun MicroDonationOption(amount: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(amount, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepMoss)
                Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = onClick, shape = RoundedCornerShape(12.dp)) {
                Text("Give", color = DeepMoss)
            }
        }
    }
}
