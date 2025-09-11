package com.example.heartbeat.presentation.features.donor.ui.profile_setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed

fun defaultKeyboardOptions(type: KeyboardType = KeyboardType.Text) =
    KeyboardOptions(keyboardType = type)

@Composable
fun ProfileSetupTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    isTrailingIcon: Boolean = false,
    focusRequester: FocusRequester = FocusRequester(),
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    keyboardOptions: KeyboardOptions = defaultKeyboardOptions(),
    list: List<String> = emptyList(),
) {
    val focusManager = LocalFocusManager.current

    val heightTextField by remember { mutableStateOf(Dimens.HeightLarge - 8.dp) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val filteredList by remember(value, list) {
        derivedStateOf {
            if(value.isNotEmpty()) {
                list.filter { it.contains(value, ignoreCase = true) || it.contains("others", ignoreCase = true) }
                    .sorted()
            } else list.sorted()
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = Dimens.PaddingM)
            .padding(vertical = Dimens.PaddingSM)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                        expanded = true
                    },
                    placeholder = { Text(placeholder, fontSize = 15.sp) },
                    leadingIcon = leadingIcon?.let {
                        {
                            Icon(imageVector = it, contentDescription = null, tint = BloodRed)
                        }
                    },
                    trailingIcon = {
                        if(isTrailingIcon) {
                            IconButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Icon(
                                    if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(AppShape.MediumShape),
                    isError = isError,
                    keyboardOptions = keyboardOptions.copy(
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
                        .height(heightTextField)
                        .focusRequester(focusRequester)
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .background(color = Color.White)
                            .heightIn(max = 150.dp)
                    ) {
                        items(filteredList) { item ->
                            CategoryItems(title = item) { selected ->
                                onValueChange(selected)
                                expanded = false
                            }
                        }
                    }
                }
            }
        }

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
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp
        )
    }
}