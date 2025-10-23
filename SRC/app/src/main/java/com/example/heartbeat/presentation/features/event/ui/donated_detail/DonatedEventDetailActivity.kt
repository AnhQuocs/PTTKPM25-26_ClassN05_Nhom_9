package com.example.heartbeat.presentation.features.event.ui.donated_detail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
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
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.ui.event_detail.EventInfoCard
import com.example.heartbeat.presentation.features.event.ui.event_detail.HospitalInfoCard
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.system.province.viewmodel.ProvinceViewModel
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonatedEventDetailActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventId = intent.getStringExtra("eventId") ?: "eventId"
        val donorId = intent.getStringExtra("donorId") ?: "donorId"
        val hospitalId = intent.getStringExtra("hospitalId") ?: "hospitalId"

        setContent {
            DonatedEventDetailScreen(
                eventId = eventId,
                donorId = donorId,
                hospitalId = hospitalId,
                onBackClick = { finish() }
            )
        }
    }
}

@Composable
fun DonatedEventDetailScreen(
    eventId: String,
    donorId: String,
    hospitalId: String,
    onBackClick: () -> Unit,
    donationViewModel: DonationViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    provinceViewModel: ProvinceViewModel = hiltViewModel()
) {
    val formState by donorViewModel.formState.collectAsState()
    val isLoading by donorViewModel.isLoading.collectAsState()
    val uiState by donationViewModel.uiState.collectAsState()
    val selectedDonation = uiState.selectedDonation
    val province by provinceViewModel.selectedProvince.collectAsState()

    LaunchedEffect(donorId) {
        donorViewModel.getCurrentDonor()
        donationViewModel.observeDonationForDonor(eventId, donorId)
    }

    LaunchedEffect(hospitalId) {
        hospitalViewModel.loadHospitalById(hospitalId)

    }

    LaunchedEffect(formState.cityId) {
        provinceViewModel.loadProvinceById(formState.cityId)
    }

    val hospital = hospitalViewModel.hospitalDetails[hospitalId]

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PaddingM)
                ) {
                    SettingTitle(
                        text = stringResource(id = R.string.donation_details),
                        onClick = onBackClick
                    )
                }
            },
            modifier = Modifier.fillMaxSize().padding(top = Dimens.PaddingM)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF2F4F4))
                    .padding(paddingValues)
                    .padding(horizontal = Dimens.PaddingM)
                    .padding(top = Dimens.PaddingM)
            ) {
                item { ThankBannerCard() }

                item { Spacer(modifier = Modifier.height(AppSpacing.LargePlus)) }

                item {
                    EventInfoCard(eventId = eventId)
                }

                item { Spacer(modifier = Modifier.height(AppSpacing.LargePlus)) }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .weight(0.3f),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )

                        Text(
                            text = stringResource(id = R.string.hosted_by),
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = Dimens.PaddingS)
                        )

                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .weight(0.3f),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(AppSpacing.LargePlus)) }

                item {
                    hospital?.let {
                        HospitalInfoCard(hospital = it)
                    }
                }

                item { Spacer(modifier = Modifier.height(AppSpacing.LargePlus)) }

                item {
                    selectedDonation?.let {
                        province?.let { it1 ->
                            DonatedDetailDonorInfo(
                                formState = formState,
                                donation = it,
                                province = it1.name
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(AppSpacing.LargePlus)) }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
            }
        }
    }
}