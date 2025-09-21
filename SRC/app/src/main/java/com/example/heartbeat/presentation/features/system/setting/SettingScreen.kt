package com.example.heartbeat.presentation.features.system.setting

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.domain.entity.system.AppLanguage
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.system.language.LanguageViewModel
import com.example.heartbeat.utils.LangUtils

@Composable
fun SettingScreen(
    navController: NavController,
    languageViewModel: LanguageViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val currentLang by languageViewModel.currentLanguage.collectAsState()
    var lastLang by remember { mutableStateOf(currentLang) }

    var selectedLang by remember { mutableStateOf(currentLang) }

    LaunchedEffect(currentLang) {
        if (currentLang != lastLang) {
            lastLang = currentLang
            activity?.recreate()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Language",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedLang = AppLanguage.ENGLISH }
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = selectedLang == AppLanguage.ENGLISH,
                onClick = { selectedLang = AppLanguage.ENGLISH }
            )
            Text("English 🇬🇧")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedLang = AppLanguage.VIETNAMESE }
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = selectedLang == AppLanguage.VIETNAMESE,
                onClick = { selectedLang = AppLanguage.VIETNAMESE }
            )
            Text("Tiếng Việt 🇻🇳")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                LangUtils.currentLang = selectedLang.code
                languageViewModel.changeLanguage(selectedLang)
            }
        ) {
            Text("Change")
        }

        AppButton(
            onClick = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            },
            text = "Logout"
        )
    }
}