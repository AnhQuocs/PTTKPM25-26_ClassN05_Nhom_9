package com.example.heartbeat.presentation.features.donation.viewmodel

import android.util.Log
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import com.example.heartbeat.domain.usecase.donation.create.AddDonationUseCase
import com.example.heartbeat.domain.usecase.donation.delete.DeleteDonationUseCase
import com.example.heartbeat.domain.usecase.donation.read.*
import com.example.heartbeat.domain.usecase.donation.update.ApproveDonationUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationVolumeUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateStatusUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class DonationViewModelTest {

    private lateinit var viewModel: DonationViewModel
    private lateinit var donationUseCases: DonationUseCases
    private val testDispatcher = StandardTestDispatcher()

    private val mockAddDonationUseCase = mockk<AddDonationUseCase>(relaxed = true)
    private val mockGetDonationsByDonorUseCase = mockk<GetDonationsByDonorUseCase>(relaxed = true)
    private val mockUpdateStatusUseCase = mockk<UpdateStatusUseCase>(relaxed = true)
    private val mockUpdateDonationVolumeUseCase = mockk<UpdateDonationVolumeUseCase>(relaxed = true)
    private val mockDeleteDonationUseCase = mockk<DeleteDonationUseCase>(relaxed = true)
    private val mockObservePendingDonationsUseCase = mockk<ObservePendingDonationsUseCase>(relaxed = true)
    private val mockObserveDonationsByEventUseCase = mockk<ObserveDonationsByEventUseCase>(relaxed = true)
    private val mockObserveDonationByDonorUseCase = mockk<ObserveDonationByDonorUseCase>(relaxed = true)
    private val mockGetDonationsByDayUseCase = mockk<GetDonationsByDayUseCase>(relaxed = true)
    private val mockGetDonationsByWeekUseCase = mockk<GetDonationsByWeekUseCase>(relaxed = true)
    private val mockGetDonationsByMonthUseCase = mockk<GetDonationsByMonthUseCase>(relaxed = true)
    private val mockGetAllDonationsUseCase = mockk<GetAllDonationsUseCase>(relaxed = true)
    private val mockGetAllDonationsListUseCase = mockk<GetAllDonationsListUseCase>(relaxed = true)
    private val mockApproveDonationUseCase = mockk<ApproveDonationUseCase>(relaxed = true)

    private val fixedNow = LocalDateTime.of(2024, 5, 30, 10, 0)
    private val testDonation = Donation(
        donationId = "D001",
        donorId = "donor001",
        eventId = "event001",
        citizenId = "citizen001",
        status = "PENDING",
        donationVolume = "500",
        createAt = fixedNow,
        donatedAt = "2024-05-30"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        donationUseCases = DonationUseCases(
            addDonation = mockAddDonationUseCase,
            getDonationsByDonor = mockGetDonationsByDonorUseCase,
            updateStatus = mockUpdateStatusUseCase,
            updateDonationVolume = mockUpdateDonationVolumeUseCase,
            deleteDonation = mockDeleteDonationUseCase,
            observePendingDonations = mockObservePendingDonationsUseCase,
            observeDonationsByEvent = mockObserveDonationsByEventUseCase,
            observeDonationByDonorUseCase = mockObserveDonationByDonorUseCase,
            getDonationsByDayUseCase = mockGetDonationsByDayUseCase,
            getDonationsByWeekUseCase = mockGetDonationsByWeekUseCase,
            getDonationsByMonthUseCase = mockGetDonationsByMonthUseCase,
            getAllDonationsUseCase = mockGetAllDonationsUseCase,
            getAllDonationsListUseCase = mockGetAllDonationsListUseCase,
            approveDonationUseCase = mockApproveDonationUseCase
        )

        viewModel = DonationViewModel(donationUseCases)
    }

    @Test
    fun `test add donation success and failure`() = runTest {
        coEvery { mockAddDonationUseCase(any()) } returns Result.success(testDonation)
        viewModel.addDonation(testDonation)
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.donations.contains(testDonation))
        assertFalse(viewModel.uiState.value.isLoading)

        coEvery { mockAddDonationUseCase(any()) } returns Result.failure(Exception("Add Fail"))
        viewModel.addDonation(testDonation)
        advanceUntilIdle()
        assertEquals("Add Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test update status map branches (if and else)`() = runTest {
        val d1 = testDonation.copy(donationId = "D1")
        val d2 = testDonation.copy(donationId = "D2")
        coEvery { mockGetDonationsByDonorUseCase(any()) } returns listOf(d1, d2)
        viewModel.getDonationsByDonor("u1")
        advanceUntilIdle()

        val updated = d1.copy(status = "SUCCESS")
        coEvery { mockUpdateStatusUseCase("D1", any()) } returns Result.success(updated)
        
        viewModel.updateStatus("D1", "SUCCESS")
        advanceUntilIdle()

        val list = viewModel.uiState.value.donations
        assertEquals("SUCCESS", list.find { it.donationId == "D1" }?.status)
        assertEquals("PENDING", list.find { it.donationId == "D2" }?.status)
    }

    @Test
    fun `test observe donations by event coverage`() = runTest {
        val eventId = "E1"
        val donations = listOf(testDonation.copy(eventId = eventId), testDonation.copy(eventId = "OTHER"))
        
        coEvery { mockObserveDonationsByEventUseCase(eventId) } returns flowOf(donations)
        viewModel.observeDonationsByEvent(eventId)
        viewModel.observeDonationsByEvent(eventId)
        
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.donations.size)

        coEvery { mockObserveDonationsByEventUseCase(eventId) } returns flow { throw Exception("Err") }
        viewModel.observeDonationsByEvent(eventId)
        advanceUntilIdle()
        assertEquals("Err", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test other operations success and failure`() = runTest {
        coEvery { mockGetAllDonationsListUseCase() } throws Exception("Fatal")
        viewModel.getAllDonatedDonations()
        advanceUntilIdle()
        assertEquals("Fatal", viewModel.uiState.value.errorMessage)

        coEvery { mockDeleteDonationUseCase(any()) } returns Result.success(Unit)
        viewModel.deleteDonation("D1")
        advanceUntilIdle()
    }

    @Test
    fun `test clear messages`() = runTest {
        viewModel.clearMessages()
        assertNull(viewModel.uiState.value.errorMessage)
    }
}
