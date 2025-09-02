package com.example.heartbeat.presentation.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heartbeat.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.ApiStatus.Experimental

data class OnboardingData(
    val img: Int,
    val title: String,
    val desc: String
)

@Composable
fun OnboardingScreen(navController: NavController) {
    val items = listOf(
        OnboardingData(
            img = R.drawable.onboarding_1,
            title = stringResource(R.string.donate_blood),
            desc = stringResource(R.string.donate_blood_desc)
        ),
        OnboardingData(
            img = R.drawable.onboarding_2,
            title = stringResource(R.string.save_lives),
            desc = stringResource(R.string.save_lives_desc)
        ),
        OnboardingData(
            img = R.drawable.onboarding_3,
            title = stringResource(R.string.get_connect),
            desc = stringResource(R.string.get_connect_desc)
        )
    )

    val pagerState = rememberPagerState(
        pageCount = { items.size },
        initialPage = 0
    )

    OnboardingPager(
        item = items,
        pagerState = pagerState,
        modifier = Modifier
            .fillMaxSize(),
        navController = navController
    )
}

@Composable
fun OnboardingPager(
    item: List<OnboardingData>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.padding(vertical = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = item[page].img),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(bottom = 50.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = item[page].title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = item[page].desc,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            }

            PagerIndicator(
                size = item.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.weight(0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if(pagerState.currentPage < item.lastIndex) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("onboarding") {
                                inclusive = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF4747)
                )
            ) {
                Text(
                    text = if(pagerState.currentPage == item.lastIndex) stringResource(id = R.string.get_started) else stringResource(id = R.string.next),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}