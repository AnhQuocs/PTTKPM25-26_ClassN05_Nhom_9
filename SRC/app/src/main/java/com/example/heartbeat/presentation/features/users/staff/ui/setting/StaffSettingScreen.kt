package com.example.heartbeat.presentation.features.users.staff.ui.setting

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.presentation.features.system.setting.SettingScreen
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel

@Composable
fun StaffSettingScreen(
    navController: NavController
) {
    SettingScreen(
        navController = navController,
        isShowPersonalInfo = false,
        isShowDonationHistory = false
    )
}