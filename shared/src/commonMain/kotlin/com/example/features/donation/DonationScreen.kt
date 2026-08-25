package com.example.features.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    targetName: String,
    targetType: String,
    onBack: () -> Unit,
    onSubmit: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedAmount by remember { mutableStateOf(500) }
    var selectedCategory by remember { mutableStateOf("General Care") }
    var isMonthly by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Support $targetName", fontFamily = SerifFontFamily, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text("Your contribution creates real impact.", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(24.dp))
            
            // Support Type
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SupportTypeChip("One-time", !isMonthly) { isMonthly = false }
                SupportTypeChip("Monthly Sponsorship", isMonthly) { isMonthly = true }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Amount Grid
            Text("Select Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            val amounts = if (isMonthly) listOf(500, 1000, 2000, 5000) else listOf(200, 500, 1000, 2500)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                amounts.take(2).forEach { amt -> AmountCard(amt, amt == selectedAmount, modifier = Modifier.weight(1f)) { selectedAmount = amt } }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                amounts.drop(2).forEach { amt -> AmountCard(amt, amt == selectedAmount, modifier = Modifier.weight(1f)) { selectedAmount = amt } }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Impact Preview
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Impact Preview", color = RitualClay, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val impactText = when (selectedAmount) {
                        200 -> "Provides green fodder for 1 week."
                        500 -> if (isMonthly) "Supports monthly feed and basic supplements." else "Covers essential deworming and medical checkups."
                        1000 -> if (isMonthly) "Provides complete nutrition and medical care for the month." else "Funds a comprehensive veterinary treatment."
                        2000, 2500 -> if (isMonthly) "Full adoption sponsorship covering all needs." else "Provides an emergency medical fund for critical cases."
                        else -> "Directly supports the urgent needs of the sanctuary."
                    }
                    
                    Text(impactText, fontSize = 16.sp, fontFamily = SerifFontFamily, color = DeepMoss, fontWeight = FontWeight.Medium)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = { onSubmit(selectedAmount, selectedCategory) },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepMoss),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isMonthly) "Sponsor ₹$selectedAmount / month" else "Contribute ₹$selectedAmount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SupportTypeChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = if (isSelected) DeepMoss else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(text, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun AmountCard(amount: Int, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = if (isSelected) DeepMoss.copy(alpha = 0.1f) else SurfaceIvory),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, DeepMoss) else null,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(64.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("₹$amount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (isSelected) DeepMoss else MaterialTheme.colorScheme.onSurface)
        }
    }
}
