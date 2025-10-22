package com.example.heartbeat.presentation.features.users.staff.ui.calendar.confirm

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.GoldenGlow

@Composable
fun ConfirmDonorItem(
    bloodVolume: String,
    onBloodVolumeChange: (String) -> Unit,
    donation: Donation,
    formState: DonorFormState,
    onConfirm: () -> Unit,
    onUnable: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rowHeight = Dimens.HeightLarge - 4.dp

    val lastDonation = donation.donatedAt.ifBlank { stringResource(id = R.string.no_record) }
    val detailStyle =
        MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium)

    val isButtonEnabled = bloodVolume.isNotEmpty()

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppShape.SmallShape + 2.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = Dimens.PaddingSM, vertical = Dimens.PaddingXS)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                tint = Color.DarkGray.copy(alpha = 0.8f),
                contentDescription = null,
                modifier = Modifier.size(Dimens.SizeL)
            )

            Spacer(modifier = Modifier.width(AppSpacing.MediumLarge))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    formState.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formState.bloodGroup,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.Gray
                )
            }

            Text(
                text = formState.dateOfBirth,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = Color.Gray
            )

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.PaddingXXL - 4.dp,
                        end = Dimens.PaddingS,
                        bottom = Dimens.PaddingS
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingXS)
                ) {
                    Text(
                        text = stringResource(id = R.string.city) + ": ${formState.cityId}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.age) + ": ${formState.age}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.gender) + ": ${formState.gender}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.phone_number) + ": ${formState.phoneNumber}",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.label_last_donation) + ": $lastDonation",
                        style = detailStyle
                    )
                    Text(
                        text = stringResource(id = R.string.citizen_id) + ": ${donation.citizenId}",
                        style = detailStyle
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.Large)
                ) {
                    OutlinedTextField(
                        value = bloodVolume,
                        onValueChange = onBloodVolumeChange,
                        shape = RoundedCornerShape(AppShape.SmallShape),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.blood_volume_placeholder),
                                fontSize = 13.sp,
                                lineHeight = 1.sp
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .height(Dimens.HeightDefault)
                            .weight(0.6f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BloodRed,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Text("ml", modifier = Modifier.weight(0.3f), fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    OutlinedButton(
                        onClick = onUnable,
                        enabled = !isButtonEnabled,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text(stringResource(id = R.string.unable))
                    }

                    Button(
                        onClick = onConfirm,
                        enabled = isButtonEnabled,
                        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                }
            }
        }

        Divider(color = Color(0xFFE0E0E0))
    }
}