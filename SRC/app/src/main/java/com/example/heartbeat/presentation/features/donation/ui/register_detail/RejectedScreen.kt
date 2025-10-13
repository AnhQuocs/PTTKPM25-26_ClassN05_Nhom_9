package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation.StatusScreenComponent

@Composable
fun RejectedScreen(
    onRegisterAgain: () -> Unit
) {
    StatusScreenComponent(
        bgrColor = Color(0xFFFDF4F4),
        mainIcon = Icons.Default.HighlightOff,
        mainIconColor = Color(0xFFE57373),
        title = stringResource(id = R.string.rejected_title),
        message = stringResource(id = R.string.rejected_message),
        onClick = onRegisterAgain,
        buttonColor = Color(0xFFE57373),
        buttonIcon = Icons.Default.FavoriteBorder,
        buttonText = stringResource(id = R.string.register_again)
    )
}