package com.example.heartbeat.presentation.features.event.ui.event_detail

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
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.TitleSection
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.main.home.ProgressBar
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.OceanBlue
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun EventInfoCard(eventId: String, eventViewModel: EventViewModel = hiltViewModel()) {
    val events by eventViewModel.events.collectAsState()

    val event = events.find { it.id == eventId } ?: return

    LaunchedEffect(eventId) {
        eventViewModel.observeDonorCount(eventId)
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val (startStr, endStr) = event.time.split(" ")
    val startTime = LocalTime.parse(startStr, timeFormatter)
    val endTime = LocalTime.parse(endStr, timeFormatter)
    val time = "$startTime - $endTime"

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(AppShape.ExtraLargeShape),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingM)
        ) {
            TitleSection(
                text1 = stringResource(id = R.string.event_info),
                text2 = ""
            )

            Spacer(modifier = Modifier.height(AppSpacing.Large))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Title,
                    contentDescription = null,
                    tint = OceanBlue,
                    modifier = Modifier.size(Dimens.SizeSM)
                )

                Spacer(modifier = Modifier.width(AppSpacing.Small))

                Text(
                    text = event.name,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            TextInfoItem(
                text = stringResource(id = R.string.description) + ": " + event.description,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextInfoItem(
                    text = stringResource(id = R.string.time) + ": ",
                    modifier = Modifier.align(Alignment.Top)
                )

                Column {
                    TextInfoItem(text = event.date)

                    TextInfoItem(text = time, fontSize = 14)
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.Small))

            Text(
                text = stringResource(id = R.string.deadline) + ": " + formatDateTime(event.deadline.toString()),
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.Large))

            ProgressBar(value = event.donorCount, capacity = event.capacity)
        }
    }
}

@Composable
fun TextInfoItem(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 15,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val size = fontSize.sp

    Text(
        text = text,
        color = Color.Black,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = size, fontWeight = fontWeight),
        modifier = modifier
    )
}

fun formatDateTime(isoString: String): String {
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")

    val dateTime = LocalDateTime.parse(isoString, inputFormatter)
    return dateTime.format(outputFormatter)
}