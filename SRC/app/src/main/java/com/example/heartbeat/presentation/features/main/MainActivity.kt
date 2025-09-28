package com.example.heartbeat.presentation.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.presentation.features.users.admin.AdminScreen
import com.example.heartbeat.presentation.features.users.auth.ui.LoginScreen
import com.example.heartbeat.presentation.features.users.auth.ui.SignUpScreen
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.onboarding.OnboardingScreen
import com.example.heartbeat.presentation.features.users.staff.ui.StaffLoginScreen
import com.example.heartbeat.presentation.features.users.staff.ui.StaffMainScreen
import com.example.heartbeat.presentation.features.users.staff.ui.StaffSignUpScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val authState by authViewModel.authState.collectAsState()
            val isLoading by authViewModel.isUserLoading.collectAsState()

            // 1 AnimatedNavHost duy nhất với tất cả route
            AnimatedNavHost(navController = navController, startDestination = "splash") {
                composable("splash") { SplashScreen() }
                composable("onboarding") { OnboardingScreen(navController) }
                composable("login") { LoginScreen(navController) }
                composable("signUp") { SignUpScreen(navController) }
                composable("main") { MainApp(navController) }
                composable("admin_main") { AdminScreen() }
                composable("staff_login") { StaffLoginScreen(navController) }
                composable("staff_signUp") { StaffSignUpScreen(navController) }
                composable("staff_main") { StaffMainScreen() }
            }

            LaunchedEffect(isLoading, authState) {
                if (!isLoading) {
                    val role = authState?.getOrNull()?.role
                    when (role) {
                        "user" -> navController.navigate("main") {
                            popUpTo("splash") { inclusive = true }
                        }
                        "admin" -> navController.navigate("admin_main") {
                            popUpTo("splash") { inclusive = true }
                            popUpTo("onboarding") { inclusive = true }
                        }
                        "staff" -> navController.navigate("staff_main") {
                            popUpTo("splash") { inclusive = true }
                        }
                        else -> navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Loading...")
    }
}