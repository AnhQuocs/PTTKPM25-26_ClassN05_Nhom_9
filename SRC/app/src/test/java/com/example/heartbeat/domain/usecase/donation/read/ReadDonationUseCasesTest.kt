package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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

    @Test
    fun `GetAllDonationsUseCase should return count`() = runBlocking {
        coEvery { repository.getAllDonations() } returns 100
        Assert.assertEquals(100, GetAllDonationsUseCase(repository).invoke())
    }

    @Test
    fun `GetDonationsByDayUseCase with parameter should return count`() = runBlocking {
        val date = LocalDate.now()
        coEvery { repository.getDonationsByDay(date) } returns 5
        Assert.assertEquals(5, GetDonationsByDayUseCase(repository).invoke(date))
    }

    @Test
    fun `GetDonationsByDayUseCase without parameter should use default date`() = runBlocking {
        coEvery { repository.getDonationsByDay(any()) } returns 5
        Assert.assertEquals(5, GetDonationsByDayUseCase(repository).invoke())
    }

    @Test
    fun `GetDonationsByWeekUseCase with parameter should return count`() = runBlocking {
        val date = LocalDate.now()
        coEvery { repository.getDonationsByWeek(any()) } returns 20
        Assert.assertEquals(20, GetDonationsByWeekUseCase(repository).invoke(date))
    }

    @Test
    fun `GetDonationsByWeekUseCase without parameter should use default week`() = runBlocking {
        coEvery { repository.getDonationsByWeek(any()) } returns 20
        Assert.assertEquals(20, GetDonationsByWeekUseCase(repository).invoke())
    }

    @Test
    fun `GetDonationsByMonthUseCase with parameter should return count`() = runBlocking {
        val month = YearMonth.now()
        coEvery { repository.getDonationsByMonth(month) } returns 80
        Assert.assertEquals(80, GetDonationsByMonthUseCase(repository).invoke(month))
    }

    @Test
    fun `GetDonationsByMonthUseCase without parameter should use default month`() = runBlocking {
        coEvery { repository.getDonationsByMonth(any()) } returns 80
        Assert.assertEquals(80, GetDonationsByMonthUseCase(repository).invoke())
    }

    @Test
    fun `GetDonationsByDonorUseCase should return list`() = runBlocking {
        val mockList = listOf(mockk<Donation>())
        coEvery { repository.getDonationsByDonor("donor123") } returns mockList
        Assert.assertEquals(mockList, GetDonationsByDonorUseCase(repository).invoke("donor123"))
    }

    @Test
    fun `ObserveDonationByDonorUseCase should return flow`() = runBlocking {
        val mockDonation = mockk<Donation>()
        every { repository.observeDonationByDonor("event123", "donor123") } returns flowOf(
            mockDonation
        )

        ObserveDonationByDonorUseCase(repository).invoke("event123", "donor123").collect {
            Assert.assertEquals(mockDonation, it)
        }
    }

    @Test
    fun `ObserveDonationsByEventUseCase should return flow`() = runBlocking {
        val mockList = listOf(mockk<Donation>())
        every { repository.observeDonationsByEvent("event123") } returns flowOf(mockList)
        ObserveDonationsByEventUseCase(repository).invoke("event123").collect {
            Assert.assertEquals(mockList, it)
        }
    }

    @Test
    fun `ObservePendingDonationsUseCase should return flow`() = runBlocking {
        val mockList = listOf(mockk<Donation>())
        every { repository.observePendingDonations() } returns flowOf(mockList)
        ObservePendingDonationsUseCase(repository).invoke().collect {
            Assert.assertEquals(mockList, it)
        }
    }

    @Test
    fun `GetAllDonationsListUseCase should return list`() = runBlocking {
        val mockList = listOf(mockk<Donation>())
        coEvery { repository.getAllDonationsList() } returns mockList
        Assert.assertEquals(mockList, GetAllDonationsListUseCase(repository).invoke())
    }
}