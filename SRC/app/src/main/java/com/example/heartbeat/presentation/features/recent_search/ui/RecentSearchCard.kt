package com.example.heartbeat.presentation.features.recent_search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.search.viewmodel.UnifiedSearchViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import kotlinx.datetime.LocalDateTime

@Composable
fun RecentSearchCard(
    list: List<RecentSearch>,
    onClear: () -> Unit,
    isLoading: Boolean,
    hospitalViewModel: HospitalViewModel,
    unifiedSearchViewModel: UnifiedSearchViewModel,
    navController: NavController
) {
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
                text1 = stringResource(id = R.string.recent_search_title),
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
                        stringResource(id = R.string.no_recent),
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.PaddingL, bottom = Dimens.PaddingXL)
                    )
                } else {
                    list.take(3).forEach { recent ->
                        LaunchedEffect(Unit) {
                            hospitalViewModel.loadHospitalById(hospitalId = recent.subTitle)
                        }

                        val hospital =
                            hospitalViewModel.hospitalDetails[recent.subTitle]

                        RecentSearchItem(
                            recent = recent,
                            hospitalName = hospital?.hospitalName ?: "Loading...",
                            onClick = {
                                val fakeEvent = Event(
                                    id = recent.id,
                                    locationId = recent.subTitle,
                                    name = recent.title,
                                    description = "",
                                    date = "",
                                    time = "",
                                    deadline = null,
                                    donorList = emptyList(),
                                    capacity = 0,
                                    donorCount = 0,
                                    createdAt = LocalDateTime(1970, 1, 1, 0, 0),
                                    updatedAt = null
                                )
                                val suggestion = SearchSuggestionItem.EventSuggestion(fakeEvent)

                                unifiedSearchViewModel.onSuggestionClicked(suggestion)

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedTab", 1)
                                navController.navigate("event_detail/${suggestion.event.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentSearchItem(recent: RecentSearch, hospitalName: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = Dimens.PaddingSM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.History,
            contentDescription = null,
            tint = Color.Black.copy(0.8f),
            modifier = Modifier.size(Dimens.SizeM)
        )

        Spacer(modifier = Modifier.width(Dimens.PaddingS))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = recent.title,
                color = Color.Black.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp)
            )

            Text(
                text = hospitalName,
                color = Color(0xFF707070),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
            )
        }
    }
}