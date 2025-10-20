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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun DatePickerFieldForEvent(
    modifier: Modifier = Modifier,
    title: String,
    placeholder: String = "",
    selectedDate: String,
    onDateChange: (String) -> Unit,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 14.sp,
    blockPastDates: Boolean = true
) {
    val today = remember { LocalDate.now() }
    val minDate = today.plusDays(10)
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(vertical = Dimens.PaddingXSPlus)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        OutlinedTextField(
            value = selectedDate,
            onValueChange = { onDateChange(selectedDate) },
            placeholder = {
                Text(text = placeholder, fontSize = 15.sp)
            },
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
                    modifier = Modifier.clickable { showDatePicker = true },
                )
            }
        )
    }

    if (showDatePicker) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val parsedDate = try {
            LocalDate.parse(selectedDate, formatter)
        } catch (e: Exception) {
            if (blockPastDates) minDate else today
        }

        val initialDateMillis = parsedDate.toEpochDay() * 24 * 60 * 60 * 1000

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    return if (blockPastDates) {
                        !date.isBefore(minDate)
                    } else {
                        !date.isAfter(today)
                    }
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