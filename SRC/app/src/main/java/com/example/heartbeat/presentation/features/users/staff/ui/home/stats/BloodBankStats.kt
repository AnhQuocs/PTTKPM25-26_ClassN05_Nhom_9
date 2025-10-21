package com.example.heartbeat.presentation.features.users.staff.ui.home.stats

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun BloodBankStats(
    donatedList: List<Donation>,
    donorCache: Map<String, Donor>
) {
    Log.d("BloodBankStats", "$donatedList")

    val bloodGroups = listOf("O-", "O+", "A+", "A-", "B+", "B-", "AB+", "AB-")

    val chartColors = listOf(
        Color(0xFF2196F3), // O-
        Color(0xFFFF9800), // O+
        Color(0xFF4CAF50), // A+
        Color(0xFFE91E63), // A-
        Color(0xFF9C27B0), // B+
        Color(0xFFFFEB3B), // B-
        Color(0xFF795548), // AB+
        Color(0xFF607D8B)  // AB-
    )

    val volumeByGroup = bloodGroups.map { group ->
        donatedList.mapNotNull { donation ->
            val donor = donorCache[donation.donorId]
            if (donor?.bloodGroup == group) donation.donationVolume.toIntOrNull() ?: 0
            else null
        }.sum()
    }

    val labels = bloodGroups.mapIndexed { index, group ->
        "$group (${volumeByGroup[index]})"
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.PaddingXXS)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingM),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(id = R.string.blood_stats_title),
                style = MaterialTheme.run {
                    typography.titleSmall.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.PaddingS),
            ) {
                AnimatedLabeledDonutChart(
                    data = volumeByGroup,
                    labels = labels,
                    colors = chartColors,
                    centerTotal = volumeByGroup.sum(),
                    centerSubText = "ml",
                    modifier = Modifier.size(100.dp),
                    showLines = false
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.MediumLarge)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
                    ) {
                        bloodGroups.take(4).forEachIndexed { index, group ->
                            val color = chartColors[index]
                            CaptionItem(color, group)
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
                    ) {
                        bloodGroups.drop(4).take(4).forEachIndexed { index, group ->
                            val color = chartColors[index + 4]
                            CaptionItem(color, group)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))
        }
    }
}

@Composable
fun CaptionItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )

        Text(text)
    }
}