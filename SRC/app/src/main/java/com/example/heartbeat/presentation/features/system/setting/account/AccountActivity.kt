package com.example.heartbeat.presentation.features.system.setting.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.TextPrimaryDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AccountScreen(onBackClick = { finish() })
        }
    }
}

@Composable
fun AccountScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val user = authState?.getOrNull()
    val color = Color(0xFF767E8C)

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                authViewModel.loadCurrentUser()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(Dimens.PaddingM + 2.dp)
            .padding(top = Dimens.PaddingM),
    ) {
        SettingTitle(
            text = stringResource(id = R.string.account),
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(AppSpacing.Jumbo))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingXXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_account),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.8f)),
                modifier = Modifier.size(Dimens.SizeM)
            )

            Spacer(modifier = Modifier.width(AppSpacing.MediumPlus))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.username) + ": ",
                    color = Color.Black.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )

                user?.username?.let {
                    Text(
                        text = it,
                        color = color,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    context.startActivity(Intent(context, ChangeUserNameActivity::class.java))
                }
            ) {
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.Black.copy(0.4f),
                    modifier = Modifier.size(Dimens.SizeSM)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingXXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.8f),
                modifier = Modifier.size(Dimens.SizeM)
            )

            Spacer(modifier = Modifier.width(AppSpacing.MediumPlus))

            Text(
                text = stringResource(R.string.password),
                color = Color.Black.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    context.startActivity(Intent(context, ChangePasswordActivity::class.java))
                }
            ) {
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.Black.copy(0.4f),
                    modifier = Modifier.size(Dimens.SizeSM)
                )
            }
        }
    }
}