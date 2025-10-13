package com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun StatusScreenComponent(
    bgrColor: Color,
    mainIcon: ImageVector,
    mainIconColor: Color,
    mainIconSize: Dp = Dimens.SizeMega,
    title: String,
    message: String,
    onClick: () -> Unit,
    buttonColor: Color,
    buttonIcon: ImageVector,
    buttonText: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.PaddingL)
            .clip(RoundedCornerShape(AppShape.ExtraLargeShape))
            .background(bgrColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = mainIcon,
            contentDescription = null,
            modifier = Modifier.size(mainIconSize),
            tint = mainIconColor
        )

        Spacer(Modifier.height(AppSpacing.Large))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2B2B2B)
        )

        Spacer(Modifier.height(AppSpacing.Medium))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF707070)
        )

        Spacer(Modifier.height(AppSpacing.ExtraLarge))

        OutlinedButton(
            onClick = onClick,
            border = BorderStroke(1.dp, buttonColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = buttonColor)
        ) {
            Icon(buttonIcon, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text(buttonText)
        }
    }
}