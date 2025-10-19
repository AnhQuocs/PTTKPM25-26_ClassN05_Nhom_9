package com.example.heartbeat.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    text: String,
    color: Color = BloodRed
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier
            .height(Dimens.HeightDefault)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContentColor = Color.White,
            disabledContainerColor = color.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(AppShape.MediumShape)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}