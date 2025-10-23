package com.example.heartbeat.presentation.features.system.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.heartbeat.presentation.features.event.ui.donated_detail.DonatedEventDetailActivity
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.Green500
import com.example.heartbeat.ui.theme.OceanBlue
import com.example.heartbeat.ui.theme.WaitingGold
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class DonationHistoryActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val donatedList: ArrayList<Donation>?  = intent.getParcelableArrayListExtra("donated_list", Donation::class.java)

        setContent {
            donatedList?.let { DonationHistoryScreen(onBackClick = { finish() }, list = it) }
        }
    }
}

@Composable
fun DonationHistoryScreen(
    onBackClick: () -> Unit,
    list: List<Donation>,
    donationViewModel: DonationViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by donationViewModel.uiState.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    Log.d("DonationHistoryScreen", "Current userId = $userId")
    Log.d("DonationHistoryScreen", "List = $list")

    LaunchedEffect(userId) {
        donationViewModel.getDonationsByDonor(userId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.PaddingM)
            ) {
                if (list.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.no_donation_yet),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    items(list) {donation ->
                        var event by remember { mutableStateOf<Event?>(null) }
                        val hospital by remember(event) {
                            derivedStateOf { event?.let { hospitalViewModel.hospitalDetails[it.locationId] } }
                        }

                        LaunchedEffect(donation.donationId) {
                            event = eventViewModel.getEventByIdDirect(donation.eventId)
                            event?.let { hospitalViewModel.loadHospitalById(it.locationId) }
                        }

                        Log.d("DonationHistoryScreen", "Hospital = $list")

                        event?.let { e ->
                            hospital?.let { h ->
                                HistoryEventCard(
                                    event = e,
                                    hospital = h,
                                    onViewDetail = {
                                        val intent = Intent(context, DonatedEventDetailActivity::class.java)
                                            .putExtra("eventId", e.id)
                                            .putExtra("donorId", userId)
                                            .putExtra("hospitalId", e.locationId)
                                        context.startActivity(intent)
                                    }
                                )
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
                    .background(Color.Black.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
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
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val (startStr, endStr) = event.time.split(" ")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)
    val time = "$startTime - $endTime"

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color = Color.Gray, RoundedCornerShape(AppShape.ExtraLargeShape))
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(0.5f),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = WaitingGold,
                            modifier = Modifier.size(Dimens.SizeSM)
                        )

                        Text(
                            text = event.date,
                            color = WaitingGold,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                    ) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = OceanBlue,
                            modifier = Modifier.size(Dimens.SizeSM)
                        )

                        Text(
                            text = time,
                            color = OceanBlue,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(AppSpacing.Medium))

                Button(
                    onClick = onViewDetail,
                    shape = RoundedCornerShape(AppShape.ExtraLargeShape),
                    colors = ButtonDefaults.buttonColors(containerColor = Green500),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
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
}