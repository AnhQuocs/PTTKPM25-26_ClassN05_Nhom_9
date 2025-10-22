package com.example.heartbeat.presentation.features.users.staff.ui.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.presentation.features.users.staff.ui.home.stats.BloodBankStats
import com.example.heartbeat.presentation.features.users.staff.ui.home.stats.DonationStatsCard
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.AquaMint
import com.example.heartbeat.ui.theme.CoralRed
import com.example.heartbeat.ui.theme.GoldenGlow
import com.example.heartbeat.ui.theme.RoyalPurple
import com.example.heartbeat.ui.theme.SunsetOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StaffHomeScreen(
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    donationViewModel: DonationViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    navController: NavController
) {
    val authState by authViewModel.authState.collectAsState()
    val user = authState?.getOrNull()

    val donorCache by donorViewModel.donorCache.collectAsState()

    val uiState by donationViewModel.uiState.collectAsState()
    val donatedList = uiState.donatedList

    LaunchedEffect(Unit) {
        donationViewModel.getAllDonatedDonations()
        hospitalViewModel.loadHospitals()
        authViewModel.loadCurrentUser()
    }

    LaunchedEffect(donatedList) {
        val missingIds = donatedList.map { it.donorId }.filter { !donorCache.containsKey(it) }
        missingIds.forEach { donorViewModel.fetchDonorAndCache(it) }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.HeightXL2)
                    .background(
                        color = Color(0xFF2051E5),
                        RoundedCornerShape(
                            bottomStart = AppShape.SuperRoundedShape,
                            bottomEnd = AppShape.SuperRoundedShape
                        )
                    )
                    .clip(
                        RoundedCornerShape(
                            bottomStart = AppShape.SuperRoundedShape,
                            bottomEnd = AppShape.SuperRoundedShape
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bgr_top_bar),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                )

                Row(
                    modifier = Modifier
                        .padding(Dimens.PaddingL)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.welcome),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Small))

                        user?.let {
                            Text(
                                text = user.username ?: "User",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.padding(bottom = Dimens.PaddingS).size(Dimens.SizeM)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM),
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = Dimens.PaddingM)
        ) {
            item {
                DonationStatsCard()
            }

            item {
                BloodBankStats(donatedList = donatedList, donorCache = donorCache)
            }

            item { Spacer(modifier = Modifier.height(AppSpacing.ExtraSmall)) }

            item {
                PendingListSection(navController = navController)
            }
        }
    }
}