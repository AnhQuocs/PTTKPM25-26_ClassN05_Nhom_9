package com.example.heartbeat.presentation.features.users.staff.ui.calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Title
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.event.ui.EventOutlinedTextField
import com.example.heartbeat.presentation.features.event.util.EventValidator
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import dagger.hilt.android.AndroidEntryPoint

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
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val hospitals = hospitalViewModel.hospitals

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

        val hospital = hospitalViewModel.hospitalDetails[eventId]

        LaunchedEffect(event) {
            hospitalViewModel.loadHospitalById(hospitalId = event.locationId)
        }

        var hospitalName by remember { mutableStateOf(hospital?.hospitalName ?: "") }
        var hospitalId by remember { mutableStateOf(hospital?.hospitalId ?: "") }

        LaunchedEffect(hospital) {
            hospital?.let {
                hospitalName = it.hospitalName
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

        val isButtonEnabled =
            nameError == null &&
                    desError == null &&
                    hosError == null &&
                    capacityError == null &&
                    startTime.isNotBlank() &&
                    endTime.isNotBlank()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(Dimens.PaddingM + 2.dp)
                .padding(top = Dimens.PaddingSM),
        ) {
            SettingTitle(
                text = "",
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
                title = stringResource(id = R.string.hospital),
                value = hospitalName,
                list = hospitals.map { it.hospitalName },
                onValueChange = { nameSelected ->
                    hospitalName = nameSelected
                    hospitalId = hospitals.firstOrNull { it.hospitalName == nameSelected }?.hospitalId ?: ""
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
        }
    }
}