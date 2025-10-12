package com.example.heartbeat.presentation.features.event.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel

@Composable
fun EventDetailScreen(
    eventId: String,
    navController: NavController,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.getEventById(eventId)
    }

    selectedEvent?.let { event ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = event.name
            )
        }
    }
}