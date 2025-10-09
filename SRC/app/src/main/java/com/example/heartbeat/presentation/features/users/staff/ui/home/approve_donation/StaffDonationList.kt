package com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed

@Composable
fun StaffDonationList(
    donationViewModel: DonationViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by donationViewModel.uiState.collectAsState()
    val pendingList = uiState.donations.filter { it.status == "PENDING" }
    val groupedByEvent = pendingList.groupBy { it.eventId }

    // Map lưu các event đã load
    val eventCache = remember { mutableStateMapOf<String, Event>() }

    if (uiState.isLoading) {
        CircularProgressIndicator(color = BloodRed)
    } else {
        Column {
            Text("${pendingList.size} donors are pending approval")

            groupedByEvent.forEach { (eventId, donations) ->
                val event = eventCache[eventId]

                LaunchedEffect(eventId) {
                    if (event == null) {
                        val result = eventViewModel.getEventByIdDirect(eventId)
                        eventCache[eventId] = result
                    }
                }

                event?.let {
                    EventPendingApprovalCard(it, size = donations.size, navController = navController)
                }
            }
        }
    }
}

@Composable
fun EventPendingApprovalCard(event: Event, size: Int, navController: NavController) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(AppShape.MediumShape),
        modifier = Modifier
            .height(Dimens.HeightXL)
            .padding(vertical = Dimens.PaddingS)
            .clickable {
                navController.navigate("staff_approve/${event.id}")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.PaddingM, vertical = Dimens.PaddingS),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = event.name,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = "$size donors are pending approval"
            )
        }
    }
}