package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RejectedScreen(
    onRegisterAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFFFDF4F4)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.HighlightOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFFE57373) // đỏ nhẹ
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Your registration was rejected",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2B2B2B)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Unfortunately, your donation request was not approved.\nYou may contact the hospital for more details.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF707070)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onRegisterAgain,
            border = BorderStroke(1.dp, Color(0xFFE57373)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE57373))
        ) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Register again")
        }
    }
}