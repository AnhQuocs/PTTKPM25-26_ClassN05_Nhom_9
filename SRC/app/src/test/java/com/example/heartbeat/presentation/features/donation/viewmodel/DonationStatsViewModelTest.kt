package com.example.heartbeat.presentation.features.donation.viewmodel

import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DonationStatsViewModelTest {

    private lateinit var donationUseCases: DonationUseCases
    private lateinit var viewModel: DonationStatsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        donationUseCases = mockk(relaxed = true)
        
        // Cập nhật để trả về Result.success thay vì Int trực tiếp
        coEvery { donationUseCases.getDonationsByDayUseCase(any()) } returns Result.success(10)
        coEvery { donationUseCases.getDonationsByWeekUseCase(any()) } returns Result.success(50)
        coEvery { donationUseCases.getDonationsByMonthUseCase(any()) } returns Result.success(200)
        coEvery { donationUseCases.getAllDonationsUseCase() } returns Result.success(1000)

        viewModel = DonationStatsViewModel(donationUseCases)
    }

    @Test
    fun `test init should load all stats`() = runTest {
        advanceUntilIdle()
        assertEquals(10, viewModel.dayCount.value)
        assertEquals(50, viewModel.weekCount.value)
        assertEquals(200, viewModel.monthCount.value)
        assertEquals(1000, viewModel.allTimeCount.value)
        assertEquals(10, viewModel.count.value)
    }

    @Test
    fun `test set stats type updates count for all types`() = runTest {
        advanceUntilIdle()
        
        viewModel.setStatsType(StatsType.WEEK)
        assertEquals(50, viewModel.count.value)
        assertEquals(StatsType.WEEK, viewModel.statsType.value)

        viewModel.setStatsType(StatsType.MONTH)
        assertEquals(200, viewModel.count.value)

        viewModel.setStatsType(StatsType.DAY)
        assertEquals(10, viewModel.count.value)
    }

    @Test
    fun `test load all stats with different types to cover when branches`() = runTest {
        viewModel.setStatsType(StatsType.WEEK)
        viewModel.loadAllStats()
        advanceUntilIdle()
        assertEquals(50, viewModel.count.value)

        viewModel.setStatsType(StatsType.MONTH)
        viewModel.loadAllStats()
        advanceUntilIdle()
        assertEquals(200, viewModel.count.value)
        
        viewModel.setStatsType(StatsType.DAY)
        viewModel.loadAllStats()
        advanceUntilIdle()
        assertEquals(10, viewModel.count.value)
    }

    @Test
    fun `test set selected day syncs dates and reloads`() = runTest {
        val date = LocalDate.of(2024, 6, 15) // Saturday
        viewModel.setSelectedDay(date)
        advanceUntilIdle()

        assertEquals(date, viewModel.selectedDay.value)
        assertEquals(LocalDate.of(2024, 6, 10), viewModel.selectedWeek.value) 
        assertEquals(YearMonth.of(2024, 6), viewModel.selectedMonth.value)
        
        coVerify(atLeast = 1) { donationUseCases.getDonationsByDayUseCase(date) }
    }

    @Test
    fun `test load all stats error management`() = runTest {
        // Cập nhật: Giả lập trả về Result.failure thay vì ném Exception
        coEvery { donationUseCases.getDonationsByDayUseCase(any()) } returns Result.failure(Exception("Error"))
        
        viewModel.loadAllStats()
        assertTrue(viewModel.isLoading.value)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
        // Khi lỗi, count sẽ là null do dùng .getOrNull() trong ViewModel
        assertNull(viewModel.dayCount.value)
    }

    @Test
    fun `test set selected day error management`() = runTest {
        val date = LocalDate.now()
        coEvery { donationUseCases.getDonationsByDayUseCase(date) } returns Result.failure(Exception("Error"))
        
        viewModel.setSelectedDay(date)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.dayCount.value)
    }
}
