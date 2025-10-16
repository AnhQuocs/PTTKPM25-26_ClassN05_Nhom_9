package com.example.heartbeat.presentation.features.recent_viewed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.entity.recent_viewed.RecentViewed
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.Green500

@Composable
fun RecentViewedCard(
    list: List<RecentViewed>,
    onClear: () -> Unit,
    isLoading: Boolean,
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel,
    navController: NavController
) {
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(AppShape.LargeShape),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingM)
        ) {
            TitleSection(
                text1 = stringResource(id = R.string.recent_viewed_title),
                text2 = stringResource(id = R.string.clear_all),
                color = BloodRed,
                onClick = { onClear() }
            )

            if(isLoading) {
                Box(
                    modifier = Modifier
                        .height(Dimens.HeightXL2)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BloodRed)
                }
            } else {
                if(list.isEmpty()) {
                    Text(
                        stringResource(id = R.string.no_recent_viewed),
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.PaddingL, bottom = Dimens.PaddingXL)
                    )
                } else {
                    list.take(3).forEach { recent ->
                        LaunchedEffect(Unit) {
                            eventViewModel.getEventById(recent.id)
                        }

                        selectedEvent?.let { event ->
                            LaunchedEffect(Unit) {
                                hospitalViewModel.loadHospitalById(hospitalId = event.locationId)
                            }

                            val hospital = hospitalViewModel.hospitalDetails[event.locationId]

                            hospital?.let {
                                RecentViewedItem(
                                    event = event,
                                    hospital = it,
                                    onClick = {
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedTab", 1)
                                        navController.navigate("event_detail/${event.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentViewedItem(
    event: Event,
    hospital: Hospital,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(CompassionBlue, RoundedCornerShape(AppShape.LargeShape))
            .fillMaxWidth()
            .padding(Dimens.PaddingS)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.SizeXXLPlus)
                .clip(RoundedCornerShape(AppShape.LargeShape))
                .background(Color.White, RoundedCornerShape(AppShape.LargeShape)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = hospital.imgUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.SizeXXL)
                    .clip(RoundedCornerShape(AppShape.LargeShape))
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Large))

        Column {
            Text(
                text = event.name,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                color = Color.Black,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingXXS)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Green500,
                    modifier = Modifier.size(Dimens.SizeS)
                )

                Text(
                    text = hospital.hospitalName,
                    color = Color(0xFF707070),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}