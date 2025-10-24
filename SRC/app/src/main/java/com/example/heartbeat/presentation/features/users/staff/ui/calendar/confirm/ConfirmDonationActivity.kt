package com.example.heartbeat.presentation.features.users.staff.ui.calendar.confirm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.presentation.features.users.staff.ui.calendar.EditEventActivity
import com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation.DonorInfoShimmer
import com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation.EventInfoCardItem
import com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation.EventInfoCardShimmer
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.FacebookBlue
import com.example.heartbeat.ui.theme.Green500
import com.example.heartbeat.ui.theme.HopeGreen
import com.example.heartbeat.ui.theme.UnityPeachText
import com.example.heartbeat.ui.theme.WaitingGold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ConfirmDonationActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventId = intent.getStringExtra("eventId") ?: ""

        setContent {
            ConfirmDonationScreen(eventId = eventId, onBackClick = { finish() })
        }
    }
}

@Composable
fun ConfirmDonationScreen(
    eventId: String,
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    donationViewModel: DonationViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val observedEvent by eventViewModel.observedEvent.collectAsState()
    val hospital = hospitalViewModel.hospitalDetails[observedEvent?.locationId]

    val uiState by donationViewModel.uiState.collectAsState()
    val donations = uiState.donations

    val approvedList = donations.filter { it.status == "APPROVED" }
    val pendingList = donations.filter { it.status == "PENDING" }

    val donorCache by donorViewModel.donorCache.collectAsState()

    var bloodVolume by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var selectedDonation by remember { mutableStateOf<Donation?>(null) }

    LaunchedEffect(eventId) {
        eventViewModel.observeEventById(eventId)
        donationViewModel.observeDonationsByEvent(eventId)
    }

    LaunchedEffect(observedEvent) {
        observedEvent?.let {
            hospitalViewModel.loadHospitalById(it.locationId)
            Log.d("ConfirmDonationScreen", "EventID: $eventId")
            Log.d("ConfirmDonationScreen", "Donor Count: ${observedEvent?.donorCount}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(Dimens.PaddingM + 2.dp)
            .padding(top = Dimens.PaddingSM),
    ) {
        SettingTitle(
            text = stringResource(id = R.string.confirm_donation),
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))

        Crossfade(targetState = uiState.isLoading, label = "donor_shimmer_fade") { isLoading ->
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    EventInfoCardShimmer(
                        modifier = Modifier.fillMaxWidth(),
                        height = Dimens.HeightXL4 - 10.dp,
                        isSpacer = true
                    )

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
                    modifier = Modifier.fillMaxSize()
                ) {
                    hospital?.let { h ->
                        observedEvent?.let { event ->
                            val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
                            val isDeadlinePassed = event.deadline?.let { it < now } ?: false

                            val isConfirmDonation = isEventOngoing(event.date, event.time)

                            ConfirmDonationCard(event, h, pendingCount = pendingList.size)

                            Spacer(modifier = Modifier.height(AppSpacing.Small))

                            Button(
                                onClick = {
                                    val intent = Intent(context, EditEventActivity::class.java)
                                        .putExtra("eventId", eventId)
                                    context.startActivity(intent)
                                },
                                enabled = !isDeadlinePassed,
                                shape = RoundedCornerShape(AppShape.SmallShape),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isDeadlinePassed) FacebookBlue else Color.Gray
                                ),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(Dimens.HeightDefault - 8.dp)
                                    .align(Alignment.End)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.edit),
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(AppSpacing.Large))

                            if(!isConfirmDonation && approvedList.isNotEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.confirm_donation_unavailable),
                                    color = BloodRed,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(AppSpacing.Medium))
                            }

                            LazyColumn {
                                if(approvedList.isEmpty()) {
                                    item {
                                        Text(
                                            text = stringResource(id = R.string.no_new_approved_donors),
                                            color = Color(0xFF757575),
                                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.PaddingL)
                                        )
                                    }
                                } else {
                                    items(
                                        approvedList,
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

                                            ConfirmDonorItem(
                                                bloodVolume = bloodVolume,
                                                onBloodVolumeChange = { newValue -> bloodVolume = newValue },
                                                donation = donation,
                                                formState = formState,
                                                onUnable = {
                                                    selectedDonation = donation
                                                    showDialog = true
                                                },
                                                onConfirm = {
                                                    donationViewModel.updateDonationVolume(
                                                        donationId = donation.donationId,
                                                        volume = bloodVolume
                                                    )

                                                    Toast.makeText(
                                                        context,
                                                        context.getString(R.string.confirm_success),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                isConfirmDonation = isConfirmDonation
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

        if (showDialog && selectedDonation != null) {
            UnableDialog(
                onDismiss = {
                    showDialog = false
                    selectedDonation = null
                },
                onConfirm = {
                    donationViewModel.deleteDonation(selectedDonation!!.donationId)
                    showDialog = false
                    selectedDonation = null
                }
            )
        }
    }
}

@Composable
fun UnableDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.unable),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.confirm_remove_person),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    Toast.makeText(
                        context,
                        context.getString(R.string.confirm_success),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ConfirmDonationCard(
    event: Event,
    hospital: Hospital,
    pendingCount: Int
) {
    val location = "${hospital.district}, ${hospital.province}"
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val (startStr, endStr) = event.time.split(" ")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)
    val time = "$startTime - $endTime"

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.MediumShape),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.HeightXL4 - 10.dp)
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
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF01579B),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EventInfoCardItem(icon = Icons.Default.DateRange, text = event.date)
                    Spacer(modifier = Modifier.width(AppSpacing.Medium))
                    Text(
                        text = time,
                        color = UnityPeachText,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                    )
                }

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

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                EventInfoCardItem(
                    Icons.Filled.CheckCircle,
                    color = Green500,
                    text = "${event.donorCount} " + stringResource(id = R.string.total_registered)
                )

                EventInfoCardItem(
                    Icons.Filled.HourglassEmpty,
                    color = WaitingGold,
                    text = pendingCount.toString() + " " + stringResource(id = R.string.donors_pending_approval)
                )
            }
        }
    }
}

fun isEventOngoing(date: String, time: String): Boolean {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate = LocalDate.parse(date, dateFormatter)

    val (startStr, endStr) = time.split(" ")

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)

    val startDateTime = LocalDateTime.of(localDate, startTime)
    val endDateTime = LocalDateTime.of(localDate, endTime)

    val now = LocalDateTime.now()

    return now.isAfter(startDateTime) && now.isBefore(endDateTime)
}