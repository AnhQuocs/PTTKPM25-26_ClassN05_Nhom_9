package com.example.heartbeat.presentation.features.users.staff.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.ui.StaffEventCard
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.ui.theme.AquaMint
import com.example.heartbeat.ui.theme.CoralRed
import com.example.heartbeat.ui.theme.GoldenGlow
import com.example.heartbeat.ui.theme.RoyalPurple
import com.example.heartbeat.ui.theme.SunsetOrange
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarSection(
    eventViewModel: EventViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    donationViewModel: DonationViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }

    val filteredEvents by eventViewModel.filteredEvents.collectAsState()

    val listState = rememberLazyListState()
    val daysInMonth = selectedMonth.lengthOfMonth()
    val startDate = selectedMonth.atDay(1)
    val dates = (0 until daysInMonth).map { startDate.plusDays(it.toLong()) }

    LaunchedEffect(selectedDate) {
        eventViewModel.observeEventsByDate(selectedDate)
    }

    LaunchedEffect(selectedDate, selectedMonth) {
        val index = dates.indexOfFirst { it == selectedDate }.coerceAtLeast(0)
        listState.animateScrollToItem((index - 2).coerceAtLeast(0))
    }

    val gradients = listOf(
        Brush.linearGradient(colors = listOf(Color(0xFFEAEFFF), Color(0xFFFAF9FF))),
        Brush.linearGradient(colors = listOf(Color(0xFFEDFFFB), Color(0xFFFFEBF4))),
        Brush.linearGradient(colors = listOf(Color(0xFFFFFAF3), Color(0xFFECEFFF))),
        Brush.linearGradient(colors = listOf(Color(0xFFEFFFF4), Color(0xFFFFFFEB)))
    )

    val accentColors = listOf(
        SunsetOrange,
        AquaMint,
        RoyalPurple,
        GoldenGlow,
        CoralRed
    )

    LaunchedEffect(Unit) {
        donationViewModel.getAllDonatedDonations()
        hospitalViewModel.loadHospitals()
    }

    Column {
        MonthSelector(
            selectedDate = selectedDate,
            onMonthSelected = {
                selectedMonth = it
                selectedDate = it.atDay(1)
            },
            onTodayClick = {
                selectedDate = LocalDate.now()
                selectedMonth = YearMonth.now()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ScrollableCalendar(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            currentMonth = YearMonth.from(selectedDate),
            listState = listState
        )

        Spacer(modifier = Modifier.height(12.dp))
        AppLineGrey(modifier = Modifier.padding(horizontal = 6.dp))
        Spacer(modifier = Modifier.height(12.dp))

        DateLabel(selectedDate)

        Spacer(modifier = Modifier.height(16.dp))
        AppLineGrey(modifier = Modifier.padding(horizontal = 6.dp))
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(filteredEvents) { index, event ->
                LaunchedEffect(Unit) {
                    hospitalViewModel.loadHospitalById(hospitalId = event.locationId)
                }

                val hospital = hospitalViewModel.hospitalDetails[event.locationId]
                val location = "${hospital?.district}, ${hospital?.province}"

                val gradient = gradients[index % gradients.size]
                val accentColor = accentColors[index % accentColors.size]

                StaffEventCard(
                    gradient = gradient,
                    event = event,
                    accentColor = accentColor,
                    location = location
                )
            }
        }
    }
}

@Composable
fun MonthSelector(
    selectedDate: LocalDate,
    onMonthSelected: (YearMonth) -> Unit,
    onTodayClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    val currentYear = java.time.Year.now().value
    val months = (1..12).map { month ->
        YearMonth.of(currentYear, month)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate.format(formatter),
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .clickable { expanded = true }
            )
        }

        Text(
            "Today",
            color = Color.Blue,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onTodayClick() }
        )
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        months.forEach { month ->
            DropdownMenuItem(
                text = { Text(month.format(formatter)) },
                onClick = {
                    expanded = false
                    onMonthSelected(month)
                }
            )
        }
    }
}


@Composable
fun ScrollableCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    currentMonth: YearMonth,
    listState: LazyListState
) {
    val color = Color(0xFF24A19C)

    val daysInMonth = currentMonth.lengthOfMonth()
    val startDate = currentMonth.atDay(1)
    val dates = (0 until daysInMonth).map { startDate.plusDays(it.toLong()) }

    LazyRow(state = listState) {
        items(dates) { date ->
            val isSelected = date == selectedDate
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .padding(horizontal = 4.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) color else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDateSelected(date) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.name.take(1),
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = date.dayOfMonth.toString(),
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun DateLabel(selectedDate: LocalDate) {
    val today = LocalDate.now()

    val label = if (selectedDate == today) {
        "Today. ${selectedDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}"
    } else {
        "${selectedDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}. ${
            selectedDate.format(
                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
            )
        }"
    }

    Text(
        text = label,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
        fontSize = 16.sp,
        color = Color(0xFF1B1C1F)
    )
}

fun String.toTimeOnly(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm[:ss]")
        val time = java.time.LocalTime.parse(this, formatter)
        time.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        this
    }
}