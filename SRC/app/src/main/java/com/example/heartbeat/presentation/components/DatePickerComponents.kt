package com.example.heartbeat.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDemo() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(Dimens.PaddingM)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        DatePickerCard(
            title = "Future Only",
            description = "Select dates from today onward.",
            date = selectedDate,
            buttonText = "Selected Date",
            onClick = { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                   val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                       .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    return validateDate(date, RestrictionType.NoPastDates)
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if(selectedMillis != null) {
                            val date = Instant.fromEpochMilliseconds(selectedMillis)
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                            if(!validateDate(date, RestrictionType.NoPastDates)) {
                                showError = true
                            } else {
                                selectedDate = date.toString()
                                showDatePicker = false
                                showError = false
                            }
                        }
                    }
                ) {
                    Text("Ok", color = Color(0xFF3F51B5))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel", color = Color(0xFF3F51B5))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF3F51B5)
            )
        ) {
            Column {
                DatePicker(
                    state = datePickerState,
                    headline = {
                        Text(
                            text = "Future Only",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF3F51B5),
                            modifier = Modifier.padding(Dimens.PaddingM)
                        )
                    },
                    modifier = Modifier
                )

                if(showError) {
                    Text(
                        "Selected date is not allowed. Please choose a valid date.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(Dimens.PaddingM)
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerCard(
    title: String,
    description: String,
    date: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppShape.ExtraExtraLargeShape))
            .border(
                1.dp,
                Color.Gray.copy(alpha = 0.2f),
                RoundedCornerShape(AppShape.ExtraExtraLargeShape)
            ),
        colors = CardDefaults.cardColors(
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.PaddingM)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BloodRed,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(AppShape.ExtraLargeShape)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 14.sp
                )
            }

            if (date.isNotEmpty()) {
                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                Text(
                    text = "Selected: $date",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }
    }
}

fun validateDate(date: LocalDate, restrictionType: RestrictionType): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    return when (restrictionType) {
        RestrictionType.NoPastDates -> date <= today
        else -> true
    }
}

enum class RestrictionType {
    None, NoPastDates
}