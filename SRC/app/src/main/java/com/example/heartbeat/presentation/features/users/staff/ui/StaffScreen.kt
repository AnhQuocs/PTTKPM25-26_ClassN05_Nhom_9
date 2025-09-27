package com.example.heartbeat.presentation.features.users.staff.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.event.ui.EventForm
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.PinkLight

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
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) { }
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
}