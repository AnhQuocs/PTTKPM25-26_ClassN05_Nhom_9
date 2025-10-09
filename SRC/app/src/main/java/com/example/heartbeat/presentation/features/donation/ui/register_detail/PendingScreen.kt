package com.example.heartbeat.presentation.features.donation.ui.register_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Refresh
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
    donationViewModel: DonationViewModel,
    donorId: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCream)
            .padding(Dimens.PaddingM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(AppShape.ExtraExtraLargeShape),
            colors = CardDefaults.cardColors(containerColor = CompassionBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(Dimens.PaddingL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.HourglassEmpty,
                    contentDescription = null,
                    tint = WaitingGold,
                    modifier = Modifier.size(Dimens.SizeL)
                )

                Spacer(Modifier.height(AppSpacing.MediumLarge))

                Text(
                    text = stringResource(id = R.string.pending_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2B2B2B)
                )

                Spacer(Modifier.height(AppSpacing.Medium))

                Text(
                    text = stringResource(id = R.string.pending_message),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF707070)
                )

                Spacer(Modifier.height(AppSpacing.LargePlus))

                OutlinedButton(
                    onClick = {
                        donationViewModel.getDonationsByDonor(donorId)
                    },
                    border = BorderStroke(1.dp, WaitingGold),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WaitingGold)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .size(Dimens.SizeSM)
                    )

                    Spacer(Modifier.width(AppSpacing.Small + 2.dp))

                    Text(stringResource(R.string.pending_check_status))
                }
            }
        }
    }
}