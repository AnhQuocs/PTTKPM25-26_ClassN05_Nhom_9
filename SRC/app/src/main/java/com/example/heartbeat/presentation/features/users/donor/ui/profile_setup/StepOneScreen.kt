package com.example.heartbeat.presentation.features.users.donor.ui.profile_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.presentation.features.system.province.viewmodel.ProvinceViewModel

@Composable
fun StepOneScreen(
    formState: DonorFormState,
    onUpdate: (name: String, phoneNumber: String, bloodGroup: String, city: String) -> Unit,
    provinceViewModel: ProvinceViewModel = hiltViewModel()
) {
    val yourNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }
    val bloodGroupFocusRequester = remember { FocusRequester() }
    val cityFocusRequester = remember { FocusRequester() }

    val bloodList = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val provinces by provinceViewModel.provinces.collectAsState()
    val provinceNames: List<String> = provinces.map { it.name }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(bottom = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileSetupTextField(
            title = stringResource(id = R.string.your_full_name),
            value = formState.name,
            onValueChange = {
                onUpdate(it, formState.phoneNumber, formState.bloodGroup, formState.city)
            },
            placeholder = stringResource(id = R.string.your_full_name),
            leadingIcon = Icons.Default.Person,
            focusRequester = yourNameFocusRequester,
            imeAction = ImeAction.Next,
            onImeAction = {
                phoneNumberFocusRequester.requestFocus()
            }
        )

        ProfileSetupTextField(
            title = stringResource(id = R.string.phone_number),
            value = formState.phoneNumber,
            onValueChange = {
                onUpdate(formState.name, it, formState.bloodGroup, formState.city)
            },
            placeholder = stringResource(id = R.string.phone_number),
            leadingIcon = Icons.Default.Phone,
            focusRequester = phoneNumberFocusRequester,
            imeAction = ImeAction.Next,
            onImeAction = {
                bloodGroupFocusRequester.requestFocus()
            },
            keyboardOptions = defaultKeyboardOptions(KeyboardType.Phone)
        )

        ProfileSetupTextField(
            title = stringResource(id = R.string.blood_group),
            value = formState.bloodGroup,
            onValueChange = {
                onUpdate(formState.name, formState.phoneNumber, it, formState.city)
            },
            placeholder = stringResource(id = R.string.select_group),
            leadingIcon = Icons.Default.Bloodtype,
            isTrailingIcon = true,
            focusRequester = bloodGroupFocusRequester,
            imeAction = ImeAction.Next,
            onImeAction = {
                cityFocusRequester.requestFocus()
            },
            list = bloodList
        )

        ProfileSetupTextField(
            title = stringResource(id = R.string.city),
            value = formState.city,
            onValueChange = {
                onUpdate(formState.name, formState.phoneNumber, formState.bloodGroup, it)
            },
            placeholder = stringResource(id = R.string.select_city),
            leadingIcon = Icons.Default.LocationOn,
            isTrailingIcon = true,
            focusRequester = cityFocusRequester,
            imeAction = ImeAction.Done,
            list = provinceNames
        )
    }
}