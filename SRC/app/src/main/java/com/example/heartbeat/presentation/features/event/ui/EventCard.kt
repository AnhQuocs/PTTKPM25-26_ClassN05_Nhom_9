package com.example.heartbeat.presentation.features.event.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.OceanBlue
import com.example.heartbeat.ui.theme.VividOrange
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    gradient: Brush,
    accentColor: Color,
    event: Event
) {
    val formattedDate = formatDate(event.date)

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.3.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.HeightXL3),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient, shape = RoundedCornerShape(AppShape.ExtraLargeShape))
                .padding(vertical = Dimens.PaddingSM),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = Dimens.PaddingXS)
                        .height(30.dp)
                        .width(4.dp)
                        .clip(CutCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                        .background(accentColor)
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingS)
                ) {
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Small))

                    Text(
                        text = event.description,
                        color = Color.Black.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Large))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.PaddingS)
                    ) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.capacity),
                                style = MaterialTheme.typography.titleSmall
                            )

                            Text(
                                text = event.capacity.toString(),
                                color = Color(0xFF2051E5),
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
                                modifier = Modifier.padding(start = Dimens.PaddingXXS)
                            )
                        }

                        Column {
                            Text(
                                text = stringResource(id = R.string.date),
                                color = Color(0xFF445668),
                                style = MaterialTheme.typography.titleSmall
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = Color(0xFF445668).copy(alpha = 0.8f),
                                    modifier = Modifier.size(Dimens.SizeSM).offset(y = (1.5 ).dp)
                                )

                                Text(
                                    text = formattedDate,
                                    color = Color(0xFF445668),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                                    modifier = Modifier.padding(start = Dimens.PaddingXXS)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(AppSpacing.Medium))

            CircularProgressBar(
                modifier = Modifier
                    .padding(end = Dimens.PaddingM, top = Dimens.PaddingM),
                value = event.donorCount,
                capacity = event.capacity
            )
        }
    }
}

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    value: Int,
    capacity: Int,
    strokeWidth: Dp = 6.dp
) {
    val percentage = (value.toFloat() / capacity.toFloat()).coerceIn(0f, 1f)

    val color = when {
        percentage <= 0.5f -> OceanBlue
        percentage <= 0.8f -> VividOrange
        else -> BloodRed
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(Dimens.SizeXXLPlus)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.White,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * percentage,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(percentage * 100).toInt()}%",
                color = Color(0xFF2051E5),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

fun formatDate(dateString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(dateString, inputFormatter)
    return date.format(outputFormatter)
}