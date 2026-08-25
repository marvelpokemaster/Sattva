package com.example.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8F5E9))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Utsavam Shared UI - CMP Wired Correctly!",
                color = Color(0xFF2E7D32)
            )
        }
    }
}
