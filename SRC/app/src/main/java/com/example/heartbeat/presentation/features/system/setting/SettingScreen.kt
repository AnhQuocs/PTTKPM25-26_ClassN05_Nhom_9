package com.example.heartbeat.presentation.features.system.setting

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.entity.users.DonorAvatar
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.main.home.base64ToImageBitmap
import com.example.heartbeat.presentation.features.system.setting.account.AccountActivity
import com.example.heartbeat.presentation.features.users.auth.viewmodel.AuthViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.TealPrimary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    donationViewModel: DonationViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()
    val avatar by donorViewModel.donorAvatar.collectAsState()
    val user = authState?.getOrNull()

    var showLogoutDialog by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val message = stringResource(id = R.string.help_center_snackbar_message)

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                authState?.getOrNull()?.let { user ->
                    authViewModel.loadCurrentUser()
                    donorViewModel.getCurrentDonor()
                    donorViewModel.getAvatar(user.uid)
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val donatedList by donationViewModel.uiState.map { it.donatedList }.collectAsState(initial = emptyList())

    LaunchedEffect(user) {
        user?.let {
            donationViewModel.getDonatedDonations(donorId = it.uid)
        }
    }

    Log.d("SettingScreen", "Size: ${donatedList.size}")

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.HeightXL),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(id = R.string.setting),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = Dimens.PaddingXL)
                        .fillMaxWidth()
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(Dimens.PaddingS)
            ) { data ->
                Snackbar(
                    modifier = Modifier.padding(horizontal = Dimens.PaddingM),
                    shape = RoundedCornerShape(AppShape.MediumShape),
                    containerColor = TealPrimary,
                    contentColor = Color.White,
                    action = {
                        TextButton(
                            onClick = { data.dismiss() }
                        ) {
                            Text(
                                text = data.visuals.actionLabel ?: "OK",
                                color = Color.Yellow
                            )
                        }
                    }
                ) {
                    Text(
                        text = data.visuals.message,
                        modifier = Modifier.padding(vertical = Dimens.PaddingS)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(paddingValues)
                .padding(Dimens.PaddingM)
                .fillMaxSize()
        ) {
            user?.let {
                UserInfo(user, avatar)
            }

            Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))

            OptionItem(
                iconRes = R.drawable.ic_account,
                text = stringResource(id = R.string.account),
                onClick = {
                    context.startActivity(Intent(context, AccountActivity::class.java))
                }
            )

            OptionItem(
                iconRes = R.drawable.ic_info,
                text = stringResource(id = R.string.personal_info),
                onClick = {
                    val intent = Intent(context, PersonalInformationActivity::class.java)
                    intent.putExtra("donorId", user?.uid)
                    context.startActivity(intent)
                }
            )

            OptionItem(
                iconRes = R.drawable.ic_language,
                text = stringResource(id = R.string.language),
                onClick = {
                    context.startActivity(Intent(context, ChangeLanguageActivity::class.java))
                }
            )

            OptionItem(
                iconRes = R.drawable.ic_history,
                text = stringResource(id = R.string.history),
                onClick = {
                    val intent = Intent(context, DonationHistoryActivity::class.java)
                    intent.putParcelableArrayListExtra("donated_list", ArrayList(donatedList))
                    context.startActivity(intent)
                }
            )

            AppLineGrey(modifier = Modifier.padding(Dimens.PaddingXS))

            OptionItem(
                iconRes = R.drawable.ic_key,
                text = stringResource(id = R.string.privacy_policy),
                onClick = {
                    growingFeaturesSnackBar(scope, snackBarHostState, message = message)
                }
            )

            OptionItem(
                iconRes = R.drawable.ic_help_center,
                text = stringResource(id = R.string.help_center),
                onClick = {
                    context.startActivity(Intent(context, HelpCenterActivity::class.java))
                }
            )

            OptionItem(
                iconRes = R.drawable.ic_logout,
                text = stringResource(id = R.string.logout),
                onClick = { showLogoutDialog = true }
            )

            if (showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        showLogoutDialog = false
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun UserInfo(user: AuthUser, avatar: DonorAvatar?) {
    val imgBitmap = remember(avatar?.avatarUrl) {
        avatar?.avatarUrl?.let { base64ToImageBitmap(it) }
    }

    val avtSize = Dimens.SizeMega + 10.dp

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imgBitmap != null) {
            Image(
                bitmap = imgBitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(avtSize)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_default_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(avtSize)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.MediumLarge))

        user.username?.let {
            Text(
                text = it,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        user.email?.let {
            Text(
                text = it,
                color = Color(0xFF767E8C),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun OptionItem(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    val color = Color(0xFF767E8C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingXXS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(Dimens.SizeM)
        )

        Spacer(modifier = Modifier.width(AppSpacing.MediumPlus))

        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onClick
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

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.logout_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    Toast.makeText(
                        context,
                        context.getString(R.string.logout_success),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(id = R.string.logout))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    stringResource(id = R.string.logout_cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

fun growingFeaturesSnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String,
    actionLabel: String = "OK"
) {
    if (snackBarHostState.currentSnackbarData != null) return

    scope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Short
        )
    }
}