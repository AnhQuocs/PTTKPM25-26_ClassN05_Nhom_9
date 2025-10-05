package com.example.heartbeat.presentation.features.event.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun StaffEventCard(
    modifier: Modifier = Modifier,
    gradient: Brush,
    accentColor: Color,
    event: Event,
    location: String,
) {
    val formattedDate = formatDate(event.date)
    val status = calculateEventStatus(event)

    val statusUI = getStatusUI(status)

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.3.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.HeightXL3),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient, shape = RoundedCornerShape(AppShape.ExtraLargeShape)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .height(Dimens.HeightXXS)
                    .clip(RoundedCornerShape(AppShape.SmallShape))
                    .background(statusUI.color, RoundedCornerShape(AppShape.SmallShape))
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingXSPlus)
                ) {
                    Image(
                        painter = painterResource(statusUI.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(Dimens.SizeS)
                    )

                    Spacer(modifier = Modifier.width(AppSpacing.Small))

                    Text(
                        text = stringResource(id = statusUI.text),
                        color = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
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

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = Color(0xFF445668).copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .size(Dimens.SizeSM)
                                        .offset(y = (1.5).dp)
                                )

                                Text(
                                    text = formattedDate,
                                    color = Color(0xFF445668),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                                    modifier = Modifier.padding(start = Dimens.PaddingXXS)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingXXS)
                            ) {
                                Icon(
                                    Icons.Default.PeopleAlt,
                                    contentDescription = null,
                                    tint = Color(0xFF2051E5).copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .size(Dimens.SizeSM)
                                )

                                Text(
                                    text = event.capacity.toString(),
                                    color = Color(0xFF2051E5),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
                                    modifier = Modifier.padding(start = Dimens.PaddingXXS)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(AppSpacing.Medium))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF445668).copy(alpha = 0.8f),
                                modifier = Modifier
                                    .size(Dimens.SizeSM)
                                    .offset(y = (1.5).dp)
                            )

                            Text(
                                text = location,
                                color = Color(0xFF445668),
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                                modifier = Modifier.padding(start = Dimens.PaddingXXS)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(AppSpacing.Medium))

                CircularProgressBar(
                    modifier = Modifier
                        .padding(end = Dimens.PaddingM, top = Dimens.PaddingXL),
                    value = event.donorCount,
                    capacity = event.capacity
                )
            }
        }
    }
}

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    value: Int,
    capacity: Int,
    strokeWidth: Dp = 6.dp,
    animationDuration: Int = 1000
) {
    val targetPercentage = (value.toFloat() / capacity.toFloat()).coerceIn(0f, 1f)

    val previousPercentage = remember { mutableFloatStateOf(targetPercentage) }

    val animatedPercentage by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(targetPercentage) {
        previousPercentage.floatValue = targetPercentage
    }

    val color = when {
        animatedPercentage <= 0.5f -> OceanBlue
        animatedPercentage <= 0.8f -> VividOrange
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
                sweepAngle = 360 * animatedPercentage,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = "${(animatedPercentage * 100).toInt()}%",
            color = Color(0xFF2051E5),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

fun formatDate(dateString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(dateString, inputFormatter)
    return date.format(outputFormatter)
}

enum class EventStatus {
    UPCOMING, ONGOING, ENDED
}

fun calculateEventStatus(event: Event): EventStatus {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val eventDate = LocalDate.parse(event.date, dateFormatter)

    val (startStr, endStr) = event.time.split(" ")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)

    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val currentTime = now.toLocalTime()

    return when {
        eventDate.isBefore(today) -> EventStatus.ENDED
        eventDate.isAfter(today) -> EventStatus.UPCOMING
        else -> when {
            currentTime.isBefore(startTime) -> EventStatus.UPCOMING
            currentTime.isAfter(endTime) -> EventStatus.ENDED
            else -> EventStatus.ONGOING
        }
    }
}

data class StatusUI(
    val text: Int,
    val icon: Int,
    val color: Color
)

fun getStatusUI(status: EventStatus): StatusUI {
    return when (status) {
        EventStatus.UPCOMING -> StatusUI(
            R.string.upcoming,
            R.drawable.ic_upcoming,
            Color(0xFF2196F3)
        )
        EventStatus.ONGOING -> StatusUI(
            R.string.ongoing,
            R.drawable.ic_ongoing,
            Color(0xFF4CAF50)
        )
        EventStatus.ENDED -> StatusUI(
            R.string.ended,
            R.drawable.ic_ended,
            Color(0xFF9E9E9E)
        )
    }
}