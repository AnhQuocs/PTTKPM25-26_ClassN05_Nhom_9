package com.example.heartbeat.presentation.features.system.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.GrayBorder
import com.example.heartbeat.ui.theme.GrayContainer
import com.example.heartbeat.ui.theme.GrayOptionText
import com.example.heartbeat.ui.theme.GrayPlaceholder
import com.example.heartbeat.ui.theme.TealPrimary
import com.example.heartbeat.ui.theme.TextPrimaryDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HelpCenterActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HelpCenterScreen { finish() }
        }
    }
}

@Composable
fun HelpCenterScreen(
    onBackClick: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    val message = stringResource(id = R.string.help_center_snackbar_message)

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
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
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
                .padding(Dimens.PaddingM + 2.dp)
                .padding(top = Dimens.PaddingM),
        ) {
            SettingTitle(
                text = stringResource(id = R.string.help_center_title),
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(AppSpacing.Large))

            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        stringResource(id = R.string.help_center_search_placeholder),
                        fontSize = 16.sp,
                        color = GrayPlaceholder,
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.SizeSM + 2.dp)
                    )
                },
                shape = RoundedCornerShape(AppShape.MediumShape),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = GrayContainer,
                    focusedContainerColor = GrayContainer,
                    disabledContainerColor = GrayContainer,
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.PaddingM)
                    .height(Dimens.HeightLarge - 4.dp)
                    .background(
                        color = GrayBorder,
                        shape = RoundedCornerShape(AppShape.MediumShape)
                    )
                    .border(0.5.dp, color = GrayBorder, RoundedCornerShape(AppShape.MediumShape))
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
                ) {
                    OptionList(
                        R.drawable.platforms,
                        stringResource(id = R.string.help_center_option_platforms),
                        onClick = { growingFeaturesSnackBar(scope, snackBarHostState, message = message) })
                    OptionList(
                        R.drawable.question,
                        stringResource(id = R.string.help_center_option_question),
                        onClick = { growingFeaturesSnackBar(scope, snackBarHostState, message = message) })
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
                ) {
                    OptionList(
                        R.drawable.application,
                        stringResource(id = R.string.help_center_option_app_usage),
                        onClick = { growingFeaturesSnackBar(scope, snackBarHostState, message = message) })
                    OptionList(
                        R.drawable.update_time,
                        stringResource(id = R.string.help_center_option_update_time),
                        onClick = { growingFeaturesSnackBar(scope, snackBarHostState, message = message) })
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
                ) {
                    OptionList(
                        R.drawable.cross,
                        stringResource(id = R.string.help_center_option_cross_platform),
                        onClick = { growingFeaturesSnackBar(scope, snackBarHostState, message = message) })
                    OptionList(
                        R.drawable.reminder,
                        stringResource(id = R.string.help_center_option_update_reminder),
                        onClick = { growingFeaturesSnackBar(scope, snackBarHostState, message = message) })
                }
            }
        }
    }
}

@Composable
fun OptionList(
    @DrawableRes iconResId: Int,
    text: String,
    onClick: () -> Unit
) {
    val painter = painterResource(id = iconResId)

    Box(
        modifier = Modifier
            .padding(vertical = Dimens.PaddingS)
            .height(Dimens.HeightXL3 - 6.dp)
            .width(Dimens.HeightXL3 + 4.dp)
            .clip(RoundedCornerShape(AppShape.MediumShape))
            .background(color = GrayContainer)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(Dimens.SizeXXL + 6.dp)
            )

            Spacer(modifier = Modifier.height(AppSpacing.Small + 2.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                color = GrayOptionText
            )
        }
    }
}