package com.example.heartbeat.presentation.features.event.ui.donated_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun ThankBannerCard() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraExtraLargeShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(AppShape.ExtraLargeShape)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = stringResource(id = R.string.thank_banner_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = stringResource(id = R.string.thank_banner_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DonatedDetailDonorInfo(formState: DonorFormState, donation: Donation, province: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.PaddingM)
                .fillMaxWidth()
        ) {
            TitleSection(
                text1 = stringResource(id = R.string.donation_info_card),
                text2 = ""
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            DonatedDetailItem(
                title = stringResource(id = R.string.full_name),
                text = formState.name
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.date_of_birth),
                text = formState.dateOfBirth + " (${formState.age})"
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.city),
                text = province
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.gender),
                text = formState.gender
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.blood_group),
                text = formState.bloodGroup
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.phone_number),
                text = formState.phoneNumber
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.citizen_id),
                text = donation.citizenId
            )

            DonatedDetailItem(
                title = stringResource(id = R.string.donated_volume),
                text = donation.donationVolume + " ml"
            )

            if (formState.about != "") {
                DonatedDetailItem(
                    title = stringResource(id = R.string.about_yourself),
                    text = formState.about
                )
            }
        }
    }
}

@Composable
fun DonatedDetailItem(
    title: String,
    text: String
) {
    val color = Color(0xFF767E8C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.PaddingS),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "$title: ",
            color = Color.Black.copy(0.8f),
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}