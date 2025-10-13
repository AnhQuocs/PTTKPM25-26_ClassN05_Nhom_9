package com.example.heartbeat.presentation.features.event.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.PeachBackground

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
                            .clickable { navController.popBackStack() }
                    )

                    Text(
                        stringResource(id = R.string.search_events),
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
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF2F4F4))
                    .padding(paddingValues)
                    .padding(Dimens.PaddingM)
            ) {
                Text(
                    text = event.name
                )

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                 Text(
                    text = event.description
                )

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                 Text(
                    text = event.date
                )

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                 Text(
                    text = event.time
                )

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                 Text(
                    text = event.capacity.toString()
                )
            }
        }
    }
}