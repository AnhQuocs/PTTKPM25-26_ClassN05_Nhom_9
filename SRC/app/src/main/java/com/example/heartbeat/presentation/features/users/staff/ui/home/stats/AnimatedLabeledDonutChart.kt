package com.example.heartbeat.presentation.features.users.staff.ui.home.stats

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedLabeledDonutChart(
    data: List<Int>,
    colors: List<Color>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    baseStrokeWidth: Float = 40f,
    maxExtraWidth: Float = 12f,
    animationDuration: Int = 1200,
    centerTotal: Int? = null,
    centerSubText: String,
    showLines: Boolean = true
) {
    val totalForSlices = data.sum().toFloat()
    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    val animatedTotal = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(data) {
        launch {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = FastOutSlowInEasing
                )
            )
        }
        launch {
            animatedTotal.animateTo(
                targetValue = centerTotal?.toFloat() ?: totalForSlices,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        var startAngle = -90f

        // Vẽ donut
        data.forEachIndexed { index, value ->
            val percent = if (totalForSlices > 0f) value / totalForSlices else 0f
            val sweepAngle = 360 * percent * animatedProgress.value
            val strokeWidth = baseStrokeWidth + percent * maxExtraWidth
            val extra = (strokeWidth - baseStrokeWidth) / 2
            val outerRadius = radius + extra

            val topLeftOffset = Offset(center.x - outerRadius, center.y - outerRadius)
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeftOffset,
                size = Size(outerRadius * 2, outerRadius * 2),
                style = Stroke(width = strokeWidth)
            )
            startAngle += 360 * percent
        }

        // Vẽ chữ ở giữa
        drawContext.canvas.nativeCanvas.apply {
            val centerValue = animatedTotal.value.toInt()

            val totalPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 64f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
            val subTextPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
            }

            val totalY = center.y - (totalPaint.descent() + totalPaint.ascent()) / 2 - 12f
            drawText(centerValue.toString(), center.x, totalY, totalPaint)

            val subY = totalY + 42f
            drawText(centerSubText, center.x, subY, subTextPaint)
        }

        // showLines = true thì vẽ line + label
        if (showLines) {
            startAngle = -90f
            data.forEachIndexed { index, value ->
                if (value <= 0) return@forEachIndexed

                val percent = if (totalForSlices > 0f) value / totalForSlices else 0f
                val sweepAngle = 360 * percent * animatedProgress.value
                val strokeWidth = baseStrokeWidth + percent * maxExtraWidth
                val extra = (strokeWidth - baseStrokeWidth) / 2
                val outerRadius = radius + extra

                if (animatedProgress.value > 0.7f) {
                    val middleAngle = startAngle + (sweepAngle / 2)
                    val rad = Math.toRadians(middleAngle.toDouble())

                    val lineStart = Offset(
                        x = (center.x + cos(rad) * outerRadius).toFloat(),
                        y = (center.y + sin(rad) * outerRadius).toFloat()
                    )
                    val lineMid = Offset(
                        x = (center.x + cos(rad) * (outerRadius + 40f)).toFloat(),
                        y = (center.y + sin(rad) * (outerRadius + 40f)).toFloat()
                    )
                    val isRightSide =
                        middleAngle % 360f in -90f..90f || middleAngle % 360f in 270f..450f
                    val lineEndX = if (isRightSide) lineMid.x + 100f else lineMid.x - 100f
                    val lineEnd = Offset(lineEndX, lineMid.y)

                    val lineAlpha = ((animatedProgress.value - 0.7f) * 3.3f).coerceIn(0f, 1f)
                    val segmentColor = colors[index % colors.size]

                    drawLine(
                        color = segmentColor.copy(alpha = lineAlpha),
                        start = lineStart,
                        end = lineMid,
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = segmentColor.copy(alpha = lineAlpha),
                        start = lineMid,
                        end = lineEnd,
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )

                    drawContext.canvas.nativeCanvas.apply {
                        val paint = android.graphics.Paint().apply {
                            color = segmentColor.copy(alpha = lineAlpha).toArgb()
                            textSize = 36f
                            textAlign = if (isRightSide) android.graphics.Paint.Align.LEFT
                            else android.graphics.Paint.Align.RIGHT
                            alpha = (lineAlpha * 255).toInt()
                        }

                        val labelX = (lineMid.x + lineEnd.x) / 2.2f
                        val labelY = lineMid.y - 12f
                        drawText(labels[index], labelX, labelY, paint)
                    }
                }

                startAngle += 360 * percent
            }
        }
    }
}