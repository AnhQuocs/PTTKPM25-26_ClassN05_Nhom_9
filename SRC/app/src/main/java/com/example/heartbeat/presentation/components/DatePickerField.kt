@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.heartbeat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.PinkLight
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerField(
    selectedDate: String,
    onDateChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    val minDate = today.plusDays(10)

    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(vertical = Dimens.PaddingXSPlus)
    ) {
        Text(
            text = stringResource(id = R.string.date),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        OutlinedTextField(
            value = selectedDate,
            onValueChange = { onDateChange(selectedDate) },
            readOnly = true,
            shape = RoundedCornerShape(AppShape.ExtraLargeShape),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                unfocusedContainerColor = PinkLight.copy(alpha = 0.2f),
                focusedContainerColor = PinkLight.copy(alpha = 0.2f),
                errorContainerColor = PinkLight.copy(alpha = 0.2f)
            ),
            singleLine = true,
            modifier = Modifier
                .defaultMinSize(minHeight = Dimens.HeightLarge - 8.dp)
                .clickable { showDatePicker = true },
            leadingIcon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.5f)
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.AdsClick,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { showDatePicker = true },
                )
            }
        )
    }

    if (showDatePicker) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val parsedDate = try {
            LocalDate.parse(selectedDate, formatter)
        } catch (e: Exception) {
            minDate
        }

        val initialDateMillis = parsedDate.toEpochDay() * 24 * 60 * 60 * 1000

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return !date.isBefore(minDate)
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        val formatted = "%02d/%02d/%04d".format(
                            date.dayOfMonth,
                            date.monthValue,
                            date.year
                        )
                        onDateChange(formatted)
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}