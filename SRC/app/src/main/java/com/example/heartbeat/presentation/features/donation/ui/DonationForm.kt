package com.example.heartbeat.presentation.features.donation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.components.DatePickerField
import com.example.heartbeat.presentation.features.donation.util.DonationValidator
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.PinkLight
import java.time.LocalDateTime

@Composable
fun DonationForm(
    modifier: Modifier = Modifier,
    eventId: String,
    donorId: String,
    donationViewModel: DonationViewModel = hiltViewModel()
) {
    var citizenId by remember { mutableStateOf("") }
    var donatedAt by remember { mutableStateOf("") }

    val heightTextField by remember { mutableStateOf(Dimens.HeightDefault) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var citizenIdTouched by remember { mutableStateOf(false) }
    val isCitizenIdError = if(citizenIdTouched) {
        !DonationValidator.validateCitizenIdLength(citizenId)
    } else false

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.citizen_id),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        OutlinedTextField(
            value = citizenId,
            onValueChange = {
                citizenId = it
                if (!citizenIdTouched) citizenIdTouched = true
            },
            placeholder = { Text(stringResource(id = R.string.hint_citizen_id), fontSize = 15.sp) },
            leadingIcon = {
                Icon(
                    Icons.Default.Badge,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.5f)
                )
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            ),
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
            isError = isCitizenIdError,
            shape = RoundedCornerShape(AppShape.ExtraLargeShape),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = modifier
                .defaultMinSize(minHeight = heightTextField)
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        if (isCitizenIdError) {
            Text(
                text = stringResource(id = R.string.error_citizen_id_invalid),
                color = BloodRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = Dimens.PaddingM,
                    top = Dimens.PaddingXXS
                )
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Text(
            text = stringResource(id = R.string.label_last_donation),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        DatePickerField(
            selectedDate = donatedAt,
            onDateChange = { donatedAt = it }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        AppButton(
            onClick = {
                val donation = Donation (
                    donationId = "${donorId}_${eventId}",
                    donorId = donorId,
                    eventId = eventId,
                    citizenId = citizenId,
                    status = "PENDING",
                    donationVolume = "0",
                    createAt = LocalDateTime.now(),
                    donatedAt = donatedAt
                )

                donationViewModel.addDonation(donation)
            },
            text = stringResource(id = R.string.submit)
        )
    }
}