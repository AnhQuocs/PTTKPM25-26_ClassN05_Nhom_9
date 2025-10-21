package com.example.heartbeat.presentation.features.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppTitle
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.HeroLavender
import com.example.heartbeat.ui.theme.HeroLavenderText
import com.example.heartbeat.ui.theme.HopeGreen
import com.example.heartbeat.ui.theme.HopeGreenText
import com.example.heartbeat.ui.theme.SunshineYellow
import com.example.heartbeat.ui.theme.SunshineYellowText
import com.example.heartbeat.ui.theme.UnityPeach
import com.example.heartbeat.ui.theme.UnityPeachText
import com.example.heartbeat.ui.theme.VitalPink
import com.example.heartbeat.ui.theme.VitalPinkText


@Composable
fun WhyDonateList() {
    val colorBgrList =
        listOf(CompassionBlue, HopeGreen, HeroLavender, VitalPink, UnityPeach, SunshineYellow)
    val colorTextList = listOf(
        CompassionBlueText,
        HopeGreenText,
        HeroLavenderText,
        VitalPinkText,
        UnityPeachText,
        SunshineYellowText
    )

    val textList = WhyDonateTextList.entries.toTypedArray()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingM),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        AppTitle(text = stringResource(id = R.string.why_donate))

        repeat(2) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
            ) {
                repeat(3) { columnIndex ->
                    val index = rowIndex * 3 + columnIndex

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(AppShape.MediumShape))
                            .background(
                                color = colorBgrList[index],
                                RoundedCornerShape(AppShape.MediumShape)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(textList[index].title),
                                style = MaterialTheme.typography.titleMedium,
                                color = colorTextList[index]
                            )
                            Text(
                                text = stringResource(textList[index].subTitle),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BloodGroup() {
    val bloodLists = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingM),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        AppTitle(text = stringResource(id = R.string.blood_group))

        repeat(2) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
            ) {
                repeat(4) { columnIndex ->
                    val index = rowIndex * 4 + columnIndex

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(AppShape.MediumShape))
                            .border(
                                1.dp,
                                color = BloodRed,
                                shape = RoundedCornerShape(AppShape.MediumShape)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_blood),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(BloodRed),
                            modifier = Modifier.padding(Dimens.PaddingSM)
                        )

                        Text(
                            text = bloodLists[index],
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            }
        }
    }
}