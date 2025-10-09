package com.example.heartbeat.presentation.features.donation.ui.register_detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.donation.ui.RegisterDonationScreen
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.PeachBackground

@Composable
fun DonationDetailScreen(
    eventId: String,
    donorId: String,
    donationViewModel: DonationViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    navController: NavController
) {
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val formState by donorViewModel.formState.collectAsState()
    val uiState by donationViewModel.uiState.collectAsState()

    val allDonations = uiState.donations
    val donationForThisEvent = allDonations.firstOrNull { it.eventId == eventId }

    Log.d("DonationDetailScreen", "Info: ${donationForThisEvent?.status}")

    LaunchedEffect(donorId) {
        donationViewModel.getDonationsByDonor(donorId)
    }

    LaunchedEffect(Unit) {
        eventViewModel.getEventById(eventId)
        donorViewModel.getCurrentDonor()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                                    .clickable { navController.popBackStack() }
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

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when {
                        donationForThisEvent == null && !uiState.isLoading -> {
                            hospital?.let {
                                RegisterDonationScreen(
                                    hospital = it,
                                    event = event,
                                    eventId = eventId,
                                    donorId = donorId,
                                    formState = formState
                                )
                            }
                        }
                        donationForThisEvent != null -> {
                            when (donationForThisEvent.status) {
                                "PENDING" -> PendingScreen(donationViewModel, donorId)
                                "APPROVED" -> ApprovedScreen()
                                "REJECTED" -> RejectedScreen(onRegisterAgain = {})
                                "DONATED" -> DonatedScreen()
                            }
                        }
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
            }
        }
    }
}