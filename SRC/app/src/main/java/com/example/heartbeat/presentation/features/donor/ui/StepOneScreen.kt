package com.example.heartbeat.presentation.features.donor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed

@Composable
fun StepOneScreen(
    formState: DonorFormState,
    onUpdate: (name: String, phoneNumber: String, bloodGroup: String, city: String) -> Unit,
    onNext: () -> Unit
) {
    var name by remember { mutableStateOf(formState.name) }
    var phoneNumber by remember { mutableStateOf(formState.phoneNumber) }
    var bloodGroup by remember { mutableStateOf(formState.bloodGroup) }
    var city by remember { mutableStateOf(formState.city) }

    val yourNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }
    val bloodGroupFocusRequester = remember { FocusRequester() }
    val cityFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .padding(top = Dimens.PaddingXL),
    ) {
        Text(
            text = stringResource(id = R.string.profile_setup),
            color = Color.Black,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Text(
            text = stringResource(id = R.string.profile_setup_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        AppLineGrey()

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Box(
            modifier = Modifier
                .size(Dimens.SizeXXLPlus)
                .background(color = BloodRed.copy(alpha = 0.3f))
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.PersonOutline,
                contentDescription = null,
                tint = BloodRed,
                modifier = Modifier
                    .size(Dimens.SizeL)
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Text(
            text = stringResource(id = R.string.personal_info),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))

        ProfileSetupTextField(
            title = stringResource(id = R.string.your_name),
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(id = R.string.your_name)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bloodGroup,
            onValueChange = { bloodGroup = it },
            label = { Text("Blood Group") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onUpdate(name, phoneNumber, bloodGroup, city)
                onNext()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}