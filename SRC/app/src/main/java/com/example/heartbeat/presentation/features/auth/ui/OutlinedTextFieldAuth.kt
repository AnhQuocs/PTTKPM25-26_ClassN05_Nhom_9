package com.example.heartbeat.presentation.features.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed

@Composable
fun OutlinedTextFieldAuth(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector? = null,
    focusRequester: FocusRequester = FocusRequester(),
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Column {
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
            singleLine = true,
            shape = RoundedCornerShape(AppShape.MediumShape),
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if(imeAction == ImeAction.Next) onImeAction()
                },
                onDone = {
                    if(imeAction == ImeAction.Done) focusManager.clearFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        if(errorMessage != null && isError) {
            Text(
                text = errorMessage,
                color = BloodRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = Dimens.PaddingM,
                    top = Dimens.PaddingXXS
                )
            )
        }
    }
}

@Composable
fun PasswordOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    focusRequester: FocusRequester = FocusRequester(),
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isShowPassword by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true,
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (imeAction == ImeAction.Done) focusManager.clearFocus()
                    onImeAction()
                },
                onNext = {
                    if (imeAction == ImeAction.Next) onImeAction()
                }
            ),
            visualTransformation = if(!isShowPassword) PasswordVisualTransformation() else VisualTransformation.None,
            isError = isError,
            shape = RoundedCornerShape(AppShape.MediumShape),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = BloodRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = Dimens.PaddingM,
                    top = Dimens.PaddingXXS
                )
            )
        }
    }
}