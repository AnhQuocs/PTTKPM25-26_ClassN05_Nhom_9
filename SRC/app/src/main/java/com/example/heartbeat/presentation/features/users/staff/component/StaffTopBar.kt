package com.example.heartbeat.presentation.features.users.staff.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun StaffTopBar(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.HeightXL2)
            .background(
                color = Color(0xFF2051E5),
                RoundedCornerShape(
                    bottomStart = AppShape.SuperRoundedShape,
                    bottomEnd = AppShape.SuperRoundedShape
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = AppShape.SuperRoundedShape,
                    bottomEnd = AppShape.SuperRoundedShape
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bgr_top_bar),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = Dimens.PaddingM),
        )
    }
}