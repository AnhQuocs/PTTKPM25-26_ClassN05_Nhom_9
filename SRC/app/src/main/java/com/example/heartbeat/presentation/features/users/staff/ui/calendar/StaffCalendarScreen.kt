package com.example.heartbeat.presentation.features.users.staff.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun StaffCalendarScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.PaddingM).padding(top = Dimens.PaddingM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarSection()
    }
}