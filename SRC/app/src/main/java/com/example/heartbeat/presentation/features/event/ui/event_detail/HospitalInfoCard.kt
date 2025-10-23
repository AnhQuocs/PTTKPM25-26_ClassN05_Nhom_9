package com.example.heartbeat.presentation.features.event.ui.event_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun HospitalInfoCard(hospital: Hospital) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens.PaddingM)
        ) {
            Box(
                modifier = Modifier.padding(horizontal = Dimens.PaddingM)
            ) {
                TitleSection(
                    text1 = stringResource(id = R.string.hospital_info),
                    text2 = ""
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = hospital.imgUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(Dimens.SizeXXLPlus)
                        .weight(0.3f)
                )

                Text(
                    text = hospital.hospitalName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(0.7f)
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = Dimens.PaddingM)
            ) {
                Spacer(modifier = Modifier.height(AppSpacing.Large))

                HospitalInfoItem(
                    icon = Icons.Default.LocationOn,
                    text = hospital.address
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HospitalInfoItem(
                    icon = Icons.Default.LocationCity,
                    text = hospital.district + ", " + hospital.province
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HospitalInfoItem(
                    icon = Icons.Default.Phone,
                    text = hospital.phone
                )
            }
        }
    }
}

@Composable
fun HospitalInfoItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(Dimens.SizeM)
        )

        Spacer(modifier = Modifier.width(AppSpacing.Medium))

        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}