package com.example.heartbeat.presentation.features.users.staff.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.event.ui.StaffEventCard
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.AquaMint
import com.example.heartbeat.ui.theme.CoralRed
import com.example.heartbeat.ui.theme.GoldenGlow
import com.example.heartbeat.ui.theme.RoyalPurple
import com.example.heartbeat.ui.theme.SunsetOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StaffHomeScreen(
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val user = authState?.getOrNull()

    val events by eventViewModel.events.collectAsState(initial = emptyList())

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val sortedEvents = events.sortedByDescending { event ->
        LocalDate.parse(event.date, dateFormatter)
    }

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
        authViewModel.loadCurrentUser()
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

                Row(
                    modifier = Modifier
                        .padding(Dimens.PaddingL)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.welcome),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Small))

                        user?.let {
                            Text(
                                text = user.username ?: "User",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.padding(bottom = Dimens.PaddingS).size(Dimens.SizeM)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(top = Dimens.PaddingL)
                .padding(horizontal = Dimens.PaddingM)
        ) {
            itemsIndexed(events) { index, event ->
                LaunchedEffect(Unit) {
                    hospitalViewModel.loadHospitalById(hospitalId = event.locationId)
                }

                val hospital = hospitalViewModel.hospitalDetails[event.locationId]
                val location = "${hospital?.district}, ${hospital?.province}"

                val gradient = gradients[index % gradients.size]
                val accentColor = accentColors[index % accentColors.size]

                StaffEventCard(
                    gradient = gradient,
                    event = event,
                    accentColor = accentColor,
                    location = location
                )
            }
        }

//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM),
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = paddingValues.calculateTopPadding())
//                .padding(top = Dimens.PaddingL)
//                .padding(horizontal = Dimens.PaddingM)
//        ) {
//            // Test
//            val eventList = listOf(
//                Event(
//                    id = "event_001",
//                    locationId = "location_phenikaa",
//                    name = "Giọt Hồng Yêu Thương",
//                    description = "Chương trình hiến máu nhân đạo cho sinh viên & giảng viên tại ĐH Phenikaa.",
//                    date = "03/10/2025",
//                    time = "08:00 16:00",
//                    deadline = LocalDateTime(2025, 10, 5, 23, 59, 0),
//                    donorList = emptyList(),
//                    capacity = 200,
//                    donorCount = 75
//                ),
//                Event(
//                    id = "event_001",
//                    locationId = "location_phenikaa",
//                    name = "Giọt Hồng Yêu Thương",
//                    description = "Chương trình hiến máu nhân đạo cho sinh viên & giảng viên tại ĐH Phenikaa.",
//                    date = "02/10/2025",
//                    time = "08:00 18:00",
//                    deadline = LocalDateTime(2025, 10, 5, 23, 59, 0),
//                    donorList = emptyList(),
//                    capacity = 200,
//                    donorCount = 127
//                ),
//                Event(
//                    id = "event_001",
//                    locationId = "location_phenikaa",
//                    name = "Giọt Hồng Yêu Thương",
//                    description = "Chương trình hiến máu nhân đạo cho sinh viên & giảng viên tại ĐH Phenikaa.",
//                    date = "28/09/2025",
//                    time = "08:00 16:00",
//                    deadline = LocalDateTime(2025, 10, 5, 23, 59, 0),
//                    donorList = emptyList(),
//                    capacity = 200,
//                    donorCount = 190
//                )
//            )
//
//            val locationList = listOf(
//                "Hà Đông, Hà Nội",
//                "TP. Thái Nguyên",
//                "Hoàn Kiếm, Hà Nội"
//            )
//
//            itemsIndexed(eventList) { index, event ->
//                val gradient = gradients[index % gradients.size]
//                val accentColor = accentColors[index % accentColors.size]
//                val location = locationList[index]
//
//                EventCard(
//                    gradient = gradient,
//                    event = event,
//                    accentColor = accentColor,
//                    location = location
//                )
//            }
//        }
    }
}