package com.example.heartbeat.presentation.features.main.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.presentation.features.event.ui.AllEventsActivity
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.recent_viewed.viewmodel.RecentViewedViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.UnityPeachText
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun UpcomingEventCard(
    context: Context,
    donorId: String,
    events: List<Event>,
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    recentViewedViewModel: RecentViewedViewModel = hiltViewModel(),
    navController: NavController
) {
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
            .padding(horizontal = Dimens.PaddingM, vertical = Dimens.PaddingS),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        TitleSection(
            text1 = stringResource(id = R.string.upcoming_event),
            text2 = stringResource(id = R.string.see_all),
            onClick = {
                val intent = Intent(context, AllEventsActivity::class.java)
                    .putExtra("donorId", donorId)
                context.startActivity(intent)
            }
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)) {
            items(upcomingEvents.take(3)) { event ->
                val (startStr, _) = event.time.split(" ")
                val startTime = LocalTime.parse(startStr, timeFormatter)

                val hospital = hospitalViewModel.hospitalDetails[event.locationId]

                Card(
                    modifier = Modifier
                        .border(0.1.dp, Color.LightGray, RoundedCornerShape(AppShape.ExtraExtraLargeShape))
                        .height(Dimens.HeightXL3 + 5.dp)
                        .aspectRatio(2f)
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