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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    firebaseUser: FirebaseUser? = null,
    allPujas: List<Puja>,
    contributions: List<SevaContribution>,
    familyMembers: List<FamilyMember>,
    onUpdateSpiritualIdentity: (String, String, String) -> Unit,
    onAddFamilyMember: (FamilyMember) -> Unit,
    onSignInWithEmail: (String, String, (Boolean, String?) -> Unit) -> Unit = { _, _, _ -> },
    onSignUpWithEmail: (String, String, String, (Boolean, String?) -> Unit) -> Unit = { _, _, _, _ -> },
    onSignInAsDevotee: (String, (Boolean, String?) -> Unit) -> Unit = { _, _ -> },
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTabIdx by remember { mutableIntStateOf(0) }
    val tabs = listOf("Spiritual", "Seva", "Personal")

    var showEditIdentityDialog by remember { mutableStateOf(false) }
    var showAddFamilyDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    var editGotra by remember(userProfile) { mutableStateOf(userProfile?.gotra ?: "Kashyapa") }
    var editNakshatra by remember(userProfile) { mutableStateOf(userProfile?.nakshatra ?: "Rohini") }
    var editRashi by remember(userProfile) { mutableStateOf(userProfile?.rashi ?: "Vrishabha (Taurus)") }

    var newMemberName by remember { mutableStateOf("") }
    var newMemberRelation by remember { mutableStateOf("Spouse") }
    var newMemberNakshatra by remember { mutableStateOf("Mrigashira") }

    // Auth dialog state
    var authIsSignUp by remember { mutableStateOf(false) }
    var authEmail by remember { mutableStateOf("") }
    var authPassword by remember { mutableStateOf("") }
    var authDisplayName by remember { mutableStateOf("") }
    var authError by remember { mutableStateOf<String?>(null) }
    var authLoading by remember { mutableStateOf(false) }

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
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RitualClay,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = userProfile?.location ?: "Mumbai, India",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Devotee Level & Badges Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceIvory)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MutedGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = RitualClay,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Gau Seva Ratna • Level 3",
                                fontFamily = SerifFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Consecrated Patron Devotee",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DeepMoss.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (firebaseUser != null) "FIREBASE SYNCED" else "OFFLINE READY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepMoss
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceIvory)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderStat(label = "Pujas Done", value = "${userProfile?.pujasCount ?: 12}")
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )
                    HeaderStat(label = "Gau Seva", value = "${userProfile?.animalsCount ?: 5}")
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )
                    HeaderStat(label = "Total Seva", value = "₹${userProfile?.totalContributedRupees ?: 5200}")
                }
            }
        }

        // Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTabIdx,
                containerColor = Color.Transparent,
                contentColor = RitualClay,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIdx]),
                        color = RitualClay
                    )
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIdx == index,
                        onClick = { selectedTabIdx = index },
                        text = {
                            Text(
                                text = title,
                                fontFamily = SerifFontFamily,
                                fontWeight = if (selectedTabIdx == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp,
                                color = if (selectedTabIdx == index) RitualClay else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tab Content
        when (selectedTabIdx) {
            0 -> {
                // SPIRITUAL TAB
                val booked = allPujas.filter { it.isBooked }
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Consecrated Rituals & Sankalpas",
                            fontFamily = SerifFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                if (booked.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceIvory)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = RitualClay,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No Consecrated Pujas Yet",
                                    fontFamily = SerifFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Book sacred Vedic pujas to receive customized sankalpas and consecration prasad.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                } else {
                    items(booked) { puja ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                            BookedPujaCard(puja = puja)
                        }
                    }
                }

                // Family Members Section
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Family Members for Sankalpa",
                                fontFamily = SerifFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            IconButton(onClick = { showAddFamilyDialog = true }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Member", tint = RitualClay)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                items(familyMembers) { member ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(RitualClay.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.People, contentDescription = null, tint = RitualClay, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        text = "${member.relation} • ${member.nakshatra}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(text = member.gotra, style = MaterialTheme.typography.bodySmall, color = RitualClay, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            1 -> {
                // SEVA TAB
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Seva & Go-Dan History",
                            fontFamily = SerifFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                if (contributions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceIvory)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = RitualClay, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No Seva Contributions Recorded",
                                    fontFamily = SerifFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Support Gaushalas and rescued cows to see direct seva receipts and 80G tax summaries.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(contributions) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp),
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(DeepMoss.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = DeepMoss,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(
                                            text = "${item.sevaCategory} • ${item.dateStr}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Text(
                                    text = "₹${item.amountRupees}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = RitualClay
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // PERSONAL & AUTH TAB
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        // Spiritual Identity Header
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
                            text = "Firebase Account & Cloud Sync",
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
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (firebaseUser != null) Icons.Default.Verified else Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = if (firebaseUser != null) DeepMoss else RitualClay,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (firebaseUser != null) {
                                                firebaseUser.displayName ?: firebaseUser.email ?: "Authenticated Devotee"
                                            } else {
                                                "Offline Devotee Mode"
                                            },
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = if (firebaseUser != null) {
                                                "UID: ${firebaseUser.uid.take(12)}... • Firestore Synced"
                                            } else {
                                                "Sign in to synchronize your bookings & seva across devices"
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                if (firebaseUser != null) {
                                    OutlinedButton(
                                        onClick = onSignOut,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RitualClay)
                                    ) {
                                        Text("Sign Out from Firebase")
                                    }
                                } else {
                                    Button(
                                        onClick = { showAuthDialog = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = RitualClay)
                                    ) {
                                        Text("Sign In / Register with Firebase")
                                    }
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

    // Firebase Auth Dialog
    if (showAuthDialog) {
        AlertDialog(
            onDismissRequest = { showAuthDialog = false },
            title = {
                Text(
                    text = if (authIsSignUp) "Register with Firebase" else "Sign In with Firebase",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (authIsSignUp) {
                        OutlinedTextField(
                            value = authDisplayName,
                            onValueChange = { authDisplayName = it },
                            label = { Text("Devotee Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = authEmail,
                        onValueChange = { authEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = authPassword,
                        onValueChange = { authPassword = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    authError?.let { err ->
                        Text(
                            text = err,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                authIsSignUp = !authIsSignUp
                                authError = null
                            }
                        ) {
                            Text(
                                text = if (authIsSignUp) "Already registered? Sign in" else "New devotee? Register",
                                fontSize = 12.sp,
                                color = RitualClay
                            )
                        }
                    }

                    // Quick Devotee sign-in option
                    TextButton(
                        onClick = {
                            authLoading = true
                            onSignInAsDevotee("Arjun Desai") { success, err ->
                                authLoading = false
                                if (success) {
                                    showAuthDialog = false
                                } else {
                                    authError = err ?: "Sign in failed"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue as Arjun Desai (Demo Devotee)", fontSize = 12.sp, color = DeepMoss)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (authEmail.isBlank() || authPassword.isBlank()) {
                            authError = "Please fill in all fields"
                            return@Button
                        }
                        authLoading = true
                        authError = null
                        if (authIsSignUp) {
                            onSignUpWithEmail(authEmail, authPassword, authDisplayName.ifBlank { "Devotee" }) { success, err ->
                                authLoading = false
                                if (success) {
                                    showAuthDialog = false
                                } else {
                                    authError = err ?: "Sign up failed"
                                }
                            }
                        } else {
                            onSignInWithEmail(authEmail, authPassword) { success, err ->
                                authLoading = false
                                if (success) {
                                    showAuthDialog = false
                                } else {
                                    authError = err ?: "Sign in failed"
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RitualClay),
                    enabled = !authLoading
                ) {
                    if (authLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                    } else {
                        Text(if (authIsSignUp) "Create Account" else "Sign In")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showAuthDialog = false }) {
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
