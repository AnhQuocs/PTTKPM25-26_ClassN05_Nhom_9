package com.example.heartbeat.presentation.features.system.setting

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.system.province.viewmodel.ProvinceViewModel
import com.example.heartbeat.presentation.features.users.donor.ui.profile_setup.CategoryItems
import com.example.heartbeat.presentation.features.users.donor.ui.profile_setup.calculateAge
import com.example.heartbeat.presentation.features.users.donor.ui.profile_setup.defaultKeyboardOptions
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@AndroidEntryPoint
class PersonalInformationActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val donorId = intent.getStringExtra("donorId")

        setContent {
            donorId?.let { PersonalInformationScreen(donorId = it, onBackClick = { finish() }) }
        }
    }
}

@Composable
fun PersonalInformationScreen(
    donorId: String,
    onBackClick: () -> Unit,
    donorViewModel: DonorViewModel = hiltViewModel(),
    provinceViewModel: ProvinceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val successMessage = stringResource(id = R.string.update_success)

    val formState by donorViewModel.formState.collectAsState()
    val originalDonor by donorViewModel.originalDonor.collectAsState()
    val isLoading by donorViewModel.isLoading.collectAsState()

    val provinces by provinceViewModel.provinces.collectAsState()
    val provinceNames: List<String> = provinces.map { it.name }
    val selectedProvince by provinceViewModel.selectedProvince.collectAsState()
    var selectedCityName by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("") }

    val bloodList = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    val isModified by remember(formState, originalDonor) {
        derivedStateOf {
            originalDonor != null && (
                    formState.name != originalDonor!!.name ||
                            formState.phoneNumber != originalDonor!!.phoneNumber ||
                            formState.bloodGroup != originalDonor!!.bloodGroup ||
                            formState.cityId != originalDonor!!.cityId ||
                            formState.dateOfBirth != originalDonor!!.dateOfBirth ||
                            formState.about != originalDonor!!.about
                    )
        }
    }

    val isUnder18 by remember(formState.dateOfBirth) {
        derivedStateOf {
            if (formState.dateOfBirth.isEmpty()) return@derivedStateOf false
            try {
                val parts = formState.dateOfBirth.split("/")
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()

                val dob = LocalDate(year, month, day)
                val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                val age = today.year - dob.year -
                        if (today < dob + DatePeriod(years = today.year - dob.year)) 1 else 0

                age < 18
            } catch (e: Exception) {
                false
            }
        }
    }

    LaunchedEffect(donorId) {
        donorViewModel.getDonorById(donorId)
    }

    LaunchedEffect(formState.cityId) {
        if (formState.cityId.isNotEmpty()) {
            provinceViewModel.loadProvinceById(formState.cityId)
        }
    }

    LaunchedEffect(selectedProvince) {
        selectedProvince?.let { province ->
            selectedCityName = province.name
        }
    }

    LaunchedEffect(formState.bloodGroup) {
        selectedBloodGroup = formState.bloodGroup
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingM)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
        ) {
            SettingTitle(
                text = stringResource(id = R.string.personal_info),
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(AppSpacing.Large))

            PersonalInfoTextField(
                value = formState.name,
                onValueChange = {
                    donorViewModel.updatePersonalInfo(
                        it,
                        formState.phoneNumber,
                        formState.bloodGroup,
                        formState.cityId
                    )
                },
                label = stringResource(id = R.string.full_name),
                leadingIcon = Icons.Default.Person
            )

            PersonalInfoTextField(
                value = formState.phoneNumber,
                onValueChange = {
                    donorViewModel.updatePersonalInfo(
                        formState.name,
                        it,
                        formState.bloodGroup,
                        formState.cityId
                    )
                },
                label = stringResource(id = R.string.phone_number),
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = defaultKeyboardOptions(KeyboardType.Phone)
            )

            PersonalInfoTextField(
                value = selectedBloodGroup,
                onValueChange = { input ->
                    selectedBloodGroup = input
                    donorViewModel.updatePersonalInfo(
                        formState.name,
                        formState.phoneNumber,
                        input,
                        formState.cityId
                    )
                },
                label = stringResource(id = R.string.blood_group),
                isTrailingIcon = true,
                list = bloodList,
                leadingIcon = Icons.Default.Bloodtype
            )

            PersonalInfoTextField(
                value = selectedCityName,
                onValueChange = { input ->
                    selectedCityName = input
                    val selectedId = provinces.firstOrNull { it.name == input }?.id
                    if (selectedId != null) {
                        donorViewModel.updatePersonalInfo(
                            formState.name,
                            formState.phoneNumber,
                            formState.bloodGroup,
                            selectedId
                        )
                    }
                },
                label = stringResource(id = R.string.city),
                isTrailingIcon = true,
                list = provinceNames,
                leadingIcon = Icons.Default.Place
            )

            DateOfBirthField(
                formState = formState,
                onUpdate = { dob, age, gender, willingToDonate, about ->
                    donorViewModel.updateBasicInfo(
                        dateOfBirth = dob,
                        age = age,
                        gender = gender,
                        willingToDonate = willingToDonate,
                        about = about
                    )
                }
            )

            if (isUnder18) {
                Text(
                    text = stringResource(id = R.string.under_18_error),
                    color = BloodRed,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            PersonalInfoTextField(
                value = formState.about,
                onValueChange = {
                    donorViewModel.updateBasicInfo(
                        dateOfBirth = formState.dateOfBirth,
                        age = formState.age,
                        gender = formState.gender,
                        willingToDonate = formState.willingToDonate,
                        about = it
                    )
                },
                label = stringResource(id = R.string.about_yourself),
                modifier = Modifier.height(Dimens.HeightXL2)
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            AppButton(
                onClick = {
                    val updatedDonor = Donor(
                        donorId = donorId,
                        name = formState.name,
                        phoneNumber = formState.phoneNumber,
                        bloodGroup = formState.bloodGroup,
                        cityId = formState.cityId,
                        dateOfBirth = formState.dateOfBirth,
                        age = formState.age,
                        gender = formState.gender,
                        willingToDonate = formState.willingToDonate,
                        about = formState.about
                    )
                    donorViewModel.updateDonor(updatedDonor)
                    onBackClick()
                },
                enabled = !isLoading && isModified && !isUnder18,
                text = stringResource(id = R.string.update)
            )

            if (formState.isSubmitSuccess) {
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
            }
        }
    }
}

@Composable
fun PersonalInfoTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    list: List<String> = emptyList(),
    isTrailingIcon: Boolean = false,
    leadingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = defaultKeyboardOptions(),
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val filteredList by remember(value, list) {
        derivedStateOf {
            if (value.isNotEmpty()) {
                list.filter {
                    it.contains(value, ignoreCase = true) || it.contains(
                        "others",
                        ignoreCase = true
                    )
                }.sorted()
            } else list.sorted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { expanded = false }
            )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                onValueChange(input)
                expanded = true
            },
            label = { Text(text = label) },
            leadingIcon = {
                leadingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = BloodRed,
                        modifier = Modifier.size(Dimens.SizeM)
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            trailingIcon = {
                if (isTrailingIcon) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            },
            shape = RoundedCornerShape(AppShape.ExtraLargeShape),
            modifier = modifier
                .padding(top = Dimens.PaddingS)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
        )

        AnimatedVisibility(visible = expanded && filteredList.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .width(textFieldSize.width.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateOfBirthField(
    formState: DonorFormState,
    onUpdate: (String, Int, String, Boolean, String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    var dobText by remember(formState.dateOfBirth) { mutableStateOf(formState.dateOfBirth) }

    val today =
        remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }

    OutlinedTextField(
        value = dobText,
        onValueChange = {},
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        leadingIcon = {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                tint = BloodRed,
                modifier = Modifier.size(Dimens.SizeM)
            )
        },
        trailingIcon = {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = null,
                modifier = Modifier.clickable { showDatePicker = true }
            )
        },
        readOnly = true,
        placeholder = { Text(stringResource(id = R.string.date_of_birth)) },
        textStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.PaddingS)
            .clickable { showDatePicker = true }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                    return date <= today
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                            if (date > today) {
                                showError = true
                            } else {
                                dobText = "%02d/%02d/%d".format(
                                    date.dayOfMonth,
                                    date.monthNumber,
                                    date.year
                                )
                                val age = calculateAge(dobText)
                                onUpdate(
                                    dobText,
                                    age,
                                    formState.gender,
                                    formState.willingToDonate,
                                    formState.about
                                )
                                showDatePicker = false
                                showError = false
                            }
                        }
                    }
                ) {
                    Text("OK", color = BloodRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(id = R.string.cancel), color = BloodRed)
                }
            }
        ) {
            Column {
                DatePicker(
                    state = datePickerState,
                    headline = {
                        Text(
                            text = stringResource(id = R.string.select_dob),
                            style = MaterialTheme.typography.titleLarge,
                            color = BloodRed,
                            modifier = Modifier.padding(Dimens.PaddingM)
                        )
                    },
                    modifier = Modifier
                )

                if (showError) {
                    Text(
                        stringResource(id = R.string.date_not_allowed),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(Dimens.PaddingM)
                    )
                }
            }
        }
    }
}