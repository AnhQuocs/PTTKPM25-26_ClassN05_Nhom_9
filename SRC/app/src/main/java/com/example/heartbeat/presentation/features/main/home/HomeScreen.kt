package com.example.heartbeat.presentation.features.main.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    val avatar by donorViewModel.donorAvatar.collectAsState()
    val formState by donorViewModel.formState.collectAsState()

    val scrollState = rememberLazyListState()
    val hasScrolled = remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0
        }
    }

    LaunchedEffect(authState) {
        val user = authState?.getOrNull()
        if (user != null) {
            donorViewModel.getDonor()
            donorViewModel.getAvatar(user.uid)
        }
    }

    authState?.onSuccess { user ->
        Scaffold(
            topBar = {
                Surface(
                    tonalElevation = if(hasScrolled.value) 12.dp else 0.dp,
                    shadowElevation = if(hasScrolled.value) 12.dp else 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.HeightXL3 + 10.dp)
                            .background(color = PeachBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        UserTopBar(
                            avatar = avatar,
                            formState = formState,
                            user = user
                        )
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    )
                    .background(color = Color.White)
            ) {
                item {
                    BannerCard()
                }

                item {
                    Spacer(modifier = Modifier.height(AppSpacing.Large))
                }

                item {
                    WhyDonateList()
                }
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

@Composable
fun WhyDonateList() {
    val colorBgrList = listOf(CompassionBlue, HopeGreen, HeroLavender, VitalPink, UnityPeach, SunshineYellow)
    val colorTextList = listOf(CompassionBlueText, HopeGreenText, HeroLavenderText, VitalPinkText, UnityPeachText, SunshineYellowText)

    val textList = WhyDonateTextList.entries.toTypedArray()

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = Dimens.PaddingM),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        AppTitle(
            text = stringResource(id = R.string.why_donate)
        )

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
                            .background(color = colorBgrList[index], RoundedCornerShape(AppShape.MediumShape)),
                        contentAlignment = Alignment.Center
                    ) {
                      Column(
                          horizontalAlignment = Alignment.CenterHorizontally
                      ){
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

enum class WhyDonateTextList(@StringRes  val title: Int, @StringRes val subTitle: Int) {
    Box1(R.string.box1_title, R.string.box1_subtitle),
    Box2(R.string.box2_title, R.string.box2_subtitle),
    Box3(R.string.box3_title, R.string.box3_subtitle),
    Box4(R.string.box4_title, R.string.box4_subtitle),
    Box5(R.string.box5_title, R.string.box5_subtitle),
    Box6(R.string.box6_title, R.string.box6_subtitle),
}