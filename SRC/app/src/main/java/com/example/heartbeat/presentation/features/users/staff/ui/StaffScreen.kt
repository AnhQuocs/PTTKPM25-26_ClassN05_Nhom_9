package com.example.heartbeat.presentation.features.users.staff.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.heartbeat.presentation.components.BottomAppBar
import com.example.heartbeat.presentation.features.users.staff.ui.create.CreateEventScreen
import com.example.heartbeat.presentation.features.users.staff.ui.home.StaffHomeScreen
import com.example.heartbeat.presentation.features.users.staff.ui.setting.StaffSettingScreen
import com.example.heartbeat.ui.theme.AquaMint
import com.example.heartbeat.ui.theme.CoralRed
import com.example.heartbeat.ui.theme.GoldenGlow
import com.example.heartbeat.ui.theme.RoyalPurple
import com.example.heartbeat.ui.theme.SunsetOrange

@Composable
fun StaffScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var previousTabIndex by remember { mutableIntStateOf(0) }

    val gradients = listOf(
        Brush.linearGradient(
            colors = listOf(Color(0xFFEAEFFF), Color(0xFFFAF9FF))
        ),

        Brush.linearGradient(
            colors = listOf(Color(0xFFEDFFFB), Color(0xFFFFEBF4))
        ),

        Brush.linearGradient(
            colors = listOf(Color(0xFFFFFAF3), Color(0xFFECEFFF))
        ),

        Brush.linearGradient(
            colors = listOf(Color(0xFFEFFFF4), Color(0xFFFFFFEB))
        )
    )

    val accentColors = listOf(
        SunsetOrange,
        AquaMint,
        RoyalPurple,
        GoldenGlow,
        CoralRed
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            )
        },
        bottomBar = {
            BottomAppBar(
                tabs = BottomBarItem.entries.toTypedArray(),
                currentIndex = selectedTabIndex,
                onTabSelected = { newIndex ->
                    previousTabIndex = selectedTabIndex
                    selectedTabIndex = newIndex
                }
            )
        }
    ) { paddingValues ->

        val isForward = selectedTabIndex > previousTabIndex

        AnimatedContent(
            targetState = selectedTabIndex,
            label = "TabTransition",
            transitionSpec = {
                if (isForward) {
                    (slideInHorizontally(
                        initialOffsetX = { width -> width },
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 200)
                    )).togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { width -> -width },
                            animationSpec = tween(durationMillis = 200)
                        )
                    )
                } else {
                    (slideInHorizontally(
                        initialOffsetX = { width -> -width },
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 200)
                    )).togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { width -> width },
                            animationSpec = tween(durationMillis = 200)
                        )
                    )
                }.using(
                    SizeTransform(clip = false)
                )
            },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) { tab ->
            when (tab) {
                0 -> StaffHomeScreen()
                1 -> CreateEventScreen()
                2 -> StaffSettingScreen()
            }
        }
    }
}