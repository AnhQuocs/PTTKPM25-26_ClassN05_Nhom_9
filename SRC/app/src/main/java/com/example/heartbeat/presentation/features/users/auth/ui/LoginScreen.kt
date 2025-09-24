package com.example.heartbeat.presentation.features.users.auth.ui

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppButton
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthActionType
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.users.donor.ui.profile_setup.ProfileSetupActivity
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.FacebookBlue
import com.example.heartbeat.ui.theme.OceanBlue
import com.google.firebase.auth.FirebaseAuthUserCollisionException

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val authState by authViewModel.authState.collectAsState()
    val lastAction by authViewModel.lastAuthAction.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val emailError by authViewModel.emailError.collectAsState()
    val passwordError by authViewModel.passwordError.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(authState) {
        authState?.onSuccess {
            val message = when (lastAction) {
                AuthActionType.LOGIN -> context.getString(R.string.login_success)
                AuthActionType.SIGN_UP -> context.getString(R.string.sign_up_success)
                else -> null
            }
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            authViewModel.clearAuthState()
        }

        authState?.onFailure { throwable ->
            val message = when {
                throwable is FirebaseAuthUserCollisionException -> context.getString(R.string.email_already_exists)
                lastAction == AuthActionType.LOGIN -> context.getString(R.string.login_failed)
                lastAction == AuthActionType.SIGN_UP -> context.getString(R.string.sign_up_failed)
                else -> context.getString(R.string.unknown_error)
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            authViewModel.clearAuthState()
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            donorViewModel.getDonor { exists ->
                if (exists) {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                } else {
                    val intent = Intent(context, ProfileSetupActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }

                authViewModel.clearAuthState()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(Dimens.PaddingM)
                .padding(vertical = Dimens.PaddingUltra),
        ) {
            Text(
                text = stringResource(id = R.string.welcome_app),
                color = Color.Black,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = stringResource(id = R.string.welcome_des),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.titleSmall
            )

            OutlinedTextFieldAuth(
                value = email,
                onValueChange = { email = it },
                label = stringResource(id = R.string.email),
                placeholder = stringResource(id = R.string.email_placeholder),
                icon = Icons.Default.Email,
                focusRequester = emailFocusRequester,
                isError = emailError != null,
                errorMessage = emailError,
                imeAction = ImeAction.Next,
                onImeAction = {
                    passwordFocusRequester.requestFocus()
                }
            )

            Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

            Text(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.titleSmall
            )

            PasswordOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(id = R.string.password),
                placeholder = stringResource(id = R.string.password_placeholder),
                leadingIcon = Icons.Default.Lock,
                isError = passwordError != null,
                errorMessage = passwordError,
                focusRequester = passwordFocusRequester,
                imeAction = ImeAction.Done
            )

            Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_pass) + "?",
                    color = OceanBlue,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(context, ForgotPassActivity::class.java)
                            context.startActivity(intent)
                        }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AppButton(
                onClick = {
                    authViewModel.login(email, password)
                },
                text = stringResource(id = R.string.login)
            )

            Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(Color.LightGray.copy(alpha = 0.6f))
                        .weight(1f)
                )

                Text(
                    "Or Login with",
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingS)
                )

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(Color.LightGray.copy(alpha = 0.6f))
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(AppShape.MediumShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEEEEE)
                    ),
                    modifier = Modifier
                        .height(Dimens.HeightText + 9.dp)
                        .weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_facebook),
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.SizeML)
                        )

                        Text(
                            "Facebook",
                            color = FacebookBlue,
                            modifier = Modifier.padding(horizontal = Dimens.PaddingS)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(AppSpacing.Large))

                Button(
                    onClick = {},
                    shape = RoundedCornerShape(AppShape.MediumShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEEEEE)
                    ),
                    modifier = Modifier
                        .height(Dimens.HeightText + 9.dp)
                        .weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.SizeML)
                        )

                        Text(
                            "Google",
                            color = BloodRed,
                            modifier = Modifier.padding(horizontal = Dimens.PaddingS)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.Jumbo + 8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login as an employee",
                    color = BloodRed,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        navController.navigate("staff_login") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.do_not_have_account) + " "
                )

                Text(
                    stringResource(id = R.string.sign_up),
                    color = BloodRed,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        navController.navigate("signUp") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        if(isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.2f))
                    .pointerInput(Unit) { },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
            }
        }
    }
}