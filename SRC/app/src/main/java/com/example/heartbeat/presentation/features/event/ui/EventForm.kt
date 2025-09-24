package com.example.heartbeat.presentation.features.event.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import com.example.heartbeat.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun EventForm(
    modifier: Modifier = Modifier,
    list: List<String>
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedTime by remember { mutableStateOf(currentTime) }

    val titleFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }
    val deadlineFocusRequester = remember { FocusRequester() }
    val capacityFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        EventOutlinedTextField(
            title = stringResource(id = R.string.event_title),
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(id = R.string.enter_title),
            isTrailingIcon = false,
        )
    }
}