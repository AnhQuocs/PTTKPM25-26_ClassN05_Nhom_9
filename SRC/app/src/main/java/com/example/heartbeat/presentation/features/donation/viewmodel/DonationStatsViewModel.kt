package com.example.heartbeat.presentation.features.donation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

enum class StatsType {
    DAY, WEEK, MONTH
}

@HiltViewModel
class DonationStatsViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases
) : ViewModel() {

    private val _dayCount = MutableStateFlow<Int?>(null)
    val dayCount: StateFlow<Int?> = _dayCount

    private val _weekCount = MutableStateFlow<Int?>(null)
    val weekCount: StateFlow<Int?> = _weekCount

    private val _monthCount = MutableStateFlow<Int?>(null)
    val monthCount: StateFlow<Int?> = _monthCount

    private val _count = MutableStateFlow<Int?>(null)
    val count: StateFlow<Int?> = _count

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _statsType = MutableStateFlow(StatsType.DAY)
    val statsType: StateFlow<StatsType> = _statsType

    private val _selectedDay = MutableStateFlow(LocalDate.now())
    val selectedDay: StateFlow<LocalDate> = _selectedDay

    private val _selectedWeek = MutableStateFlow(LocalDate.now().with(DayOfWeek.MONDAY))
    val selectedWeek: StateFlow<LocalDate> = _selectedWeek

    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth: StateFlow<YearMonth> = _selectedMonth

    private val _allTimeCount = MutableStateFlow(0)
    val allTimeCount: StateFlow<Int> = _allTimeCount


    init {
        loadAllStats()
    }

    fun loadAllStats(forDay: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val weekStart = forDay.with(DayOfWeek.MONDAY)
                val month = YearMonth.from(forDay)

                val dayResult = donationUseCases.getDonationsByDayUseCase(forDay)
                val weekResult = donationUseCases.getDonationsByWeekUseCase(weekStart)
                val monthResult = donationUseCases.getDonationsByMonthUseCase(month)
                val allResult = donationUseCases.getAllDonationsUseCase()

                _dayCount.value = dayResult
                _weekCount.value = weekResult
                _monthCount.value = monthResult
                _allTimeCount.value = allResult

                _count.value = when (_statsType.value) {
                    StatsType.DAY -> dayResult
                    StatsType.WEEK -> weekResult
                    StatsType.MONTH -> monthResult
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setStatsType(type: StatsType) {
        _statsType.value = type
        _count.value = when (type) {
            StatsType.DAY -> _dayCount.value
            StatsType.WEEK -> _weekCount.value
            StatsType.MONTH -> _monthCount.value
        }
    }

    fun setSelectedDay(day: LocalDate) {
        _selectedDay.value = day
        _selectedWeek.value = day.with(DayOfWeek.MONDAY)
        _selectedMonth.value = YearMonth.from(day)

        loadAllStats(forDay = day)

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val dayResult = donationUseCases.getDonationsByDayUseCase(day)
                val weekStart = day.with(DayOfWeek.MONDAY)
                val month = YearMonth.from(day)

                val weekResult = donationUseCases.getDonationsByWeekUseCase(weekStart)
                val monthResult = donationUseCases.getDonationsByMonthUseCase(month)

                _dayCount.value = dayResult
                _weekCount.value = weekResult
                _monthCount.value = monthResult
            } finally {
                _isLoading.value = false
            }
        }
    }

}