package com.example.heartbeat.presentation.features.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    size: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(top = 60.dp)
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun Indicator(
    isSelected: Boolean
) {
    val width = animateDpAsState(targetValue = if(isSelected) 25.dp else 12.dp, label = "")

    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(12.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if(isSelected) Color(0xFFFF4747) else Color(0xFFFFCBCB)
            )
    )
}