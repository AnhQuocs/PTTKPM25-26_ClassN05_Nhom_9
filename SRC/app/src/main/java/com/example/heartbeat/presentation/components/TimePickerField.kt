package com.example.heartbeat.presentation.components

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AdsClick
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.PinkLight

@SuppressLint("DefaultLocale")
@Composable
fun TimePickerField(
    selectedTime: String,
    placeholder: String,
    onTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedTime,
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
        placeholder = { Text(placeholder, fontSize = 15.sp) },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        modifier = modifier
            .defaultMinSize(minHeight = Dimens.HeightLarge - 8.dp)
            .clickable { showTimePicker = true },
        trailingIcon = {
            Icon(
                Icons.Default.AccessTime,
                contentDescription = "",
                modifier = Modifier.clickable { showTimePicker = true }
            )
        }
    )

    if (showTimePicker) {
        val context = LocalContext.current
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val formatted = String.format("%02d:%02d", hour, minute)
                onTimeChange(formatted)
                showTimePicker = false
            },
            0,
            0,
            true
        ).apply {
            setOnDismissListener { showTimePicker = false }
        }.show()
    }
}