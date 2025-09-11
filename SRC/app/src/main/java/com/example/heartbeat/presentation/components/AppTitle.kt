package com.example.heartbeat.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AppTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}