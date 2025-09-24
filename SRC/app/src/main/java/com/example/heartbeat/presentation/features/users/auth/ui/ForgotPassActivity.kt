package com.example.heartbeat.presentation.features.users.auth.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ForgotPassScreen(
                onBackClick = { finish() }
            )
        }
    }
}

@Composable
fun ForgotPassScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val emailError by authViewModel.emailError.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val isSendEmail by authViewModel.isSendEmail.collectAsState()
    val isSendLoading by authViewModel.isSendLoading.collectAsState()

    val context = LocalContext.current

    val message = stringResource(id = R.string.forgot_pass_sent )

    LaunchedEffect(isSendEmail) {
        if(isSendEmail) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val errorText = stringResource(id = R.string.forgot_pass_error)

    LaunchedEffect(errorMessage) {
        if(errorMessage != null) {
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
            authViewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .height(Dimens.HeightML),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = Dimens.PaddingS)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.Black),
                            modifier = Modifier
                                .size(Dimens.SizeML)
                                .clickable { onBackClick() }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = stringResource(id = R.string.forgot_pass),
                            color = Color.Black,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

                    AppLineGrey()
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(paddingValues)
                    .padding(Dimens.PaddingM)
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_pass_desc),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextFieldAuth(
                    value = email,
                    onValueChange = { email = it },
                    icon = Icons.Default.Email,
                    placeholder = stringResource(id = R.string.email_placeholder),
                    label = stringResource(id = R.string.email),
                    isError = emailError != null,
                    errorMessage = emailError
                )

                Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

                Text(
                    text = stringResource(id = R.string.forgot_pass_if_exists)
                )

                Spacer(modifier = Modifier.weight(1f))

                AppButton(
                    onClick = {
                        authViewModel.resetPassword(email)
                    },
                    text = stringResource(id = R.string.send)
                )
            }
        }

        if(isSendLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
            }
        }
    }
}