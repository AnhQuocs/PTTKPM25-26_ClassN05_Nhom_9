package com.example.heartbeat.presentation.features.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.presentation.features.auth.ui.LoginScreen
import com.example.heartbeat.presentation.features.auth.ui.SignUpScreen
import com.example.heartbeat.presentation.features.auth.viewmodel.SplashViewModel
import com.example.heartbeat.presentation.features.onboarding.OnboardingScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : BaseComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            AnimatedNavHost(
                navController = navController,
                startDestination = "splash"
            ) {
                composable("splash") { SplashScreen(navController) }
                composable("onboarding") { OnboardingScreen(navController) }
                composable("login") { LoginScreen(navController) }
                composable("signUp") { SignUpScreen(navController) }
                composable("main") { MainApp() }
            }
        }
    }
}
@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val hasOnboarded by viewModel.hasOnboarded.collectAsState(initial = false)
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            when {
                currentUser == null -> {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                !hasOnboarded -> {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                else -> {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
    }
}