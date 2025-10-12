package com.example.heartbeat.presentation.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.search.viewmodel.UnifiedSearchViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    unifiedSearchViewModel: UnifiedSearchViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    navController: NavController
) {
    val query = unifiedSearchViewModel.query

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingM)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { unifiedSearchViewModel.onQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.PaddingSM),
            label = { Text("Search event, hospital or province") },
            singleLine = true
        )

        LazyColumn {
            items(unifiedSearchViewModel.suggestions) { suggestion ->
                when (suggestion) {
                    is SearchSuggestionItem.EventSuggestion -> {
                        LaunchedEffect(Unit) {
                            hospitalViewModel.loadHospitalById(hospitalId = suggestion.event.locationId)
                        }

                        val hospital =
                            hospitalViewModel.hospitalDetails[suggestion.event.locationId]

                        SearchSuggestionRow(
                            title = suggestion.event.name,
                            province = "${hospital?.province}",
                            subtitle = "${hospital?.hospitalName}"
                        ) {
                            unifiedSearchViewModel.onSuggestionClicked(suggestion)
                            navController.navigate("event_detail/${suggestion.event.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSuggestionRow(
    title: String,
    subtitle: String,
    province: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.PaddingM, vertical = Dimens.PaddingSM)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF00D09E),
                modifier = Modifier.size(Dimens.SizeSM)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Text(
                    text = province,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Divider(color = Color(0xFFE0E0E0))
    }
}