package com.example.heartbeat.presentation.features.donor.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed

@Composable
fun StepTwoScreen(
    formState: DonorFormState,
    onUpdate: (dateOfBirth: String, age: Int, gender: String, willingToDonate: Boolean, about: String) -> Unit
) {
    var isWillingDonate by remember { mutableStateOf("") }

    val dateOfBirthFocusRequester = remember { FocusRequester() }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Date
        Text(
            text = stringResource(id = R.string.date_of_birth),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        OutlinedTextField(
            value = formState.dateOfBirth,
            onValueChange = {
                onUpdate(it, 19, formState.gender, false, formState.about)
            },
            leadingIcon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = BloodRed
                )
            },
            placeholder = { Text(stringResource(id = R.string.date_of_birth)) },
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.PaddingM)
        )

        ProfileSetupTextField(
            title = stringResource(id = R.string.gender),
            value = formState.gender,
            onValueChange = {
                onUpdate(formState.dateOfBirth, formState.age, it, false, formState.about)
            },
            placeholder = stringResource(id = R.string.select_group),
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