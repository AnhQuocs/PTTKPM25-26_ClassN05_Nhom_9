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
        assertEquals("Donation added successfully", viewModel.uiState.value.successMessage)
        assertFalse(viewModel.uiState.value.isLoading)

        coEvery { mockAddDonationUseCase(any()) } returns Result.failure(Exception("Add Fail"))
        viewModel.addDonation(testDonation)
        advanceUntilIdle()
        assertEquals("Add Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test getDonationsByDonor success and failure`() = runTest {
        val donations = listOf(testDonation)
        coEvery { mockGetDonationsByDonorUseCase("donor1") } returns Result.success(donations)
        viewModel.getDonationsByDonor("donor1")
        advanceUntilIdle()
        assertEquals(donations, viewModel.uiState.value.donations)

        coEvery { mockGetDonationsByDonorUseCase("donor1") } returns Result.failure(Exception("Fetch Fail"))
        viewModel.getDonationsByDonor("donor1")
        advanceUntilIdle()
        assertEquals("Fetch Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test getDonatedDonations success and failure`() = runTest {
        val d1 = testDonation.copy(status = "DONATED")
        val d2 = testDonation.copy(status = "PENDING")
        coEvery { mockGetDonationsByDonorUseCase("donor1") } returns Result.success(listOf(d1, d2))
        
        viewModel.getDonatedDonations("donor1")
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.donatedList.size)
        assertEquals(d1, viewModel.uiState.value.donatedList[0])

        coEvery { mockGetDonationsByDonorUseCase("donor1") } returns Result.failure(Exception("Donated Fetch Fail"))
        viewModel.getDonatedDonations("donor1")
        advanceUntilIdle()
        assertEquals("Donated Fetch Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test updateStatus success and failure`() = runTest {
        val d1 = testDonation.copy(donationId = "D1")
        val d2 = testDonation.copy(donationId = "D2")
        coEvery { mockGetDonationsByDonorUseCase(any()) } returns Result.success(listOf(d1, d2))
        viewModel.getDonationsByDonor("u1")
        advanceUntilIdle()

        val updated = d1.copy(status = "SUCCESS")
        coEvery { mockUpdateStatusUseCase("D1", "SUCCESS") } returns Result.success(updated)
        
        viewModel.updateStatus("D1", "SUCCESS")
        advanceUntilIdle()
        assertEquals("Status updated", viewModel.uiState.value.successMessage)
        assertEquals("SUCCESS", viewModel.uiState.value.donations.find { it.donationId == "D1" }?.status)
        assertEquals("PENDING", viewModel.uiState.value.donations.find { it.donationId == "D2" }?.status)

        coEvery { mockUpdateStatusUseCase("D1", "FAIL") } returns Result.failure(Exception("Update Fail"))
        viewModel.updateStatus("D1", "FAIL")
        advanceUntilIdle()
        assertEquals("Update Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test updateDonationVolume success and failure`() = runTest {
        val d1 = testDonation.copy(donationId = "D1", donationVolume = "100")
        coEvery { mockGetDonationsByDonorUseCase(any()) } returns Result.success(listOf(d1))
        viewModel.getDonationsByDonor("u1")
        advanceUntilIdle()

        val updatedVolume = d1.copy(donationVolume = "450")
        val updatedStatus = updatedVolume.copy(status = "DONATED")
        
        coEvery { mockUpdateDonationVolumeUseCase("D1", "450") } returns Result.success(updatedVolume)
        coEvery { mockUpdateStatusUseCase("D1", "DONATED") } returns Result.success(updatedStatus)
        
        viewModel.updateDonationVolume("D1", "450")
        advanceUntilIdle()
        
        assertEquals("Volume updated", viewModel.uiState.value.successMessage)
        assertEquals("450", viewModel.uiState.value.donations[0].donationVolume)
        assertFalse(viewModel.uiState.value.isLoading)

        coEvery { mockUpdateDonationVolumeUseCase("D1", "450") } returns Result.failure(Exception("Volume Fail"))
        viewModel.updateDonationVolume("D1", "450")
        advanceUntilIdle()
        assertEquals("Volume Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test approveDonation success and failure`() = runTest {
        coEvery { mockApproveDonationUseCase("D1", "donor1") } returns Result.success(Unit)
        
        val d1 = testDonation.copy(donationId = "D1")
        val d2 = testDonation.copy(donationId = "D2")
        coEvery { mockGetDonationsByDonorUseCase(any()) } returns Result.success(listOf(d1, d2))
        viewModel.getDonationsByDonor("u1")
        advanceUntilIdle()
        
        viewModel.approveDonation("D1", "donor1")
        advanceUntilIdle()
        
        assertEquals("Donation approved", viewModel.uiState.value.successMessage)
        assertEquals("APPROVED", viewModel.uiState.value.donations.find { it.donationId == "D1" }?.status)
        assertEquals("PENDING", viewModel.uiState.value.donations.find { it.donationId == "D2" }?.status)

        coEvery { mockApproveDonationUseCase("D1", "donor1") } returns Result.failure(Exception("Approve Fail"))
        viewModel.approveDonation("D1", "donor1")
        advanceUntilIdle()
        assertEquals("Approve Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test deleteDonation success and failure`() = runTest {
        coEvery { mockDeleteDonationUseCase("D1") } returns Result.success(Unit)
        
        val d1 = testDonation.copy(donationId = "D1")
        coEvery { mockGetDonationsByDonorUseCase(any()) } returns Result.success(listOf(d1))
        viewModel.getDonationsByDonor("u1")
        advanceUntilIdle()

        viewModel.deleteDonation("D1")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.donations.isEmpty())
        assertEquals("Donation deleted", viewModel.uiState.value.successMessage)

        coEvery { mockDeleteDonationUseCase("D1") } returns Result.failure(Exception("Delete Fail"))
        viewModel.deleteDonation("D1")
        advanceUntilIdle()
        assertEquals("Delete Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test observePendingDonations`() = runTest {
        val donations = listOf(testDonation)
        coEvery { mockObservePendingDonationsUseCase() } returns flowOf(donations)
        
        viewModel.observePendingDonations()
        advanceUntilIdle()
        assertEquals(donations, viewModel.uiState.value.donations)
    }

    @Test
    fun `test observeDonationsByEvent success, filtering and failure`() = runTest {
        val eventId = "E1"
        val d1 = testDonation.copy(eventId = eventId, donationId = "MATCH")
        val d2 = testDonation.copy(eventId = "OTHER", donationId = "MISMATCH")
        val donations = listOf(d1, d2)
        
        coEvery { mockObserveDonationsByEventUseCase(eventId) } returns flowOf(donations)
        
        viewModel.observeDonationsByEvent(eventId)
        viewModel.observeDonationsByEvent(eventId)
        
        advanceUntilIdle()
        
        assertEquals(1, viewModel.uiState.value.donations.size)
        assertEquals("MATCH", viewModel.uiState.value.donations[0].donationId)

        coEvery { mockObserveDonationsByEventUseCase(eventId) } returns flow { throw Exception("Observe Fail") }
        viewModel.observeDonationsByEvent(eventId)
        advanceUntilIdle()
        assertEquals("Observe Fail", viewModel.uiState.value.errorMessage)
        
        coEvery { mockObserveDonationsByEventUseCase(eventId) } returns flow { throw Exception() }
        viewModel.observeDonationsByEvent(eventId)
        advanceUntilIdle()
        assertEquals("Unknown error", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test getAllDonatedDonations success and failure`() = runTest {
        val d1 = testDonation.copy(status = "DONATED")
        val d2 = testDonation.copy(status = "PENDING")
        coEvery { mockGetAllDonationsListUseCase() } returns Result.success(listOf(d1, d2))
        
        viewModel.getAllDonatedDonations()
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.donatedList.size)
        assertEquals(d1, viewModel.uiState.value.donatedList[0])

        coEvery { mockGetAllDonationsListUseCase() } returns Result.failure(Exception("All Donated Fail"))
        viewModel.getAllDonatedDonations()
        advanceUntilIdle()
        assertEquals("All Donated Fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test observeDonationForDonor`() = runTest {
        coEvery { mockObserveDonationByDonorUseCase("E1", "donor1") } returns flowOf(testDonation)
        
        viewModel.observeDonationForDonor("E1", "donor1")
        advanceUntilIdle()
        assertEquals(testDonation, viewModel.uiState.value.selectedDonation)
    }

    @Test
    fun `test clearMessages`() = runTest {
        coEvery { mockAddDonationUseCase(any()) } returns Result.failure(Exception("Err"))
        viewModel.addDonation(testDonation)
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.errorMessage)
        
        viewModel.clearMessages()
        assertNull(viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.successMessage)
    }
}
