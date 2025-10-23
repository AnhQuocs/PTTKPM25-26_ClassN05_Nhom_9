package com.example.heartbeat.presentation.features.users.staff.ui.calendar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.presentation.components.DatePickerFieldForEvent
import com.example.heartbeat.presentation.components.DeadlinePickerField
import com.example.heartbeat.presentation.components.TimePickerField
import com.example.heartbeat.presentation.features.event.ui.EventOutlinedTextField
import com.example.heartbeat.presentation.features.event.ui.defaultKeyboardOptions
import com.example.heartbeat.presentation.features.event.util.EventValidator
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.FacebookBlue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EditEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val eventId = intent.getStringExtra("eventId") ?: ""

        setContent {
            EditEventScreen(eventId, onBackClick = { finish() })
        }
    }
}

@Composable
fun EditEventScreen(
    eventId: String,
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    var isShowDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val hospitals = hospitalViewModel.hospitals
    val isLoading by hospitalViewModel.isLoading

    LaunchedEffect(Unit) {
        hospitalViewModel.loadHospitals()
    }

    LaunchedEffect(eventId) {
        eventViewModel.getEventById(eventId)
    }

    selectedEvent?.let { event ->
        var name by remember { mutableStateOf(event.name) }
        var description by remember { mutableStateOf(event.description) }
        var date by remember { mutableStateOf(event.date) }

        val times = event.time.split(" ")
        var startTime by remember { mutableStateOf(times.getOrNull(0) ?: "08:00") }
        var endTime by remember { mutableStateOf(times.getOrNull(1) ?: "16:00") }

        var deadline by remember { mutableStateOf(event.deadline) }
        var capacity by remember { mutableStateOf(event.capacity.toString()) }

        var nameTouched by remember { mutableStateOf(false) }
        var desTouched by remember { mutableStateOf(false) }
        var hosTouched by remember { mutableStateOf(false) }
        var capacityTouched by remember { mutableStateOf(false) }

        val hospital = hospitalViewModel.hospitalDetails[event.locationId]
        var hospitalName by remember { mutableStateOf("") }
        var hospitalId by remember { mutableStateOf("") }

        LaunchedEffect(event) {
            hospitalViewModel.loadHospitalById(event.locationId)
        }

        LaunchedEffect(hospital) {
            hospital?.let {
                hospitalName = it.hospitalName
                hospitalId = it.hospitalId
            }
        }

        val nameError = if (nameTouched) {
            EventValidator.isValidEventText(
                text = name,
                maxWords = 10,
                emptyError = R.string.validate_title_empty,
                tooLongError = R.string.validate_title_too_long
            )
        } else null

        val desError = if (desTouched) {
            EventValidator.isValidEventText(
                text = description,
                maxWords = 20,
                emptyError = R.string.validate_description_empty,
                tooLongError = R.string.validate_description_too_long
            )
        } else null

        val hosError = if (hosTouched) {
            EventValidator.isValidEventText(
                text = hospitalName,
                maxWords = 50,
                emptyError = R.string.validate_hospital_empty,
                tooLongError = 0
            )
        } else null

        val capacityError = if (capacityTouched) {
            EventValidator.validateCapacity(
                capacity = capacity
            )
        } else null

        val hasChanged =
            name != event.name ||
                    description != event.description ||
                    date != event.date ||
                    startTime != event.time.split(" ").getOrNull(0) ||
                    endTime != event.time.split(" ").getOrNull(1) ||
                    deadline != event.deadline ||
                    hospitalId != event.locationId ||
                    capacity.toIntOrNull() != event.capacity

        val isButtonEnabled =
            hasChanged &&
                    nameError == null &&
                    desError == null &&
                    hosError == null &&
                    capacityError == null &&
                    startTime.isNotBlank() &&
                    endTime.isNotBlank()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(Dimens.PaddingM + 2.dp)
                    .padding(top = Dimens.PaddingSM),
            ) {
                SettingTitle(
                    text = stringResource(id = R.string.edit_event),
                    onClick = onBackClick
                )

                Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))

                EventOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.event_title),
                    value = name,
                    onValueChange = {
                        name = it
                        if (!nameTouched) nameTouched = true
                    },
                    placeholder = stringResource(id = R.string.enter_title),
                    leadingIcon = Icons.Default.Title,
                    isTrailingIcon = false,
                    isError = nameError != null,
                    errorMessage = nameError?.let { stringResource(id = it) } ?: "",
                    imeAction = ImeAction.Next
                )

                EventOutlinedTextField(
                    title = stringResource(id = R.string.description),
                    value = description,
                    onValueChange = {
                        description = it
                        if (!desTouched) desTouched = true
                    },
                    placeholder = stringResource(id = R.string.enter_description),
                    leadingIcon = Icons.Default.Description,
                    isTrailingIcon = false,
                    isError = desError != null,
                    isSingleLine = false,
                    errorMessage = desError?.let { stringResource(id = it) } ?: "",
                    imeAction = ImeAction.Next,
                    modifier = Modifier.fillMaxWidth()
                )

                EventOutlinedTextField(
                    title = stringResource(id = R.string.hospital),
                    value = hospitalName,
                    list = hospitals.map { it.hospitalName },
                    onValueChange = { nameSelected ->
                        hospitalName = nameSelected
                        hospitalId =
                            hospitals.firstOrNull { it.hospitalName == nameSelected }?.hospitalId ?: ""
                        if (!hosTouched) hosTouched = true
                    },
                    placeholder = stringResource(id = R.string.select_hospital),
                    leadingIcon = Icons.Default.LocalHospital,
                    isTrailingIcon = true,
                    isError = hosError != null,
                    errorMessage = hosError?.let { stringResource(id = it) } ?: "",
                    imeAction = ImeAction.Next,
                    modifier = Modifier.fillMaxWidth()
                )

                EventOutlinedTextField(
                    title = stringResource(id = R.string.capacity),
                    value = capacity,
                    onValueChange = {
                        capacity = it
                        if (!capacityTouched) capacityTouched = true
                    },
                    placeholder = stringResource(id = R.string.enter_capacity),
                    leadingIcon = Icons.Default.PeopleAlt,
                    isError = capacityError != null,
                    errorMessage = capacityError?.let { stringResource(id = it) } ?: "",
                    imeAction = ImeAction.Done,
                    keyboardOptions = defaultKeyboardOptions(
                        KeyboardType.Number
                    )
                )

                DatePickerFieldForEvent(
                    title = stringResource(id = R.string.date),
                    selectedDate = date,
                    onDateChange = { newDate -> date = newDate },
                )

                Text(
                    text = stringResource(id = R.string.time),
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TimePickerField(
                        selectedTime = startTime,
                        onTimeChange = { startTime = it },
                        placeholder = stringResource(id = R.string.start_time),
                        modifier = Modifier.weight(0.8f)
                    )

                    TimePickerField(
                        selectedTime = endTime,
                        onTimeChange = { endTime = it },
                        placeholder = stringResource(id = R.string.finish_time),
                        modifier = Modifier.weight(0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.MediumPlus))

                Text(
                    text = stringResource(id = R.string.deadline),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(AppSpacing.Small))

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                DeadlinePickerField(
                    selectedDateTime = deadline?.toJavaLocalDateTime()?.format(formatter) ?: "",
                    onDateTimeChange = { newDeadline ->
                        deadline = try {
                            val parsed = java.time.LocalDateTime.parse(newDeadline, formatter)
                            kotlinx.datetime.LocalDateTime(
                                parsed.year, parsed.monthValue, parsed.dayOfMonth,
                                parsed.hour, parsed.minute, parsed.second, parsed.nano
                            )
                        } catch (e: Exception) {
                            null
                        }
                    },
                    placeholder = stringResource(id = R.string.select_deadline),
                    eventDate = date,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            isShowDialog = true
                        },
                        shape = RoundedCornerShape(AppShape.LargeShape),
                        border = BorderStroke(1.dp, BloodRed),
                        modifier = Modifier
                            .height(Dimens.HeightDefault)
                            .weight(0.3f)
                    ) {
                        Text(text = stringResource(id = R.string.delete), color = BloodRed)
                    }

                    Spacer(modifier = Modifier.width(AppSpacing.MediumLarge))

                    Button(
                        onClick = {
                            val time = "$startTime $endTime"
                            val now =
                                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                            val newEvent = Event(
                                id = eventId,
                                locationId = hospitalId,
                                name = name,
                                description = description,
                                date = date,
                                time = time,
                                deadline = deadline,
                                donorList = event.donorList,
                                capacity = capacity.toInt(),
                                donorCount = event.donorCount,
                                createdAt = event.createdAt,
                                updatedAt = now
                            )

                            eventViewModel.updateEvent(eventId, newEvent)

                            Toast.makeText(
                                context,
                                context.getString(R.string.update_success),
                                Toast.LENGTH_SHORT
                            ).show()

                            onBackClick()
                        },
                        shape = RoundedCornerShape(AppShape.LargeShape),
                        enabled = isButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue),
                        modifier = Modifier
                            .height(Dimens.HeightDefault)
                            .weight(0.55f)
                    ) {
                        Text(text = stringResource(id = R.string.submit), color = Color.White)
                    }
                }

                if (isShowDialog) {
                    DeleteEventDialog(
                        onDismiss = { isShowDialog = false },
                        onConfirm = {
                            eventViewModel.deleteEvent(eventId)
                            onBackClick()
                        }
                    )
                }
            }

            if(isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BloodRed)
                }
            }
        }
    }
}

@Composable
fun DeleteEventDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.delete),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.confirm_delete_event),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    Toast.makeText(
                        context,
                        context.getString(R.string.delete_success),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .padding(horizontal = Dimens.PaddingS)
                    .height(40.dp),
                shape = RoundedCornerShape(AppShape.ExtraLargeShape)
            ) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = RoundedCornerShape(AppShape.ExtraExtraLargeShape)
    )
}