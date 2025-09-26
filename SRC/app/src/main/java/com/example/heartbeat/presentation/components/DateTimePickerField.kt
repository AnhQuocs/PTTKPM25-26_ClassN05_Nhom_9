package com.example.heartbeat.presentation.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.PinkLight
import java.util.Calendar

@SuppressLint("DefaultLocale")
@Composable
fun DateTimePickerField(
    modifier: Modifier = Modifier,
    selectedDateTime: String,
    onDateTimeChange: (String) -> Unit,
    placeholder: String
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDateTime,
        onValueChange = {},
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
        placeholder = { Text(placeholder) },
        modifier = modifier
            .defaultMinSize(minHeight = Dimens.HeightLarge - 8.dp)
            .clickable { showDatePicker = true },
        trailingIcon = {
            Icon(
                Icons.Default.AccessTime,
                contentDescription = "",
                modifier = Modifier.clickable { showDatePicker = true }
            )
        }
    )

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val formatted = String.format(
                            "%02d/%02d/%04d %02d:%02d",
                            day, month + 1, year, hour, minute
                        )
                        onDateTimeChange(formatted)
                        showDatePicker = false
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false }
        }.show()
    }
}