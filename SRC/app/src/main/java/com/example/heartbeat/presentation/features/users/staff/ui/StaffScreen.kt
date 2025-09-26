package com.example.heartbeat.presentation.features.users.staff.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.presentation.features.event.ui.EventForm
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun StaffScreen(
    modifier: Modifier = Modifier,
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val hospitals = hospitalViewModel.hospitals

    LaunchedEffect(Unit) {
        hospitalViewModel.loadHospitals()
        eventViewModel.getAllEvents()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.PaddingM),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Staff Screen")

            EventForm(
                list = hospitals
            )
        }
    }
}