package com.example.heartbeat.presentation.features.main.home

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.system.province.viewmodel.ProvinceViewModel
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.AquaDeep
import com.example.heartbeat.ui.theme.AquaMist
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.BlushMist
import com.example.heartbeat.ui.theme.PeachBackground

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    provinceViewModel: ProvinceViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()
    val formState by donorViewModel.formState.collectAsState()
    val avatar by donorViewModel.donorAvatar.collectAsState()
    val isLoading by donorViewModel.isLoading.collectAsState()

    val events by eventViewModel.events.collectAsState(initial = emptyList())

    Log.d("HomeScreen", "Event size: ${events.size}")

    val scrollState = rememberLazyListState()
    val hasScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0
        }
    }

    val user = authState?.getOrNull()

    val selectedProvince by provinceViewModel.selectedProvince.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser()
    }

    LaunchedEffect(authState) {
        authState?.getOrNull()?.let { user ->
            donorViewModel.getCurrentDonor()
            donorViewModel.getAvatar(user.uid)
        }
    }

    LaunchedEffect(formState.cityId) {
        if (formState.cityId.isNotBlank()) {
            provinceViewModel.loadProvinceById(formState.cityId)
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
                    if(user != null && selectedProvince != null) {
                        UserTopBar(
                            avatar = avatar,
                            user = user,
                            cityName = selectedProvince!!.name
                        )
                    } else {
                        UserShimmerLoading()
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                item {
                    user?.let {
                        UpcomingEventCard(
                            context = context,
                            donorId = it.uid,
                            events = events,
                            navController = navController
                        )
                    }
                }
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
fun ProgressBar(
    value: Int,
    capacity: Int,
    modifier: Modifier = Modifier
) {
    val targetPercentage = (value.toFloat() / capacity.toFloat()).coerceIn(0f, 1f)

    val animatedPercentage by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 800),
        label = "progressAnim"
    )

    Column(modifier = modifier) {
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
                    .fillMaxWidth(animatedPercentage)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(AppShape.MediumShape))
                    .background(BloodRed)
            )
        }

        Spacer(Modifier.height(AppSpacing.Small))

        Text(
            text = "Progress: $value/$capacity",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = BloodRed
            )
        )
    }
}