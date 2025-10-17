package com.example.heartbeat.presentation.features.search.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.recent_search.ui.RecentSearchCard
import com.example.heartbeat.presentation.features.recent_search.viewmodel.RecentSearchViewModel
import com.example.heartbeat.presentation.features.recent_viewed.ui.RecentViewedCard
import com.example.heartbeat.presentation.features.recent_viewed.viewmodel.RecentViewedViewModel
import com.example.heartbeat.presentation.features.search.viewmodel.UnifiedSearchViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.PeachBackground

@Composable
fun SearchScreen(
    unifiedSearchViewModel: UnifiedSearchViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    recentSearchViewModel: RecentSearchViewModel = hiltViewModel(),
    recentViewedViewModel: RecentViewedViewModel = hiltViewModel(),
    navController: NavController
) {
    val query = unifiedSearchViewModel.query
    val recentSearchList = recentSearchViewModel.recentList
    val isRecentSearchLoading = recentSearchViewModel.isLoading

    val recentViewedList = recentViewedViewModel.recentList
    val isRecentViewedLoading = recentViewedViewModel.isLoading

    LaunchedEffect(Unit) {
        recentSearchViewModel.loadRecentSearch()
        recentViewedViewModel.loadRecentViewed()
    }

    Log.d("SearchScreen", "Recent Viewed size: ${recentViewedList.size}")

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.HeightXL2)
                    .background(PeachBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(id = R.string.search_events),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = Dimens.PaddingXL)
                        .fillMaxWidth()
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(color = Color(0xFFF2F4F4))
                .padding(paddingValues.calculateTopPadding())
                .padding(Dimens.PaddingM)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { unifiedSearchViewModel.onQueryChanged(it) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(AppShape.LargeShape),
                    singleLine = true,
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.SizeSM)
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(Dimens.SizeSM)
                                    .clickable { unifiedSearchViewModel.clearQuery() }
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            stringResource(id = R.string.search_placeholder),
                            fontSize = 13.sp,
                            lineHeight = 1.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.83f)
                        .height(Dimens.HeightLarge)
                        .padding(bottom = Dimens.PaddingSM)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(AppShape.LargeShape))
                        .background(Color.White)
                        .size(Dimens.HeightDefault)
                        .padding(bottom = Dimens.PaddingSM),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        modifier = Modifier
                            .size(Dimens.SizeL)
                            .padding(top = Dimens.PaddingSM)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .heightIn(max = Dimens.HeightXXL)
                    .clip(RoundedCornerShape(AppShape.LargeShape))
                    .background(Color.White)
            ) {
                items(unifiedSearchViewModel.suggestions) { suggestion ->
                    when (suggestion) {
                        is SearchSuggestionItem.EventSuggestion -> {
                            LaunchedEffect(Unit) {
                                hospitalViewModel.loadHospitalById(hospitalId = suggestion.event.locationId)
                            }

                            val hospital =
                                hospitalViewModel.hospitalDetails[suggestion.event.locationId]

                            SearchSuggestionRow(
                                date = suggestion.event.date,
                                title = suggestion.event.name,
                                province = "${hospital?.province}",
                                subtitle = "${hospital?.hospitalName}"
                            ) {
                                unifiedSearchViewModel.onSuggestionClicked(suggestion)
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedTab", 1)
                                navController.navigate("event_detail/${suggestion.event.id}")
                            }
                        }
                    }
                }
            }

            if (unifiedSearchViewModel.suggestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.PaddingM))
            }

            RecentSearchCard(
                list = recentSearchList,
                onClear = { recentSearchViewModel.clearRecentSearch() },
                isLoading = isRecentSearchLoading,
                hospitalViewModel = hospitalViewModel,
                unifiedSearchViewModel = unifiedSearchViewModel,
                navController = navController
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingSM))

            RecentViewedCard(
                list = recentViewedList,
                isLoading = isRecentViewedLoading,
                onClear = { recentViewedViewModel.clearRecentViewed() },
                hospitalViewModel = hospitalViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun SearchSuggestionRow(
    date: String,
    title: String,
    subtitle: String,
    province: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(start = Dimens.PaddingM, top = Dimens.PaddingSM, bottom = Dimens.PaddingS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_event),
            contentDescription = null,
            colorFilter = ColorFilter.tint(BloodRed),
            modifier = Modifier.size(Dimens.SizeM)
        )

        Spacer(modifier = Modifier.width(AppSpacing.Medium))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$title - ",
                    color = BloodRed,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = date,
                    color = Color(0xFF707070),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$subtitle - ",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.Gray,
                    lineHeight = 2.sp
                )

                Text(
                    text = province,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.Black,
                    lineHeight = 2.sp
                )
            }
        }
    }
}