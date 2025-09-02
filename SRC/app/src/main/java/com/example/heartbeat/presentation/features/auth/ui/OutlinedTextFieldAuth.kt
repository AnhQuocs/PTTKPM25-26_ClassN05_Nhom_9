package com.example.heartbeat.presentation.features.auth.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.theme.BloodRed

@Composable
fun OutlinedTextFieldAuth(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = icon?.let {
            {
                Icon(imageVector = it, contentDescription = null, tint = BloodRed)
            }
        },
//        colors = outlinedTextFieldColors(),
        shape = RoundedCornerShape(AppShape.MediumShape),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun PasswordOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector? = null,
) {
    var isShowPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(imageVector = it, contentDescription = null, tint = BloodRed)
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { isShowPassword = !isShowPassword }
            ) {
                Icon(
                    if(isShowPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    tint = BloodRed
                )
            }
        },
        visualTransformation = if(!isShowPassword) PasswordVisualTransformation() else VisualTransformation.None,
//        colors = outlinedTextFieldColors(),
        shape = RoundedCornerShape(AppShape.MediumShape),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun outlinedTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.White,
        cursorColor = Color.White
    )
}