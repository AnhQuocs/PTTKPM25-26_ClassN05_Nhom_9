package com.example.heartbeat.presentation.features.event.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.donation.ui.register_detail.DonationDetailScreen
import com.example.heartbeat.presentation.features.event.ui.event_detail.EventDetailScreen
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.main.home.ProgressBar
import com.example.heartbeat.presentation.features.recent_viewed.viewmodel.RecentViewedViewModel
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.UnityPeachText
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class AllEventsActivity : BaseComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val donorIdFromHome = intent.getStringExtra("donorId") ?: ""

        setContent {
            val navController = rememberNavController()

            AnimatedNavHost(navController = navController, startDestination = "all_event") {
                composable("all_event") {
                    AllEventsScreen(
                        donorId = donorIdFromHome,
                        navController = navController,
                        onBackClick = { finish() }
                    )
                }

                composable(
                    route = "register_donation/{eventId}/{donorId}",
                    arguments = listOf(
                        navArgument("eventId") { type = NavType.StringType },
                        navArgument("donorId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId")
                    val donorId = backStackEntry.arguments?.getString("donorId")
                    DonationDetailScreen(
                        eventId = eventId ?: "",
                        donorId = donorId ?: "",
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun AllEventsScreen(
    donorId: String,
    eventViewModel: EventViewModel = hiltViewModel(),
    recentViewedViewModel: RecentViewedViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    navController: NavController,
    onBackClick: () -> Unit
) {
    val events by eventViewModel.events.collectAsState()

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    val now = LocalDateTime.now()

    val upcomingEvents = remember(events) {
        events.filter { event ->
            try {
                val eventDate = LocalDate.parse(event.date, dateFormatter)
                val startStr = event.time.split(" ")[0]
                val eventTime = LocalTime.parse(startStr, timeFormatter)
                val eventDateTime = LocalDateTime.of(eventDate, eventTime)

                eventDateTime.isAfter(now)
            } catch (e: Exception) {
                false
            }
        }.sortedBy { event ->
            val eventDate = LocalDate.parse(event.date, dateFormatter)
            val startStr = event.time.split(" ")[0]
            val eventTime = LocalTime.parse(startStr, timeFormatter)
            LocalDateTime.of(eventDate, eventTime)
        }
    }

    LaunchedEffect(events) {
        events.forEach { event ->
            hospitalViewModel.loadHospitalById(event.locationId)
            eventViewModel.observeDonorCount(event.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingM + 2.dp)
            .padding(top = Dimens.PaddingM),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        SettingTitle(
            text = stringResource(R.string.upcoming_event),
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
        ) {
            items(upcomingEvents) { event ->
                val (startStr, _) = event.time.split(" ")
                val startTime = LocalTime.parse(startStr, timeFormatter)

                val hospital = hospitalViewModel.hospitalDetails[event.locationId]

                Card(
                    modifier = Modifier
                        .border(0.1.dp, Color.LightGray, RoundedCornerShape(AppShape.ExtraExtraLargeShape))
                        .fillMaxWidth()
                        .height(Dimens.HeightXL3 + 10.dp)
                        .clickable {
                            recentViewedViewModel.addRecentViewed(id = event.id)
                            navController.navigate("register_donation/${event.id}/${donorId}")
                        },
                    shape = RoundedCornerShape(AppShape.ExtraExtraLargeShape),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = Dimens.PaddingM, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_blood),
                                contentDescription = null,
                                tint = BloodRed,
                                modifier = Modifier.size(Dimens.SizeSM)
                            )
                            Spacer(Modifier.width(AppSpacing.Small))
                            Text(
                                text = event.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BloodRed
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = UnityPeachText,
                                modifier = Modifier.size(Dimens.SizeSM)
                            )
                            Spacer(Modifier.width(AppSpacing.Small + 2.dp))
                            Text(
                                text = "${event.date} - $startTime",
                                style = MaterialTheme.typography.bodyMedium.copy(color = UnityPeachText)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_hospital),
                                contentDescription = null,
                                tint = CompassionBlueText,
                                modifier = Modifier.size(Dimens.SizeSM)
                            )
                            Spacer(Modifier.width(AppSpacing.Small + 2.dp))
                            Text(
                                text = hospital?.hospitalName ?: "Loading...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = CompassionBlueText)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        ProgressBar(value = event.donorCount, capacity = event.capacity)
                    }
                }
            }
        }
    }
}