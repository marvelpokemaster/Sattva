package com.example.features.welfare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SurfaceIvory
import com.example.data.model.WelfareUpdate

@Composable
fun WelfareTimeline(
    updates: List<WelfareUpdate>,
    modifier: Modifier = Modifier
) {
    if (updates.isEmpty()) {
        Text(
            text = "No recent updates.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        return
    }
    Column(modifier = modifier) {
        updates.forEachIndexed { index, update ->
            WelfareUpdateCard(
                update = update,
                isLast = index == updates.size - 1
            )
        }
    }
}

@Composable
fun WelfareUpdateCard(
    update: WelfareUpdate,
    isLast: Boolean
) {
    val isMedical = update.eventType.contains("Medical", ignoreCase = true)

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        // Timeline line and dot
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (isMedical) RitualClay else DeepMoss)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp) // Approximate height, could be dynamic
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Content
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(update.dateStr, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceIvory),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isMedical) Icons.Default.MedicalServices else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isMedical) RitualClay else DeepMoss,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(update.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(update.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
                }
            }
        }
    }
}
