package com.example.heartbeat.presentation.features.system.setting.account

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.presentation.features.users.auth.util.AuthValidator
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.TextPrimaryDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChangePasswordScreen(onBackClick = { finish() })
        }
    }
}

@Composable
fun ChangePasswordScreen(onBackClick: () -> Unit, authViewModel: AuthViewModel = hiltViewModel()) {
    var newPassword by remember { mutableStateOf("") }
    val color = Color(0xFF767E8C)

    val context = LocalContext.current
    val activity = context as? Activity

    val isValid = AuthValidator.isValidPassword(newPassword)

    val successMessage = stringResource(id = R.string.update_success)
    val errorMessage = stringResource(id = R.string.change_pass_placeholder)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(Dimens.PaddingM + 2.dp)
            .padding(top = Dimens.PaddingM),
    ) {
        SettingTitle(
            text = stringResource(id = R.string.change_password),
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(id = R.string.new_password),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(Dimens.SizeM)
                )
            },
            placeholder = {
                Text(text = stringResource(id = R.string.new_password))
            },
            shape = RoundedCornerShape(AppShape.ExtraLargeShape),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.apply),
            onClick = {
                if (isValid) {
                    authViewModel.updatePassword(newPassword)
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    activity?.finish()
                } else {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}