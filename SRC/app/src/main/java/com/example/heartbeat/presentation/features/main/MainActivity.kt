package com.example.heartbeat.presentation.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.BottomAppBar
import com.example.heartbeat.presentation.components.TabItem
import com.example.heartbeat.presentation.features.donation.ui.register_detail.DonationDetailScreen
import com.example.heartbeat.presentation.features.onboarding.OnboardingScreen
import com.example.heartbeat.presentation.features.users.admin.AdminScreen
import com.example.heartbeat.presentation.features.users.auth.ui.LoginScreen
import com.example.heartbeat.presentation.features.users.auth.ui.SignUpScreen
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation.ApproveScreen
import com.example.heartbeat.presentation.features.users.staff.ui.main.StaffLoginScreen
import com.example.heartbeat.presentation.features.users.staff.ui.main.StaffMainScreen
import com.example.heartbeat.presentation.features.users.staff.ui.main.StaffSignUpScreen
import com.example.heartbeat.testing.UiTestTags
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
            val slideDuration = 200

            AnimatedNavHost(navController = navController, startDestination = "splash") {
                composable("splash") { SplashScreen() }
                composable("onboarding") { OnboardingScreen(navController) }
                composable("login") { LoginScreen(navController) }
                composable("signUp") { SignUpScreen(navController) }
                composable("main") { MainApp(navController) }
                composable("ui_test_main") { UiTestMainScreen(navController) }
                composable("admin_main") { AdminScreen() }
                composable("staff_login") { StaffLoginScreen(navController) }
                composable("staff_signUp") { StaffSignUpScreen(navController) }
                composable("staff_main") { StaffMainScreen(navController) }
                composable(
                    route = "register_donation/{eventId}/{donorId}",
                    arguments = listOf(
                        navArgument("eventId") { type = NavType.StringType },
                        navArgument("donorId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val eventId1 = backStackEntry.arguments?.getString("eventId")
                    val donorId = backStackEntry.arguments?.getString("donorId")
                    DonationDetailScreen(
                        eventId = eventId1 ?: "",
                        donorId = donorId ?: "",
                        navController = navController
                    )
                }
                composable(
                    route = "staff_approve/{eventId}",
                    arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId")
                    ApproveScreen(eventId = eventId ?: "", navController = navController)
                }
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
        Text(stringResource(id = R.string.loading), fontSize = 18.sp)
    }
}

@Composable
private fun UiTestMainScreen(navController: androidx.navigation.NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                tabs = TabItem.entries.toTypedArray(),
                currentIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTabIndex) {
                0 -> Text("Home")
                1 -> Text("Search")
                2 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Setting")
                        Button(
                            modifier = Modifier.testTag(UiTestTags.SettingLogout),
                            onClick = { showLogoutDialog = true }
                        ) {
                            Text("Logout")
                        }
                    }
                }
            }

            if (showLogoutDialog) {
                AlertDialog(
                    modifier = Modifier.testTag(UiTestTags.LogoutDialog),
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Logout") },
                    text = { Text("Are you sure you want to log out?") },
                    confirmButton = {
                        Button(
                            modifier = Modifier.testTag(UiTestTags.LogoutConfirmButton),
                            onClick = {
                                showLogoutDialog = false
                                navController.navigate("login") {
                                    popUpTo("ui_test_main") { inclusive = true }
                                }
                            }
                        ) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            modifier = Modifier.testTag(UiTestTags.LogoutCancelButton),
                            onClick = { showLogoutDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
