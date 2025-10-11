package com.example.heartbeat.presentation.features.users.staff.ui.home

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlueLight
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.HopeGreenLight
import com.example.heartbeat.ui.theme.HopeGreenText
import com.example.heartbeat.ui.theme.PinkLight
import com.example.heartbeat.ui.theme.SunshineYellowLight
import com.example.heartbeat.ui.theme.SunshineYellowText
import com.example.heartbeat.ui.theme.UnityPeachText
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PendingListSection(
    donationViewModel: DonationViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by donationViewModel.uiState.collectAsState()
    val pendingList = uiState.donations.filter { it.status == "PENDING" }
    val groupedByEvent = pendingList.groupBy { it.eventId }
    val entries = groupedByEvent.entries.toList()

    val eventCache = remember { mutableStateMapOf<String, Event>() }

    if (uiState.isLoading) {
        CircularProgressIndicator(color = BloodRed)
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TitleSection(
            text1 = stringResource(id = R.string.pending_requests),
            text2 = stringResource(id = R.string.see_all),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Log.d("PendingListSection", "Size: ${entries.size}")

        if (entries.isEmpty()) {
            // EmptyPendingState()
        } else {
            AnimatedContent(
                targetState = entries.take(3).map { it.key },
                transitionSpec = {
                    (fadeIn() + slideInVertically(initialOffsetY = { it / 2 })) togetherWith
                            (fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }))
                },
                label = "PendingListTransition"
            ) { _ ->
                val currentEntries = entries.take(3)

                when (currentEntries.size) {
                    1 -> {
                        val (eventId, donations) = currentEntries[0]
                        EventFetcher(eventId, eventViewModel, eventCache) { event ->
                            EventPendingApprovalCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens.HeightXL)
                                    .animateContentSize(),
                                event = event,
                                pendingCount = donations.size,
                                navController = navController,
                                cardColor = CompassionBlueLight,
                                textColor = CompassionBlueText
                            )
                        }
                    }

                    2 -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimens.HeightXL3)
                                .animateContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                        ) {
                            val colorList = listOf(CompassionBlueLight, HopeGreenLight)
                            val textColorList = listOf(CompassionBlueText, HopeGreenText)

                            currentEntries.forEachIndexed { index, (eventId, donations) ->
                                val color = colorList[index]
                                val textColor = textColorList[index]

                                EventFetcher(eventId, eventViewModel, eventCache) { event ->
                                    EventPendingApprovalCard(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .animateContentSize(),
                                        event = event,
                                        pendingCount = donations.size,
                                        navController = navController,
                                        cardColor = color,
                                        textColor = textColor
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimens.HeightXL3 + 25.dp)
                                .animateContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                        ) {
                            currentEntries.getOrNull(0)?.let { (eventId, donations) ->
                                EventFetcher(eventId, eventViewModel, eventCache) { event ->
                                    EventPendingApprovalCard(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .animateContentSize(),
                                        event = event,
                                        pendingCount = donations.size,
                                        navController = navController,
                                        cardColor = CompassionBlueLight,
                                        textColor = CompassionBlueText
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                            ) {
                                val colorList = listOf(HopeGreenLight, SunshineYellowLight)
                                val textColorList = listOf(HopeGreenText, SunshineYellowText)

                                currentEntries.drop(1).take(2)
                                    .forEachIndexed { index, (eventId, donations) ->
                                        val color = colorList[index]
                                        val textColor = textColorList[index]

                                        EventFetcher(eventId, eventViewModel, eventCache) { event ->
                                            EventPendingApprovalCard(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f)
                                                    .animateContentSize(),
                                                event = event,
                                                pendingCount = donations.size,
                                                navController = navController,
                                                isCompact = true,
                                                cardColor = color,
                                                textColor = textColor
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
}

@Composable
private fun EventFetcher(
    eventId: String,
    eventViewModel: EventViewModel,
    eventCache: MutableMap<String, Event>,
    content: @Composable (Event) -> Unit
) {
    val event = eventCache[eventId]
    LaunchedEffect(eventId) {
        if (event == null) {
            val result = eventViewModel.getEventByIdDirect(eventId)
            eventCache[eventId] = result
        }
    }
    event?.let {
        content(it)
    }
}

@Composable
fun EventPendingApprovalCard(
    modifier: Modifier = Modifier,
    event: Event,
    pendingCount: Int,
    navController: NavController,
    isCompact: Boolean = false,
    cardColor: Color,
    textColor: Color
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val (startStr, endStr) = event.time.split(" ")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)
    val time = "$startTime - $endTime"

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(AppShape.MediumShape),
        modifier = modifier
            .clip(RoundedCornerShape(AppShape.MediumShape))
            .clickable { navController.navigate("staff_approve/${event.id}") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingS),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = event.name,
                color = textColor,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (isCompact) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        tint = textColor.copy(alpha = 0.7f),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(event.date, style = MaterialTheme.typography.bodySmall, color = textColor.copy(alpha = 0.7f))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$pendingCount donors pending",
                    color = textColor.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
                ) {
                    Text("Approve", fontSize = 12.sp)
                    Icon(
                        Icons.Default.ArrowRightAlt,
                        contentDescription = null
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = textColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(time, style = MaterialTheme.typography.bodySmall, color = textColor)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = textColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        event.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.8f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = textColor.copy(alpha = 0.8f),
                        modifier = Modifier
                            .align(Alignment.Top)
                            .size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "$pendingCount donors pending approval",
                        color = textColor.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                }

                Column {  }
            }
        }
    }
}