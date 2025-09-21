package com.example.heartbeat.presentation.features.main.home

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppTitle
import com.example.heartbeat.presentation.features.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.AquaDeep
import com.example.heartbeat.ui.theme.AquaMist
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.BlushMist
import com.example.heartbeat.ui.theme.CompassionBlue
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.example.heartbeat.ui.theme.HeroLavender
import com.example.heartbeat.ui.theme.HeroLavenderText
import com.example.heartbeat.ui.theme.HopeGreen
import com.example.heartbeat.ui.theme.HopeGreenText
import com.example.heartbeat.ui.theme.PeachBackground
import com.example.heartbeat.ui.theme.SunshineYellow
import com.example.heartbeat.ui.theme.SunshineYellowText
import com.example.heartbeat.ui.theme.UnityPeach
import com.example.heartbeat.ui.theme.UnityPeachText
import com.example.heartbeat.ui.theme.VitalPink
import com.example.heartbeat.ui.theme.VitalPinkText

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val formState by donorViewModel.formState.collectAsState()
    val avatar by donorViewModel.donorAvatar.collectAsState()
    val isLoading by donorViewModel.isLoading.collectAsState()

    val scrollState = rememberLazyListState()
    val hasScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0
        }
    }

    val user = authState?.getOrNull()

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser()
    }

    LaunchedEffect(authState) {
        authState?.getOrNull()?.let { user ->
            Log.d("HomeScreen", "Fetching donor data for ${user.uid}")
            donorViewModel.getDonor()
            donorViewModel.getAvatar(user.uid)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = if (hasScrolled) 12.dp else 0.dp,
                shadowElevation = if (hasScrolled) 12.dp else 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.HeightXL2)
                        .background(PeachBackground),
                    contentAlignment = Alignment.Center
                ) {
                    user?.let {
                        UserTopBar(
                            avatar = avatar,
                            formState = formState,
                            user = it
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }

            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                item { BannerCard() }
                item { Spacer(modifier = Modifier.height(AppSpacing.Large)) }
                item { BloodGroup() }
                item { Spacer(modifier = Modifier.height(AppSpacing.Jumbo)) }
                item { WhyDonateList() }
                item { Spacer(modifier = Modifier.height(AppSpacing.Jumbo)) }
                item { UpcomingEventCard() }
            }

            formState.error?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun BannerCard() {
    val colorList = listOf(BloodRed, AquaDeep, AquaMist)

    Card(
        modifier = Modifier
            .padding(Dimens.PaddingM)
            .fillMaxWidth()
            .height(Dimens.HeightXL2),
        colors = CardDefaults.cardColors(
            containerColor = BlushMist
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingM),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(id = R.string.give_blood),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 32.sp
                )

                Text(
                    text = stringResource(id = R.string.save_a_life),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingXS)
                ) {
                    colorList.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color = color, CircleShape)
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_banner),
                contentDescription = null,
                modifier = Modifier.size(Dimens.SizeUltra + 50.dp)
            )
        }
    }
}

enum class WhyDonateTextList(@StringRes val title: Int, @StringRes val subTitle: Int) {
    Box1(R.string.box1_title, R.string.box1_subtitle),
    Box2(R.string.box2_title, R.string.box2_subtitle),
    Box3(R.string.box3_title, R.string.box3_subtitle),
    Box4(R.string.box4_title, R.string.box4_subtitle),
    Box5(R.string.box5_title, R.string.box5_subtitle),
    Box6(R.string.box6_title, R.string.box6_subtitle),
}

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

@Composable
fun UpcomingEventCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingM, vertical = Dimens.PaddingS),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        AppTitle(
            text = stringResource(id = R.string.upcoming_event)
        )

        Card(
            modifier = Modifier
                .height(Dimens.HeightXL3)
                .aspectRatio(2f),
            shape = RoundedCornerShape(AppShape.ExtraExtraLargeShape),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = Dimens.PaddingM, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_blood),
                        contentDescription = null,
                        tint = BloodRed,
                        modifier = Modifier.size(Dimens.SizeSM)
                    )
                    Spacer(Modifier.width(AppSpacing.Small))
                    Text(
                        text = "One Drop, One Life",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BloodRed
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = UnityPeachText,
                        modifier = Modifier.size(Dimens.SizeSM)
                    )
                    Spacer(Modifier.width(AppSpacing.Small + 2.dp))
                    Text(
                        text = "25/09/2025 - 08:00 AM",
                        style = MaterialTheme.typography.bodyMedium.copy(color = UnityPeachText)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hospital),
                        contentDescription = null,
                        tint = CompassionBlueText,
                        modifier = Modifier.size(Dimens.SizeSM)
                    )
                    Spacer(Modifier.width(AppSpacing.Small + 2.dp))
                    Text(
                        text = "Ha Dong General Hospital",
                        style = MaterialTheme.typography.bodyMedium.copy(color = CompassionBlueText)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(AppShape.MediumShape))
                                .background(Color(0xFFEEEEEE))
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.71f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(AppShape.MediumShape))
                                .background(BloodRed)
                        )
                    }
                    Spacer(Modifier.height(AppSpacing.Small))
                    Text(
                        text = "Progress: 71/100",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BloodRed
                        )
                    )
                }
            }
        }
    }
}