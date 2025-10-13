package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.WaitingGold

@Composable
fun PendingScreen(
    onBackHome: () -> Unit
) {
    StatusScreenComponent(
        bgrColor = CompassionBlue,
        mainIcon = Icons.Default.HourglassEmpty,
        mainIconColor = WaitingGold,
        mainIconSize = Dimens.SizeXXL,
        title = stringResource(id = R.string.pending_title),
        message = stringResource(id = R.string.pending_message),
        onClick = onBackHome,
        buttonColor = WaitingGold,
        buttonIcon = Icons.Default.Home,
        buttonText = stringResource(id = R.string.home)
    )
}