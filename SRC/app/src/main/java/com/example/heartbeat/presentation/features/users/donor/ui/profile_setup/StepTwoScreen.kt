package com.example.heartbeat.presentation.features.users.donor.ui.profile_setup

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoScreen(
    formState: DonorFormState,
    onUpdate: (dateOfBirth: String, age: Int, gender: String, willingToDonate: Boolean, about: String) -> Unit
) {
    var isWillingDonate by remember { mutableStateOf("") }

    val genderFocusRequester = remember { FocusRequester() }
    val willingToDonateFocusRequester = remember { FocusRequester() }
    val aboutFocusRequester = remember { FocusRequester() }

    val genderList = listOf(
        stringResource(id = R.string.male),
        stringResource(id = R.string.female)
    )

    val isWillingOption = listOf(
        stringResource(id = R.string.yes),
        stringResource(id = R.string.no)
    )

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var dobText by remember { mutableStateOf(formState.dateOfBirth) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.date_of_birth),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        OutlinedTextField(
            value = dobText,
            onValueChange = {},
            leadingIcon = {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = BloodRed)
            },
            trailingIcon = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier = Modifier.clickable { showDatePicker = true }
                )
            },
            readOnly = true,
            placeholder = { Text(stringResource(id = R.string.date_of_birth)) },
            textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingM)
                .clickable {
                    showDatePicker = true
                }
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                        return date <= today
                    }
                }
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .date
                                if (date > today) {
                                    showError = true
                                } else {
                                    dobText = "${date.dayOfMonth.toString().padStart(2, '0')}/" +
                                            "${date.monthNumber.toString().padStart(2, '0')}/" +
                                            "${date.year}"

                                    val age = calculateAge(dobText)
                                    onUpdate(
                                        dobText,
                                        age,
                                        formState.gender,
                                        formState.willingToDonate!!,
                                        formState.about
                                    )
                                    showDatePicker = false
                                    showError = false
                                }
                            }
                        }
                    ) { Text("OK", color = BloodRed) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(id = R.string.cancel), color = BloodRed)
                    }
                }
            ) {
                Column {
                    DatePicker(
                        state = datePickerState,
                        headline = {
                            Text(
                                text = stringResource(id = R.string.select_dob),
                                style = MaterialTheme.typography.titleLarge,
                                color = BloodRed,
                                modifier = Modifier.padding(Dimens.PaddingM)
                            )
                        },
                        modifier = Modifier
                    )

                    if (showError) {
                        Text(
                            stringResource(id = R.string.date_not_allowed),
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(Dimens.PaddingM)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        val age = if (formState.age != 0) formState.age else 0

        Text(
            text = stringResource(id = R.string.your_age) + " - " + age,
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(end = Dimens.PaddingM + 4.dp)
                .fillMaxWidth()
        )

        ProfileSetupTextField(
            title = stringResource(id = R.string.gender),
            value = formState.gender,
            onValueChange = {
                onUpdate(formState.dateOfBirth, formState.age, it, false, formState.about)
            },
            placeholder = stringResource(id = R.string.select_gender),
            leadingIcon = Icons.Default.Transgender,
            isTrailingIcon = true,
            focusRequester = genderFocusRequester,
            imeAction = ImeAction.Next,
            onImeAction = {
                willingToDonateFocusRequester.requestFocus()
            },
            list = genderList
        )

        ProfileSetupTextField(
            title = stringResource(id = R.string.want_donate_blood),
            value = isWillingDonate,
            onValueChange = { selected ->
                isWillingDonate = selected
                val willing = selected == isWillingOption[0]
                onUpdate(formState.dateOfBirth, formState.age, formState.gender, willing, formState.about)
            },
            placeholder = stringResource(id = R.string.want_donate_blood_placeholder),
            leadingIcon = Icons.Default.QuestionMark,
            isTrailingIcon = true,
            focusRequester = willingToDonateFocusRequester,
            imeAction = ImeAction.Next,
            onImeAction = {
                aboutFocusRequester.requestFocus()
            },
            list = isWillingOption
        )

        Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

        Text(
            text = stringResource(id = R.string.about_yourself),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        OutlinedTextField(
            value = formState.about,
            onValueChange = {
                onUpdate(formState.dateOfBirth, formState.age, formState.gender, formState.willingToDonate, it)
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.type_about_yourself)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingM)
                .height(Dimens.HeightXL2)
                .focusRequester(aboutFocusRequester)
        )
    }
}

fun calculateAge(dateString: String): Int {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dob = sdf.parse(dateString) ?: return 0

        val dobCalendar = Calendar.getInstance().apply { time = dob }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        age
    } catch (e: Exception) {
        0
    }
}