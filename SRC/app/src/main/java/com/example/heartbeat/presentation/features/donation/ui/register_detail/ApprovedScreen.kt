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
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material.icons.filled.Visibility
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
import androidx.navigation.NavController
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun ApprovedScreen(
    onViewDetail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.PaddingL)
            .clip(RoundedCornerShape(AppShape.ExtraLargeShape))
            .background(Color(0xFFF4F9FD)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircleOutline,
            contentDescription = null,
            modifier = Modifier.size(Dimens.SizeMega),
            tint = Color(0xFF81C784)
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Text(
            text = stringResource(id = R.string.approved_title),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Text(
            text = stringResource(id = R.string.approved_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color(0xFF707070)
        )

        Spacer(Modifier.height(AppSpacing.ExtraLarge))

        OutlinedButton(
            onClick = { onViewDetail() },
            border = BorderStroke(1.dp, Color(0xFF81C784)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF81C784))
        ) {
            Icon(Icons.Default.Visibility, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text(stringResource(id = R.string.view_details))
        }
    }
}