package com.example.heartbeat.presentation.features.event.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.components.DatePickerField
import com.example.heartbeat.presentation.components.DeadlinePickerField
import com.example.heartbeat.presentation.components.TimePickerField
import com.example.heartbeat.presentation.features.event.util.EventValidator
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun EventForm(
    modifier: Modifier = Modifier,
    list: List<Hospital>,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hospitalName by remember { mutableStateOf("") }
    var hospitalId by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val currentDate = LocalDate.now().plusDays(10)
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    var selectedDate by remember { mutableStateOf(currentDate) }

    var selectedStartTime by remember { mutableStateOf("") }
    var selectedFinishTime by remember { mutableStateOf("") }

    val nameFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }
    val hospitalFocusRequester = remember { FocusRequester() }
    val capacityFocusRequester = remember { FocusRequester() }

    var nameTouched by remember { mutableStateOf(false) }
    var desTouched by remember { mutableStateOf(false) }
    var hosTouched by remember { mutableStateOf(false) }
    var capacityTouched by remember { mutableStateOf(false) }

    val nameError = if (nameTouched) {
        EventValidator.isValidEventText(
            text = name,
            maxWords = 10,
            emptyError = R.string.validate_title_empty,
            tooLongError = R.string.validate_title_too_long
        )
    } else null

    val desError = if(desTouched) {
        EventValidator.isValidEventText(
            text = description,
            maxWords = 20,
            emptyError = R.string.validate_description_empty,
            tooLongError = R.string.validate_description_too_long
        )
    } else null

    val hosError = if(hosTouched) {
        EventValidator.isValidEventText(
            text = hospitalName,
            maxWords = 50,
            emptyError = R.string.validate_hospital_empty,
            tooLongError = 0
        )
    } else null

    val capacityError = if(capacityTouched) {
        EventValidator.validateCapacity(
            capacity = capacity
        )
    } else null

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        EventOutlinedTextField(
            title = stringResource(id = R.string.event_title),
            value = name,
            onValueChange = {
                name = it
                if (!nameTouched) nameTouched = true
            },
            placeholder = stringResource(id = R.string.enter_title),
            isTrailingIcon = false,
            focusRequester = nameFocusRequester,
            isError = nameError != null,
            errorMessage = nameError?.let { stringResource(id = it) } ?: "",
            imeAction = ImeAction.Next,
            onImeAction = {
                descriptionFocusRequester.requestFocus()
            },
            modifier = Modifier.fillMaxWidth()
        )

        EventOutlinedTextField(
            title = stringResource(id = R.string.description),
            value = description,
            onValueChange = {
                description = it
                if(!desTouched) desTouched = true
            },
            placeholder = stringResource(id = R.string.enter_description),
            isTrailingIcon = false,
            focusRequester = descriptionFocusRequester,
            isError = desError != null,
            isSingleLine = false,
            errorMessage = desError?.let { stringResource(id = it) } ?: "",
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )

        EventOutlinedTextField(
            title = stringResource(id = R.string.hospital),
            value = hospitalName,
            list = list.map { it.hospitalName },
            onValueChange = { nameSelected ->
                hospitalName = nameSelected
                hospitalId = list.firstOrNull { it.hospitalName == nameSelected }?.hospitalId ?: ""
                if(!hosTouched) hosTouched = true
            },
            placeholder = stringResource(id = R.string.select_hospital),
            isTrailingIcon = true,
            focusRequester = hospitalFocusRequester,
            isError = hosError != null,
            errorMessage = hosError?.let { stringResource(id = it) } ?: "",
            imeAction = ImeAction.Next,
            onImeAction = {
                capacityFocusRequester.requestFocus()
            },
            modifier = Modifier.fillMaxWidth()
        )

        EventOutlinedTextField(
            title = stringResource(id = R.string.capacity),
            value = capacity,
            onValueChange = {
                capacity = it
                if(!capacityTouched) capacityTouched = true
            },
            placeholder = stringResource(id = R.string.enter_capacity),
            focusRequester = capacityFocusRequester,
            isError = capacityError != null,
            errorMessage = capacityError?.let { stringResource(id = it) } ?: "",
            imeAction = ImeAction.Done,
            keyboardOptions = defaultKeyboardOptions(
                KeyboardType.Number
            )
        )

        DatePickerField(
            selectedDate = selectedDate,
            onDateChange = { newDate -> selectedDate = newDate },
        )

        Text(
            text = stringResource(id = R.string.time),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TimePickerField(
                selectedTime = selectedStartTime,
                onTimeChange = { selectedStartTime = it },
                placeholder = stringResource(id= R.string.start_time),
                modifier = Modifier.weight(0.8f)
            )

            TimePickerField(
                selectedTime = selectedFinishTime,
                onTimeChange = { selectedFinishTime = it },
                placeholder = stringResource(id= R.string.finish_time),
                modifier = Modifier.weight(0.8f)
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.MediumPlus))

        Text(
            text = stringResource(id = R.string.deadline),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        DeadlinePickerField(
            selectedDateTime = deadline,
            onDateTimeChange = { deadline = it },
            placeholder = stringResource(id = R.string.select_deadline),
            eventDate = selectedDate,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.MediumPlus))

        AppButton(
            onClick = {
                val time = "$selectedStartTime $selectedFinishTime"

                val event = Event(
                    id = UUID.randomUUID().toString(),
                    locationId = hospitalId,
                    name = name,
                    description = description,
                    date = selectedDate,
                    time = time,
                    deadline = if (deadline.isNotBlank()) deadline.toLocalDateTime() else null,
                    donorList = emptyList(),
                    capacity = capacity.toIntOrNull() ?: 0,
                    donorCount = 0
                )

//                eventViewModel.addEvent(event)

                Log.d("EventForm", "Time: $time")
            },
            text = stringResource(id = R.string.submit)
        )
    }
}