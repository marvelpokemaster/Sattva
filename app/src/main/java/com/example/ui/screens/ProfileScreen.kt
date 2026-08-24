package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.FamilyMember
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile
import com.example.ui.theme.DeepMoss
import com.example.ui.theme.MutedGold
import com.example.ui.theme.RitualClay
import com.example.ui.theme.SerifFontFamily
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceIvory

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    allPujas: List<Puja>,
    contributions: List<SevaContribution>,
    familyMembers: List<FamilyMember>,
    onUpdateSpiritualIdentity: (String, String, String) -> Unit,
    onAddFamilyMember: (FamilyMember) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIdx by remember { mutableIntStateOf(0) }
    val tabs = listOf("Spiritual", "Seva", "Personal")

    var showEditIdentityDialog by remember { mutableStateOf(false) }
    var showAddFamilyDialog by remember { mutableStateOf(false) }

    var editGotra by remember(userProfile) { mutableStateOf(userProfile?.gotra ?: "Kashyapa") }
    var editNakshatra by remember(userProfile) { mutableStateOf(userProfile?.nakshatra ?: "Rohini") }
    var editRashi by remember(userProfile) { mutableStateOf(userProfile?.rashi ?: "Vrishabha (Taurus)") }

    var newMemberName by remember { mutableStateOf("") }
    var newMemberRelation by remember { mutableStateOf("Spouse") }
    var newMemberNakshatra by remember { mutableStateOf("Mrigashira") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        // Profile Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with gold border
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(MutedGold, RitualClay, MutedGold)
                            )
                        )
                        .padding(3.dp)
                ) {
                    AsyncImage(
                        model = userProfile?.avatarUrl ?: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userProfile?.name ?: "Arjun Desai",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

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
                        text = userProfile?.city ?: "Mumbai, India",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Counters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceIvory)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderStat(label = "Pujas", value = "${userProfile?.pujasCount ?: 12}")
                    HeaderStat(label = "Animals", value = "${userProfile?.animalsSupportedCount ?: 5}")
                    HeaderStat(label = "Total Seva", value = "₹${(userProfile?.totalContributedRupees ?: 5200) / 1000.0}k")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs Bar
                TabRow(
                    selectedTabIndex = selectedTabIdx,
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIdx]),
                            color = RitualClay
                        )
                    }
                ) {
                    tabs.forEachIndexed { idx, title ->
                        Tab(
                            selected = selectedTabIdx == idx,
                            onClick = { selectedTabIdx = idx },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTabIdx == idx) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTabIdx == idx) RitualClay else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }
        }

        // Tab Content
        when (selectedTabIdx) {
            0 -> {
                // SPIRITUAL TAB
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Your Booked & Registered Pujas",
                            fontFamily = SerifFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }
                }

                items(allPujas.take(3), key = { "booked_${it.id}" }) { puja ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        BookedPujaCard(puja = puja)
                    }
                }

                // Family Members Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Family Members for Sankalpa",
                                fontFamily = SerifFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            IconButton(
                                onClick = { showAddFamilyDialog = true },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(RitualClay.copy(alpha = 0.12f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Member",
                                    tint = RitualClay,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        familyMembers.forEach { member ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceIvory)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = member.name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "${member.relation} • Nakshatra: ${member.nakshatra}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Text(
                                    text = member.gotra,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepMoss
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }

            1 -> {
                // SEVA TAB
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Seva Achievements & Badges",
                            fontFamily = SerifFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            BadgeCard(title = "Protector", subtitle = "Tier 3 Seva", icon = Icons.Default.Shield, color = DeepMoss, modifier = Modifier.weight(1f))
                            BadgeCard(title = "Provider", subtitle = "10+ Cow Feed", icon = Icons.Default.Favorite, color = RitualClay, modifier = Modifier.weight(1f))
                            BadgeCard(title = "Devotee", subtitle = "12 Pujas", icon = Icons.Default.Spa, color = MutedGold, modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Recent Seva Contributions",
                            fontFamily = SerifFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }
                }

                items(contributions, key = { it.id }) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceIvory)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = item.targetName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "${item.category} • ${item.dateStr}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Text(
                                    text = "₹${item.amountRupees}",
                                    fontFamily = SerifFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DeepMoss
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // PERSONAL & SPIRITUAL IDENTITY TAB
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Spiritual Identity (Kundali / Gotra)",
                                fontFamily = SerifFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            IconButton(onClick = { showEditIdentityDialog = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = RitualClay)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        IdentityRow(label = "Gotra", value = userProfile?.gotra ?: "Kashyapa")
                        Spacer(modifier = Modifier.height(8.dp))
                        IdentityRow(label = "Birth Nakshatra", value = userProfile?.nakshatra ?: "Rohini")
                        Spacer(modifier = Modifier.height(8.dp))
                        IdentityRow(label = "Rashi / Moon Sign", value = userProfile?.rashi ?: "Vrishabha (Taurus)")

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Trust & Authentication",
                            fontFamily = SerifFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceIvory)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = DeepMoss,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "Verified Sanatana Dharma Devotee",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Direct receipts & 80G tax exemptions verified.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Identity Dialog
    if (showEditIdentityDialog) {
        AlertDialog(
            onDismissRequest = { showEditIdentityDialog = false },
            title = { Text("Update Spiritual Identity", fontFamily = SerifFontFamily, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editGotra,
                        onValueChange = { editGotra = it },
                        label = { Text("Gotra") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editNakshatra,
                        onValueChange = { editNakshatra = it },
                        label = { Text("Nakshatra") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editRashi,
                        onValueChange = { editRashi = it },
                        label = { Text("Rashi") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateSpiritualIdentity(editGotra, editNakshatra, editRashi)
                        showEditIdentityDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RitualClay)
                ) {
                    Text("Save Identity")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditIdentityDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Family Member Dialog
    if (showAddFamilyDialog) {
        AlertDialog(
            onDismissRequest = { showAddFamilyDialog = false },
            title = { Text("Add Family Member for Sankalpa", fontFamily = SerifFontFamily, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newMemberName,
                        onValueChange = { newMemberName = it },
                        label = { Text("Full Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newMemberRelation,
                        onValueChange = { newMemberRelation = it },
                        label = { Text("Relation (Spouse, Child, Parent)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newMemberNakshatra,
                        onValueChange = { newMemberNakshatra = it },
                        label = { Text("Birth Nakshatra") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newMemberName.isNotBlank()) {
                            onAddFamilyMember(
                                FamilyMember(
                                    name = newMemberName,
                                    relation = newMemberRelation,
                                    nakshatra = newMemberNakshatra,
                                    gotra = userProfile?.gotra ?: "Kashyapa"
                                )
                            )
                            newMemberName = ""
                            showAddFamilyDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RitualClay)
                ) {
                    Text("Add Member")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFamilyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun HeaderStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = SerifFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = RitualClay
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BookedPujaCard(puja: Puja) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = puja.title,
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DeepMoss.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (puja.isBooked) "CONFIRMED" else "COMPLETED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepMoss
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${puja.templeName} • ${puja.dateTimeStr}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = RitualClay, modifier = Modifier.size(16.dp))
                    Text(text = "Video Sankalp Recorded", fontSize = 12.sp, color = RitualClay, fontWeight = FontWeight.Medium)
                }

                Text(
                    text = "Prasad Dispatched",
                    fontSize = 11.sp,
                    color = DeepMoss,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun BadgeCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceIvory)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }
}

@Composable
private fun IdentityRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceIvory)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RitualClay)
    }
}
