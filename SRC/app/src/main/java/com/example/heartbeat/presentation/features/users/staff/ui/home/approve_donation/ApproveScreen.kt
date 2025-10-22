package com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.presentation.features.users.staff.component.SubScreenTopBar
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.Green500
import com.example.heartbeat.ui.theme.HopeGreen

@Composable
fun ApproveScreen(
    eventId: String,
    donationViewModel: DonationViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by donationViewModel.uiState.collectAsState()
    val donations = uiState.donations

    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val donorCache by donorViewModel.donorCache.collectAsState()

    LaunchedEffect(eventId) {
        donationViewModel.observeDonationsByEvent(eventId)
//        donationViewModel.observePendingDonations()
        eventViewModel.getEventById(eventId)
    }

    Scaffold(
        topBar = {
            SubScreenTopBar(
                bgrColor = Color(0xFF2051E5),
                textColor = Color.White,
                text = stringResource(id = R.string.approve),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        selectedEvent?.let { event ->
            LaunchedEffect(Unit) {
                hospitalViewModel.loadHospitalById(hospitalId = event.locationId)
            }

            val hospital = hospitalViewModel.hospitalDetails[event.locationId]

            Crossfade(targetState = uiState.isLoading, label = "donor_shimmer_fade") { isLoading ->
                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(Dimens.PaddingM)
                    ) {
                        EventInfoCardShimmer()

                        Spacer(modifier = Modifier.height(AppSpacing.Large))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = Dimens.HeightShimmer)
                        ) {
                            items(5) {
                                DonorInfoShimmer()
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(Dimens.PaddingM)
                    ) {
                        hospital?.let {
                            EventInfoCard(event = event, hospital = it)
                        }

                        Spacer(modifier = Modifier.height(AppSpacing.Large))

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(
                                donations.filter { it.status == "PENDING" },
                                key = { it.donationId }
                            ) { donation ->
                                val donor = donorCache[donation.donorId]

                                LaunchedEffect(donation.donationId) {
                                    if (donor == null) {
                                        donorViewModel.fetchDonorAndCache(donation.donorId)
                                    }
                                }

                                donor?.let {
                                    val formState = DonorFormState(
                                        name = it.name,
                                        phoneNumber = it.phoneNumber,
                                        bloodGroup = it.bloodGroup,
                                        cityId = it.cityId,
                                        dateOfBirth = it.dateOfBirth,
                                        age = it.age,
                                        gender = it.gender
                                    )

                                    DonorInfo(
                                        donation = donation,
                                        formState = formState,
                                        onApprove = {
                                            donationViewModel.updateStatus(
                                                donationId = donation.donationId,
                                                status = "APPROVED"
                                            )

                                            donationViewModel.approveDonation(
                                                donationId = donation.donationId,
                                                donorId = donation.donorId
                                            )
                                        },
                                        onReject = {
                                            donationViewModel.updateStatus(
                                                donationId = donation.donationId,
                                                status = "REJECTED"
                                            )

                                            eventViewModel.updateDonorCount(event.id, -1)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventInfoCard(event: Event, hospital: Hospital) {
    val location = "${hospital.district}, ${hospital.province}"

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.MediumShape),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.HeightXL3)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            CompassionBlue,
                            HopeGreen
                        )
                    )

                )
                .padding(Dimens.PaddingS)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
            ) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF01579B),
                    modifier = Modifier.fillMaxWidth()
                )

                EventInfoCardItem(icon = Icons.Default.DateRange, text = event.date)

                EventInfoCardItem(icon = Icons.Default.Apartment, text = hospital.hospitalName)

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
                            .size(Dimens.SizeSM)
                    )

                    Spacer(modifier = Modifier.width(AppSpacing.MediumPlus))

                    Column {
                        Text(
                            text = hospital.address,
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
            }
        }
    }
}

@Composable
fun EventInfoCardItem(icon: ImageVector, text: String, modifier: Modifier = Modifier, color: Color = Color(0xFF01579B)) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Dimens.SizeSM)
        )

        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun DonorInfo(
    donation: Donation,
    formState: DonorFormState,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rowHeight = Dimens.HeightLarge - 4.dp

    val lastDonation = donation.donatedAt.ifBlank { stringResource(id = R.string.no_record) }
    val detailStyle =
        MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppShape.SmallShape + 2.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = Dimens.PaddingSM, vertical = Dimens.PaddingXS)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                tint = Color.DarkGray.copy(alpha = 0.8f),
                contentDescription = null,
                modifier = Modifier.size(Dimens.SizeL)
            )

            Spacer(modifier = Modifier.width(AppSpacing.MediumLarge))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    formState.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formState.bloodGroup,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.Gray
                )
            }

            Text(
                text = formState.dateOfBirth,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = Color.Gray
            )

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.PaddingXXL - 4.dp,
                        end = Dimens.PaddingS,
                        bottom = Dimens.PaddingS
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingXS)
                ) {
                    Text(
                        text = stringResource(id = R.string.city) + ": ${formState.cityId}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.age) + ": ${formState.age}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.gender) + ": ${formState.gender}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.phone_number) + ": ${formState.phoneNumber}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.label_last_donation) + ": $lastDonation",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.citizen_id) + ": ${donation.citizenId}",
                        style = detailStyle
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text(stringResource(id = R.string.reject))
                    }

                    Button(
                        onClick = onApprove,
                        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.approve))
                    }
                }
            }
        }

        Divider(color = Color(0xFFE0E0E0))
    }
}