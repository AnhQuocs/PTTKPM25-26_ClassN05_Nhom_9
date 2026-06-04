package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class ReadDonationUseCasesTest {
    private lateinit var repository: DonationRepository

    @Before
    fun setUp() {
        repository = mockk()
    }

    // --- 1. GET ALL DONATIONS ---
    @Test
    fun `GetAllDonationsUseCase coverage`() = runTest {
        coEvery { repository.getAllDonations() } returns 100
        assertEquals(100, GetAllDonationsUseCase(repository).invoke().getOrNull())

        coEvery { repository.getAllDonations() } throws Exception("Error")
        assertTrue(GetAllDonationsUseCase(repository).invoke().isFailure)
    }

    // --- 2. GET BY DAY ---
    @Test
    fun `GetDonationsByDayUseCase coverage`() = runTest {
        val date = LocalDate.now()
        coEvery { repository.getDonationsByDay(any()) } returns 5
        
        // Test with param
        assertEquals(5, GetDonationsByDayUseCase(repository).invoke(date).getOrNull())
        // Test default param
        assertEquals(5, GetDonationsByDayUseCase(repository).invoke().getOrNull())

        coEvery { repository.getDonationsByDay(any()) } throws Exception("Error")
        assertTrue(GetDonationsByDayUseCase(repository).invoke().isFailure)
    }

    // --- 3. GET BY WEEK ---
    @Test
    fun `GetDonationsByWeekUseCase coverage`() = runTest {
        coEvery { repository.getDonationsByWeek(any()) } returns 20
        
        // Test with param
        assertEquals(20, GetDonationsByWeekUseCase(repository).invoke(LocalDate.now()).getOrNull())
        // Test default param
        assertEquals(20, GetDonationsByWeekUseCase(repository).invoke().getOrNull())

        coEvery { repository.getDonationsByWeek(any()) } throws Exception("Error")
        assertTrue(GetDonationsByWeekUseCase(repository).invoke().isFailure)
    }

    // --- 4. GET BY MONTH ---
    @Test
    fun `GetDonationsByMonthUseCase coverage`() = runTest {
        coEvery { repository.getDonationsByMonth(any()) } returns 80
        
        // Test with param
        assertEquals(80, GetDonationsByMonthUseCase(repository).invoke(YearMonth.now()).getOrNull())
        // Test default param
        assertEquals(80, GetDonationsByMonthUseCase(repository).invoke().getOrNull())

        coEvery { repository.getDonationsByMonth(any()) } throws Exception("Error")
        assertTrue(GetDonationsByMonthUseCase(repository).invoke().isFailure)
    }

    // --- 5. GET BY DONOR ---
    @Test
    fun `GetDonationsByDonorUseCase coverage`() = runTest {
        val list = listOf(mockk<Donation>())
        coEvery { repository.getDonationsByDonor(any()) } returns list
        assertEquals(list, GetDonationsByDonorUseCase(repository).invoke("d1").getOrNull())

        coEvery { repository.getDonationsByDonor(any()) } throws Exception("Error")
        assertTrue(GetDonationsByDonorUseCase(repository).invoke("d1").isFailure)
    }

    // --- 6. GET ALL LIST ---
    @Test
    fun `GetAllDonationsListUseCase coverage`() = runTest {
        val list = listOf(mockk<Donation>())
        coEvery { repository.getAllDonationsList() } returns list
        assertEquals(list, GetAllDonationsListUseCase(repository).invoke().getOrNull())

        coEvery { repository.getAllDonationsList() } throws Exception("Error")
        assertTrue(GetAllDonationsListUseCase(repository).invoke().isFailure)
    }

    // --- 7. OBSERVABLE FLOWS ---
    @Test
    fun `Observable flows coverage`() = runTest {
        val donation = mockk<Donation>()
        val list = listOf(donation)
        
        every { repository.observeDonationByDonor(any(), any()) } returns flowOf(donation)
        every { repository.observeDonationsByEvent(any()) } returns flowOf(list)
        every { repository.observePendingDonations() } returns flowOf(list)

        ObserveDonationByDonorUseCase(repository).invoke("e1", "d1").collect { assertEquals(donation, it) }
        ObserveDonationsByEventUseCase(repository).invoke("e1").collect { assertEquals(list, it) }
        ObservePendingDonationsUseCase(repository).invoke().collect { assertEquals(list, it) }
    }
}
