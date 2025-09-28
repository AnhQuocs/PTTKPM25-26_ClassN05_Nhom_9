package com.example.heartbeat.presentation.features.users.staff.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.presentation.components.BottomAppBar
import com.example.heartbeat.presentation.features.event.ui.EventCard
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.staff.ui.BottomBarItem
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.AquaMint
import com.example.heartbeat.ui.theme.CoralRed
import com.example.heartbeat.ui.theme.GoldenGlow
import com.example.heartbeat.ui.theme.RoyalPurple
import com.example.heartbeat.ui.theme.SunsetOrange
import kotlinx.datetime.LocalDateTime

@Composable
fun StaffHomeScreen(
    modifier: Modifier = Modifier,
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var previousTabIndex by remember { mutableIntStateOf(0) }

    val hospitals = hospitalViewModel.hospitals
    val events by eventViewModel.events.collectAsState(initial = emptyList())

    val gradients = listOf(
        Brush.linearGradient(
            colors = listOf(Color(0xFFEAEFFF), Color(0xFFFAF9FF))
        ),

        Brush.linearGradient(
            colors = listOf(Color(0xFFEDFFFB), Color(0xFFFFEBF4))
        ),

        Brush.linearGradient(
            colors = listOf(Color(0xFFFFFAF3), Color(0xFFECEFFF))
        ),

        Brush.linearGradient(
            colors = listOf(Color(0xFFEFFFF4), Color(0xFFFFFFEB))
        )
    )

    val accentColors = listOf(
        SunsetOrange,
        AquaMint,
        RoyalPurple,
        GoldenGlow,
        CoralRed
    )

    LaunchedEffect(Unit) {
        hospitalViewModel.loadHospitals()
        eventViewModel.getAllEvents()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.HeightXL2)
                    .background(
                        color = Color(0xFF2051E5),
                        RoundedCornerShape(
                            bottomStart = AppShape.SuperRoundedShape,
                            bottomEnd = AppShape.SuperRoundedShape
                        )
                    )
                    .clip(
                        RoundedCornerShape(
                            bottomStart = AppShape.SuperRoundedShape,
                            bottomEnd = AppShape.SuperRoundedShape
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bgr_top_bar),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                )

                Text(
                    text = stringResource(id = R.string.events),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(vertical = Dimens.PaddingM),
                )
            }
        }
    ) { paddingValues ->
//        LazyColumn {
//                itemsIndexed(events) { index, event ->
//                    val gradient = gradients[index % gradients.size]
//                    EventCard(
//                        gradient = gradient,
//                        event = event
//                    )
//                }
//            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(top = Dimens.PaddingL)
                    .padding(horizontal = Dimens.PaddingM)
            ) {
                val event = Event(
                    id = "event_001",
                    locationId = "location_phenikaa",
                    name = "Giọt Hồng Yêu Thương",
                    description = "Chương trình hiến máu nhân đạo cho sinh viên & giảng viên tại ĐH Phenikaa.",
                    date = "2025-10-05",
                    time = "08:00",
                    deadline = LocalDateTime(2025, 10, 5, 23, 59, 0),
                    donorList = emptyList(),
                    capacity = 200,
                    donorCount = 75
                )

                items(6) { index ->
                    val gradient = gradients[index % gradients.size]
                    val accentColor = accentColors[index % accentColors.size]
                    EventCard(
                        gradient = gradient,
                        event = event,
                        accentColor = accentColor
                    )
                }
            }
    }
}

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = Dimens.PaddingM),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Staff Screen")
//
//            EventForm(
//                list = hospitals
//            )
//        }
//    }