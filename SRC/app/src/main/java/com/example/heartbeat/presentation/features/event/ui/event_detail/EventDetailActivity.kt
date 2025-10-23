package com.example.heartbeat.presentation.features.event.ui.event_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.donation.ui.register_detail.DonationDetailScreen
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.Green500
import com.example.heartbeat.ui.theme.PeachBackground
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EventDetailActivity : BaseComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventId = intent.getStringExtra("eventId") ?: ""

        setContent {
            val navController = rememberNavController()
            AnimatedNavHost(navController = navController, startDestination = "event_detail") {
                composable("event_detail") {
                    EventDetailScreen(eventId, navController, onBackClick = { finish() })
                }

                composable(
                    route = "register_donation/{eventId}/{donorId}",
                    arguments = listOf(
                        navArgument("eventId") { type = NavType.StringType },
                        navArgument("donorId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val eventId1 = backStackEntry.arguments?.getString("eventId")
                    val donorId = backStackEntry.arguments?.getString("donorId")
                    DonationDetailScreen(
                        eventId = eventId1 ?: "",
                        donorId = donorId ?: "",
                        navController = navController
                    )
                }
            }

        }
    }
}

@Composable
fun EventDetailScreen(
    eventId: String,
    navController: NavController,
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    donationViewModel: DonationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val context = LocalContext.current

    val uiState by donationViewModel.uiState.collectAsState()
    val selectedDonation = uiState.selectedDonation

    LaunchedEffect(Unit) {
        eventViewModel.getEventById(eventId)
    }

    LaunchedEffect(selectedEvent?.id) {
        selectedEvent?.let { event ->
            hospitalViewModel.loadHospitalById(event.locationId)
        }
    }

    LaunchedEffect(userId) {
        userId?.let { donationViewModel.observeDonationForDonor(eventId, it) }
    }

    val isButtonEnable = selectedDonation?.status == "REJECTED" || selectedDonation?.status == null

    selectedEvent?.let { event ->
        val hospital = hospitalViewModel.hospitalDetails[event.locationId]
        val isValid = isFutureTime(isoString = event.deadline.toString())

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.HeightXL)
                            .background(PeachBackground)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(top = Dimens.PaddingL, start = Dimens.PaddingSM)
                                .size(Dimens.SizeL)
                                .clickable { onBackClick() }
                        )

                        Text(
                            stringResource(id = R.string.event_detail),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = Dimens.PaddingL)
                        )
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimens.PaddingSM)
                            .padding(horizontal = Dimens.PaddingM),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                hospital?.phone?.let { phone ->
                                    val dialIntent =
                                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(dialIntent)
                                }
                            },
                            shape = RoundedCornerShape(AppShape.ExtraLargeShape),
                            colors = ButtonDefaults.buttonColors(containerColor = Green500),
                            modifier = Modifier
                                .weight(0.4f)
                                .height(Dimens.HeightDefault)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Call,
                                    contentDescription = null,
                                    modifier = Modifier.size(Dimens.SizeML)
                                )

                                Spacer(modifier = Modifier.width(AppSpacing.MediumLarge))

                                Text(
                                    stringResource(id = R.string.contact),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }

                        AppButton(
                            text = stringResource(id = R.string.register_now),
                            enabled = isValid && isButtonEnable,
                            onClick = {
                                navController.navigate("register_donation/$eventId/$userId")
                            },
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF2F4F4))
                        .padding(paddingValues)
                        .padding(Dimens.PaddingM)
                ) {
                    item {
                        EventWelcomeBanner()
                    }

                    item { Spacer(modifier = Modifier.height(AppSpacing.LargePlus)) }

                    item {
                        EventInfoCard(eventId = event.id)
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
                                text = "Hosted by",
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
                }
            }
        }
    }
}

@Composable
fun EventWelcomeBanner() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(AppShape.ExtraLargeShape)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.welcome_banner_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.welcome_banner_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun isFutureTime(isoString: String): Boolean {
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val dateTime = LocalDateTime.parse(isoString, inputFormatter)
    val now = LocalDateTime.now()

    return dateTime.isAfter(now)
}