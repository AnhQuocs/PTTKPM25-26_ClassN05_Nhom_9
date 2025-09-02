package com.example.heartbeat.presentation.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.presentation.features.auth.ui.LoginScreen
import com.example.heartbeat.presentation.features.auth.ui.SignUpScreen
import com.example.heartbeat.presentation.features.auth.viewmodel.SplashViewModel
import com.example.heartbeat.presentation.features.onboarding.OnboardingScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            OnboardingScreen()
            val navController = rememberNavController()
            val startDes by splashViewModel.startDestination.collectAsState()

            AnimatedNavHost(
                navController = navController,
                startDestination = startDes
            ) {
                composable("onboarding") {
                    OnboardingScreen(navController)
                }

                composable("login") {
                    LoginScreen(navController)
                }

                composable("signUp") {
                    SignUpScreen(navController)
                }
            }
        }
    }
}