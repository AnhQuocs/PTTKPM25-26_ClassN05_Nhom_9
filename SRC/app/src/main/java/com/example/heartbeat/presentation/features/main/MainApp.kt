package com.example.heartbeat.presentation.features.main

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.domain.entity.user.AuthUser
import com.example.heartbeat.domain.repository.auth.AuthRepository
import com.example.heartbeat.presentation.components.BottomAppBar
import com.example.heartbeat.presentation.features.main.home.HomeScreen
import com.example.heartbeat.presentation.features.search.SearchScreen
import com.example.heartbeat.presentation.features.system.setting.SettingScreen

@Composable
fun MainApp(authRepository: AuthRepository = hiltViewModel()) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var previousTabIndex by remember { mutableIntStateOf(0) }

    val currentUser by produceState<AuthUser?>(initialValue = null) {
        value = authRepository.getCurrentUserFromFirestore()
    }

    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

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
                    ) + fadeIn(animationSpec = tween(200))).togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { width -> -width },
                            animationSpec = tween(200)
                        )
                    )
                } else {
                    (slideInHorizontally(
                        initialOffsetX = { width -> -width },
                        animationSpec = tween(200)
                    ) + fadeIn(animationSpec = tween(200))).togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { width -> width },
                            animationSpec = tween(200)
                        )
                    )
                }.using(SizeTransform(clip = false))
            },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) { tab ->
            when(tab) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> SettingScreen()
            }
        }
    }
}