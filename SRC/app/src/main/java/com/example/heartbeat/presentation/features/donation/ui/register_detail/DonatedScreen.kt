package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.heartbeat.R

@Composable
fun DonatedScreen(
    onViewDetail: () -> Unit
) {
    StatusScreenComponent(
        bgrColor = Color(0xFFF4F9FD),
        mainIcon = Icons.Default.DoneOutline,
        mainIconColor = Color(0xFF388E3C),
        title = stringResource(id = R.string.donated_title),
        message = stringResource(id = R.string.donated_message),
        onClick = onViewDetail,
        buttonColor = Color(0xFF388E3C),
        buttonIcon = Icons.Default.Visibility,
        buttonText = stringResource(id = R.string.view_details)
    )
}