package com.example.heartbeat.presentation.features.donation.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.presentation.features.event.ui.CircularProgressBar
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.Green500
import com.example.heartbeat.ui.theme.UnityPeachText
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun RegisterDonationScreen(
    formState: DonorFormState,
    hospital: Hospital,
    event: Event,
    eventId: String,
    donorId: String
) {
    val location = "${hospital?.district}, ${hospital?.province}"
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val (startStr, endStr) = event.time.split(" ")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)
    val time = "$startTime - $endTime"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                vertical = Dimens.PaddingS,
                                horizontal = Dimens.PaddingM
                            )
                            .fillMaxWidth(),
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
                                EventInfoItem(
                                    icon = Icons.Default.Apartment,
                                    text = hospital?.hospitalName ?: "Loading...",
                                    iconColor = CompassionBlueText,
                                    textColor = CompassionBlueText,
                                    modifier = Modifier.padding(end = 56.dp)
                                )

                                Spacer(modifier = Modifier.height(AppSpacing.Large))

                                EventInfoItem(
                                    icon = Icons.Default.AccessTime,
                                    text = time,
                                    iconColor = UnityPeachText,
                                    textColor = UnityPeachText
                                )

                                Spacer(modifier = Modifier.height(AppSpacing.Large))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Green500,
                                        modifier = Modifier
                                            .align(Alignment.Top)
                                            .size(Dimens.SizeM)
                                    )

                                    Spacer(modifier = Modifier.width(AppSpacing.MediumPlus))

                                    Column {
                                        Text(
                                            text = hospital?.address ?: "Loading...",
                                            color = Green500,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontSize = 15.sp,
                                                lineHeight = 1.sp
                                            )
                                        )

                                        Text(
                                            text = location,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 12.sp,
                                                lineHeight = 1.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(AppSpacing.Large))

                                EventInfoItem(
                                    icon = Icons.Default.Description,
                                    text = event.description,
                                    iconColor = Color.Black,
                                    textColor = Color.Black,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(AppSpacing.Medium))

                            CircularProgressBar(
                                modifier = Modifier
                                    .padding(bottom = Dimens.PaddingS)
                                    .align(Alignment.TopEnd),
                                value = event.donorCount,
                                capacity = event.capacity
                            )
                        }

                        Spacer(modifier = Modifier.width(AppSpacing.Medium))
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                    DashedLine(
                        color = Color.LightGray,
                        strokeWidth = 2.dp,
                        dashLength = 10.dp,
                        gapLength = 6.dp
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = Dimens.PaddingM)
                            .padding(bottom = Dimens.PaddingM, top = Dimens.PaddingS)
                            .fillMaxWidth(),
                    ) {
                        val dateOfBirth = "${formState.dateOfBirth} (${formState.age})"

                        Text(
                            text = stringResource(id = R.string.registration_information),
                            color = Color.Black.copy(alpha = 0.65f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Large))

                        FormItem(
                            title = stringResource(id = R.string.full_name),
                            value = formState.name
                        )

                        FormItem(
                            title = stringResource(id = R.string.date_of_birth),
                            value = dateOfBirth
                        )

                        FormItem(
                            title = stringResource(id = R.string.gender),
                            value = formState.gender
                        )

                        FormItem(
                            title = stringResource(id = R.string.phone_number),
                            value = formState.phoneNumber
                        )

                        FormItem(
                            title = stringResource(id = R.string.blood_group),
                            value = formState.bloodGroup
                        )

                        FormItem(
                            title = stringResource(id = R.string.city),
                            value = formState.city
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Small))

                        DonationForm(
                            eventId = eventId,
                            donorId = donorId
                        )
                    }
                }
            }
        }
    }
}