package com.example.heartbeat.presentation.features.system.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import dagger.hilt.android.AndroidEntryPoint

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
    donorViewModel: DonorViewModel = hiltViewModel()
) {
    val formState by donorViewModel.formState.collectAsState()
    val isLoading by donorViewModel.isLoading.collectAsState()

    LaunchedEffect(donorId) {
        donorViewModel.getDonorById(donorId)
    }

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
            onValueChange = { donorViewModel.updatePersonalInfo(it, formState.phoneNumber, formState.bloodGroup, formState.cityId) },
            label = stringResource(id = R.string.full_name)
        )

        PersonalInfoTextField(
            value = formState.phoneNumber,
            onValueChange = { donorViewModel.updatePersonalInfo(formState.name, it, formState.bloodGroup, formState.cityId) },
            label = stringResource(id = R.string.phone_number)
        )

        PersonalInfoTextField(
            value = formState.bloodGroup,
            onValueChange = { donorViewModel.updatePersonalInfo(formState.name, formState.phoneNumber, it, formState.cityId) },
            label = stringResource(id = R.string.blood_group)
        )

        PersonalInfoTextField(
            value = formState.cityId,
            onValueChange = { donorViewModel.updatePersonalInfo(formState.name, formState.phoneNumber, formState.bloodGroup, it) },
            label = stringResource(id = R.string.city)
        )

        PersonalInfoTextField(
            value = formState.dateOfBirth,
            onValueChange = {
                donorViewModel.updateBasicInfo(
                    dateOfBirth = it,
                    age = formState.age,
                    gender = formState.gender,
                    willingToDonate = formState.willingToDonate,
                    about = formState.about
                )
            },
            label = stringResource(id = R.string.date_of_birth)
        )

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
            modifier = Modifier.height(Dimens.HeightXL)
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
            },
            enabled = !isLoading,
            text = "Update"
        )

        formState.error?.let { error ->
            Text(
                text = "Lỗi: $error",
                color = Color.Red,
                modifier = Modifier.padding(top = Dimens.PaddingS)
            )
        }

        if (formState.isSubmitSuccess) {
            Text(
                text = stringResource(id = R.string.update_success),
                color = Color(0xFF00C853),
                modifier = Modifier.padding(top = Dimens.PaddingS)
            )
        }
    }
}

@Composable
fun PersonalInfoTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
          Text(text = label)
        },
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        modifier = modifier
            .padding(top = Dimens.PaddingS)
            .fillMaxWidth()
    )
}