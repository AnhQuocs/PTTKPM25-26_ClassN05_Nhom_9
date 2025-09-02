package com.example.heartbeat.presentation.features.auth.ui

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
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.FacebookBlue

@Composable
fun LoginScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(Dimens.PaddingM)
            .padding(vertical = Dimens.PaddingUltra),
    ) {
        Text(
            text = stringResource(id = R.string.welcome),
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
            icon = Icons.Default.Email
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
            leadingIcon = Icons.Default.Lock
        )

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {

            },
            modifier = Modifier
                .height(Dimens.HeightDefault)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = BloodRed
            ),
            shape = RoundedCornerShape(AppShape.MediumShape)
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.titleMedium
            )
        }

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
}