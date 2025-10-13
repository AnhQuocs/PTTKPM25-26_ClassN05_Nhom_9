package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.SoftCream
import com.example.heartbeat.ui.theme.WaitingGold

@Composable
fun PendingScreen(
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.PaddingL)
            .clip(RoundedCornerShape(AppShape.ExtraLargeShape))
            .background(CompassionBlue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.HourglassEmpty,
            contentDescription = null,
            modifier = Modifier.size(Dimens.SizeXXL),
            tint = WaitingGold
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Text(
            text = stringResource(id = R.string.pending_title),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Text(
            text = stringResource(id = R.string.pending_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF707070)
        )

        Spacer(Modifier.height(AppSpacing.ExtraLarge))

        OutlinedButton(
            onClick = { onBackHome() },
            border = BorderStroke(1.dp, color = WaitingGold),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = WaitingGold)
        ) {
            Icon(Icons.Default.Home, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text(stringResource(id = R.string.home))
        }
    }
}