package com.example.heartbeat.presentation.features.users.staff.ui.home.stats

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.ArrowRightAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationStatsViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.OceanBlue
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun DonationStatsCard(
    donationStatsViewModel: DonationStatsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val allTimeCount by donationStatsViewModel.allTimeCount.collectAsState()
    val isLoading by donationStatsViewModel.isLoading.collectAsState()
    val dayCount by donationStatsViewModel.dayCount.collectAsState()
    val weekCount by donationStatsViewModel.weekCount.collectAsState()
    val monthCount by donationStatsViewModel.monthCount.collectAsState()

    val selectedDay by donationStatsViewModel.selectedDay.collectAsState()
    val selectedWeek by donationStatsViewModel.selectedWeek.collectAsState()
    val selectedMonth by donationStatsViewModel.selectedMonth.collectAsState()

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.PaddingSM)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingM),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(id = R.string.donation_stats_title),
                    style = MaterialTheme.run {
                        typography.titleSmall.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = OceanBlue,
                    modifier = Modifier
                        .size(Dimens.SizeM)
                        .clickable {
                            val calendar = Calendar.getInstance()
                            val datePicker = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val selected = LocalDate.of(year, month + 1, dayOfMonth)
                                    donationStatsViewModel.setSelectedDay(selected)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePicker.show()
                        }
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BloodRed)
                }
            } else {
                val data = listOf(dayCount ?: 0, weekCount ?: 0, monthCount ?: 0)

                val labels = listOf(
                    stringResource(R.string.day_label) + " ($dayCount)",
                    stringResource(R.string.week_label) + " ($weekCount)",
                    stringResource(R.string.month_label) + " ($monthCount)"
                )

                if (data.sum() == 0) {
                    Text(
                        stringResource(id = R.string.no_donation_data),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    AnimatedLabeledDonutChart(
                        data = data,
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFFFF9800),
                            Color(0xFF4CAF50)
                        ),
                        labels = labels,
                        modifier = Modifier.size(120.dp),
                        centerTotal = allTimeCount,
                        centerSubText = stringResource(id = R.string.total_registered)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

            SelectedPeriodDisplay(
                selectedDay = selectedDay,
                selectedWeek = selectedWeek,
                selectedMonth = selectedMonth
            )
        }
    }
}

@Composable
fun SelectedPeriodDisplay(
    selectedDay: LocalDate,
    selectedWeek: LocalDate,
    selectedMonth: YearMonth,
    modifier: Modifier = Modifier
) {
    val dayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy")

    val weekEnd = selectedWeek.plusDays(6)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingXXS)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.weight(0.18f)
            ) {
                Text(
                    text = stringResource(R.string.day_label),
                    color = Color(0xFF2196F3)
                )
            }

            Text(
                text = ": " + selectedDay.format(dayFormatter),
                modifier = Modifier.weight(0.9f),
                color = Color(0xFF2196F3)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(0.18f)
            ) {
                Text(
                    text = stringResource(R.string.week_label),
                    color = Color(0xFFFF9800)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.9f)
            ) {
                Text(
                    text = ": " + selectedWeek.format(dayFormatter),
                    color = Color(0xFFFF9800)
                )
                Icon(
                    Icons.Rounded.ArrowRightAlt,
                    contentDescription = null,
                    tint = Color(0xFFFF9800)
                )
                Text(
                    text = weekEnd.format(dayFormatter),
                    modifier = Modifier.weight(0.9f),
                    color = Color(0xFFFF9800)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.weight(0.18f)
            ) {
                Text(
                    text = stringResource(R.string.month_label),
                    color = Color(0xFF4CAF50)
                )
            }

            Text(
                text = ": " + selectedMonth.format(monthFormatter),
                modifier = Modifier.weight(0.9f),
                color = Color(0xFF4CAF50)
            )
        }
    }
}