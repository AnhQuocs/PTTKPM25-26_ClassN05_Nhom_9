package com.example.heartbeat.presentation.features.system.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.system.AppLanguage
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.main.MainActivity
import com.example.heartbeat.presentation.features.system.language.LanguageViewModel
import com.example.heartbeat.utils.LangUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeLanguageActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChangeLanguageScreen()
        }
    }
}

@Composable
fun ChangeLanguageScreen(languageViewModel: LanguageViewModel = hiltViewModel()) {
    val currentLang by languageViewModel.currentLanguage.collectAsState()
    var lastLang by remember { mutableStateOf(currentLang) }
    var selectedLang by remember { mutableStateOf(currentLang) }

    val context = LocalContext.current
    val activity = context as? ComponentActivity

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
                val code = selectedLang.code
                LangUtils.updateLocale(context, code)

                languageViewModel.changeLanguage(selectedLang)

                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                (activity as? Activity)?.overridePendingTransition(0, 0)
            }
        ) {
            Text("Change")
        }
    }
}