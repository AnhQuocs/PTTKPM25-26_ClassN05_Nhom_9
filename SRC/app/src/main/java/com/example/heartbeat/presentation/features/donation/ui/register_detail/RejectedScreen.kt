package com.example.heartbeat.presentation.features.donation.ui.register_detail

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun RejectedScreen(
    onRegisterAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.PaddingL)
            .clip(RoundedCornerShape(AppShape.ExtraLargeShape))
            .background(Color(0xFFFDF4F4)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.HighlightOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFFE57373)
        )

        Spacer(Modifier.height(AppSpacing.Large))

        Text(
            text = stringResource(id = R.string.rejected_title),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2B2B2B)
        )

        Spacer(Modifier.height(AppSpacing.Medium))

        Text(
            text = stringResource(id = R.string.rejected_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF707070)
        )

        Spacer(Modifier.height(AppSpacing.ExtraLarge))

        OutlinedButton(
            onClick = onRegisterAgain,
            border = BorderStroke(1.dp, Color(0xFFE57373)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE57373))
        ) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text(stringResource(id = R.string.register_again))
        }
    }
}