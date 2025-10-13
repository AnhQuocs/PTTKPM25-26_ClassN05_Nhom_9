package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.heartbeat.R

@Composable
fun ApprovedScreen(
    onViewDetail: () -> Unit
) {
    StatusScreenComponent(
        bgrColor = Color(0xFFF4F9FD),
        mainIcon = Icons.Default.CheckCircleOutline,
        mainIconColor = Color(0xFF81C784),
        title = stringResource(id = R.string.approved_title),
        message = stringResource(id = R.string.approved_message),
        onClick = onViewDetail,
        buttonColor = Color(0xFF81C784),
        buttonIcon = Icons.Default.Visibility,
        buttonText = stringResource(id = R.string.view_details)
    )
}