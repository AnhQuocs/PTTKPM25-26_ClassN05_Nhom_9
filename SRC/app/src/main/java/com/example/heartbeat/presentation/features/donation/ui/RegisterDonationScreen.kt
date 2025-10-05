package com.example.heartbeat.presentation.features.donation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.ui.CircularProgressBar
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.PeachBackground
import com.example.heartbeat.ui.theme.UnityPeachText

@Composable
fun RegisterDonationScreen(
    eventId: String,
    donorId: String,
    donationViewModel: DonationViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel()
) {
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.getEventById(eventId)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.SizeMega + 10.dp)
                    .background(PeachBackground)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.Black),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = Dimens.PaddingM)
                                .size(Dimens.SizeML)
                        )

                        Text(
                            text = stringResource(id = R.string.register_donation),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.Large))

                    AppLineGrey()
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        selectedEvent?.let { event ->
            LaunchedEffect(Unit) {
                hospitalViewModel.loadHospitalById(hospitalId = event.locationId)
            }

            val hospital = hospitalViewModel.hospitalDetails[event.locationId]
            val location = "${hospital?.district}, ${hospital?.province}"

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.PaddingM)
                            .clip(RoundedCornerShape(AppShape.LargeShape))
                            .background(
                                color = CompassionBlue,
                                RoundedCornerShape(AppShape.LargeShape)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = Dimens.PaddingS, horizontal = Dimens.PaddingM)
                                .fillMaxSize(),
                        ) {
                            Text(
                                text = event.name,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(AppSpacing.Large))

                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.CenterStart)
                                ) {
                                    FormItem(
                                        icon = Icons.Default.Apartment,
                                        text = hospital?.hospitalName ?: "Loading...",
                                        iconColor = CompassionBlueText,
                                        textColor = CompassionBlueText
                                    )

                                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                                    FormItem(
                                        icon = Icons.Default.LocationOn,
                                        text = location,
                                        iconColor = Color.Black,
                                        textColor = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                                    FormItem(
                                        icon = Icons.Default.AccessTime,
                                        text = event.time,
                                        iconColor = UnityPeachText,
                                        textColor = UnityPeachText
                                    )
                                }

                                Spacer(modifier = Modifier.width(AppSpacing.Medium))

                                CircularProgressBar(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd),
                                    value = event.donorCount,
                                    capacity = event.capacity
                                )
                            }

                            Spacer(modifier = Modifier.width(AppSpacing.Medium))

                            DashedLine(
                                color = Color.LightGray,
                                strokeWidth = 2.dp,
                                dashLength = 10.dp,
                                gapLength = 6.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormItem(
    icon: ImageVector,
    text: String,
    iconColor: Color,
    textColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(Dimens.SizeM)
        )

        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
        )
    }
}

@Composable
fun DashedLine(
    strokeWidth: Dp = 1.dp,
    color: Color = Color.Gray,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(strokeWidth)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f
            )
        )
    }
}