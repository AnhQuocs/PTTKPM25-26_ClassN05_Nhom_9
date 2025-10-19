package com.example.heartbeat.presentation.features.system.setting

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.Green500
import com.example.heartbeat.ui.theme.HeroLavenderText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonationHistoryActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DonationHistoryScreen(onBackClick = { finish() })
        }
    }
}

@Composable
fun DonationHistoryScreen(
    onBackClick: () -> Unit,
    donationViewModel: DonationViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val uiState by donationViewModel.uiState.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    Log.d("DonationHistoryScreen", "Current userId = $userId")

    var donationDetails by remember { mutableStateOf<List<Triple<Donation, Event?, Hospital?>>>(emptyList()) }

    LaunchedEffect(uiState.donations, userId) {
        if (uiState.donations.isNotEmpty()) {
            donationViewModel.getDonatedDonationsWithEventAndHospital(
                donorId = userId,
                eventViewModel = eventViewModel,
                hospitalViewModel = hospitalViewModel
            ) { result ->
                donationDetails = result
                Log.d("DonationHistoryScreen", "Loaded ${result.size} donated items")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(Dimens.PaddingM + 2.dp)
            .padding(top = Dimens.PaddingM)
    ) {
        SettingTitle(
            text = stringResource(R.string.history),
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

        LazyColumn {
            items(
                items = donationDetails,
                key = { it.second?.id ?: it.first.donationId }
            ) { (_, event, hospital) ->
                Log.d("DonationHistoryScreen", "Hospital: $hospital")
                if (event != null && hospital != null) {
                    HistoryEventCard(
                        event = event,
                        hospital = hospital,
                        onViewDetail = {}
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryEventCard(
    event: Event,
    hospital: Hospital,
    onViewDetail: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.PaddingM)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(Dimens.SizeXXLPlus)
                        .clip(RoundedCornerShape(AppShape.LargeShape))
                        .background(Color.White, RoundedCornerShape(AppShape.LargeShape)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = hospital.imgUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(Dimens.SizeXXL + 10.dp)
                            .clip(RoundedCornerShape(AppShape.LargeShape))
                    )
                }

                Spacer(modifier = Modifier.width(AppSpacing.Medium))

                Column {
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Small))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingXXS)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Green500,
                            modifier = Modifier
                                .size(Dimens.SizeS)
                                .align(Alignment.Top)
                        )

                        Text(
                            text = hospital.address,
                            color = Color(0xFF707070),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 12.sp
                            )
                        )
                    }
                }
            }

            Button(
                onClick = onViewDetail,
                colors = ButtonDefaults.buttonColors(containerColor = HeroLavenderText),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .align(Alignment.End)
            ) {
                Text(
                    text = stringResource(id = R.string.view_details),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}