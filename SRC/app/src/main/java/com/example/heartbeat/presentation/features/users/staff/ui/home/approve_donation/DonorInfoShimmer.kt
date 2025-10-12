package com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun DonorInfoShimmer() {
    val transition = rememberInfiniteTransition(label = "donor_shimmer")
    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "donor_shimmer_translate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.4f),
            Color.White.copy(alpha = 0.7f),
            Color.LightGray.copy(alpha = 0.4f)
        ),
        start = Offset(x = shimmerTranslate - 1000f, y = 0f),
        end = Offset(x = shimmerTranslate, y = 0f)
    )

    val rowHeight = Dimens.HeightLarge - 4.dp

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppShape.SmallShape + 2.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = Dimens.PaddingSM, vertical = Dimens.PaddingXS)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(rowHeight)
                    .background(shimmerBrush, CircleShape)
            )

            Spacer(modifier = Modifier.width(AppSpacing.MediumLarge))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(0.8f)
                        .background(shimmerBrush, RoundedCornerShape(AppShape.SmallShape))
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium - 2.dp))

                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(0.1f)
                        .background(shimmerBrush, RoundedCornerShape(AppShape.SmallShape))
                )
            }

            Spacer(modifier = Modifier.width(AppSpacing.Large))

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(Dimens.WidthM)
                    .background(shimmerBrush, RoundedCornerShape(AppShape.SmallShape))
            )

            Spacer(modifier = Modifier.width(AppSpacing.Medium))

            Box(
                modifier = Modifier
                    .size(Dimens.SizeS)
                    .background(shimmerBrush, CircleShape)
            )

            Spacer(modifier = Modifier.width(AppSpacing.Large))
        }

        Divider(color = Color(0xFFE0E0E0))
    }
}