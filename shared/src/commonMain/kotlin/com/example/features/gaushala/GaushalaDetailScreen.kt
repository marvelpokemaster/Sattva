package com.example.features.gaushala

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
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
import com.example.data.model.Gaushala
import com.example.data.model.WelfareUpdate
import com.example.data.model.AnimalResident
import com.example.features.welfare.WelfareTimeline
import com.example.features.animal.EditorialAnimalCard
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@Composable
fun GaushalaDetailScreen(
    gaushalaId: String,
    gaushala: Gaushala?,
    animals: List<AnimalResident>,
    welfareUpdates: List<WelfareUpdate>,
    onBack: () -> Unit,
    onSupportClick: () -> Unit,
    onAnimalClick: (String) -> Unit,
    onAnimalSupportClick: (AnimalResident) -> Unit,
    onViewAllAnimals: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (gaushala == null) {
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
            // Immersive Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                ImageWithPlaceholder(
                    model = gaushala.imageUrl,
                    contentDescription = gaushala.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent))))
                Box(modifier = Modifier.fillMaxWidth().height(150.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
                
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.statusBarsPadding().padding(start = 8.dp).size(48.dp).align(Alignment.TopStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(gaushala.transparencyTier, color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = gaushala.name,
                        fontFamily = SerifFontFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${gaushala.location}, ${gaushala.state}", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    }
                }
            }
            
            // Content
            Column {
                // Trust & Impact Strip
                Row(
                    modifier = Modifier.fillMaxWidth().background(SurfaceIvory).padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ImpactStat("${gaushala.animalsRescuedCount}+", "Rescued")
                    ImpactStat("${gaushala.trustScorePercent}%", "Trust Score")
                    ImpactStat("${gaushala.updatesCount}", "Weekly Updates")
                }
                
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Our Mission", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = gaushala.missionQuote,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    // Care Allocation
                    Text("Fund Allocation", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    CareAllocationBar(gaushala.fodderPercent, gaushala.medicalPercent, gaushala.shelterPercent)

                    Spacer(modifier = Modifier.height(28.dp))

                    // Virtual Visit / Live Stream
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF4CAF50)))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Virtual Darshan & Live Feeds", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepMoss)
                                }
                                Text("Live Stream Ready", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = RitualClay)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Sanctuary feeding area & grazing pasture cameras stream daily during morning Gau Pooja (7:00 AM - 8:30 AM IST) and evening Sandhya (5:30 PM - 7:00 PM IST).",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
                
                // Meet the Animals Carousel
                if (animals.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Meet the Animals", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onViewAllAnimals) {
                            Text("See all (${animals.size})", color = RitualClay, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(animals) { animal ->
                            EditorialAnimalCard(animal, onAnimalClick, onAnimalSupportClick, modifier = Modifier.width(280.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
                
                // Welfare Timeline
                Text("Care & Welfare Timeline", style = MaterialTheme.typography.titleMedium, color = DeepMoss, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 20.dp, bottom = 16.dp))
                WelfareTimeline(welfareUpdates)
            }
        }
        
        // Bottom Support Bar
        Surface(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
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
                Text("Help sustain this sanctuary", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Button(
                    onClick = onSupportClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Support Seva", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ImpactStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontFamily = SerifFontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DeepMoss)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun CareAllocationBar(fodder: Int, medical: Int, shelter: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp))) {
            Box(modifier = Modifier.weight(fodder.toFloat()).fillMaxHeight().background(DeepMoss))
            Box(modifier = Modifier.weight(medical.toFloat()).fillMaxHeight().background(RitualClay))
            Box(modifier = Modifier.weight(shelter.toFloat()).fillMaxHeight().background(Color(0xFFE0E0E0)))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            LegendItem("Fodder $fodder%", DeepMoss)
            LegendItem("Medical $medical%", RitualClay)
            LegendItem("Shelter $shelter%", Color(0xFFE0E0E0))
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
