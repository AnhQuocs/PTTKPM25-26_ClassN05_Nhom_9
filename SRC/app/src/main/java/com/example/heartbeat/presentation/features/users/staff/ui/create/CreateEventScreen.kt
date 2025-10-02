package com.example.heartbeat.presentation.features.users.staff.ui.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.event.ui.EventForm
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.staff.component.StaffTopBar
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun CreateEventScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel()
) {
    val hospitals = hospitalViewModel.hospitals

    Scaffold(
        topBar = {
            StaffTopBar(
                text = stringResource(id = R.string.create_event)
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(color = Color(0xFFE3F2FD).copy(alpha = 0.4f))
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.PaddingM)
                        .background(color = Color.White, RoundedCornerShape(AppShape.ExtraExtraLargeShape))
                        .clip(RoundedCornerShape(AppShape.ExtraExtraLargeShape)),
                    contentAlignment = Alignment.Center
                ) {
                    EventForm(list = hospitals, modifier = Modifier.padding(Dimens.PaddingS))
                }
            }
        }
    }
}