package com.example.heartbeat.presentation.features.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.presentation.features.auth.ui.LoginScreen
import com.example.heartbeat.presentation.features.auth.ui.SignUpScreen
import com.example.heartbeat.presentation.features.auth.viewmodel.SessionViewModel
import com.example.heartbeat.presentation.features.onboarding.OnboardingScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : BaseComponentActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val startDestination by sessionViewModel.startDestination.collectAsState()

            Log.d("AuthDeBug", "MainActivity-Route: $startDestination")

            startDestination?.let {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = it
                ) {
                    composable("onboarding") { OnboardingScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("signUp") { SignUpScreen(navController) }
                    composable("main") { MainApp() }
                }
            }
        }
    }
}