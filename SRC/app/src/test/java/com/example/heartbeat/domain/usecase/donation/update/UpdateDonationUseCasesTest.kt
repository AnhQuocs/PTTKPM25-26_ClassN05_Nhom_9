package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateDonationUseCasesTest {
    private lateinit var repository: DonationRepository
    private lateinit var updateStatusUseCase: UpdateStatusUseCase
    private lateinit var updateDonationVolumeUseCase: UpdateDonationVolumeUseCase
    private lateinit var approveDonationUseCase: ApproveDonationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        updateStatusUseCase = UpdateStatusUseCase(repository)
        updateDonationVolumeUseCase = UpdateDonationVolumeUseCase(repository)
        approveDonationUseCase = ApproveDonationUseCase(repository)
    }

    // --- 1. UPDATE STATUS TESTING ---

    @Test
    fun `UpdateStatus with blank donationId should return EmptyDonationId`() = runTest {
        val result = updateStatusUseCase("   ", "COMPLETED")
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyDonationId, result.exceptionOrNull())
        coVerify(exactly = 0) { repository.updateStatus(any(), any()) }
    }

    @Test
    fun `UpdateStatus with blank status should return InvalidStatus`() = runTest {
        val result = updateStatusUseCase("id1", "   ")
        assertTrue(result.isFailure)
        assertEquals(DonationException.InvalidStatus, result.exceptionOrNull())
    }

    @Test
    fun `UpdateStatus success should call repository once and return success result`() = runTest {
        val mockDonation = mockk<Donation>()
        coEvery { repository.updateStatus("id1", "COMPLETED") } returns mockDonation
        
        val result = updateStatusUseCase("id1", "COMPLETED")
        
        assertTrue(result.isSuccess)
        assertEquals(mockDonation, result.getOrNull())
        coVerify(exactly = 1) { repository.updateStatus("id1", "COMPLETED") }
    }

    @Test
    fun `UpdateStatus should handle repository exceptions`() = runTest {
        coEvery { repository.updateStatus(any(), any()) } throws Exception("Network error")
        val result = updateStatusUseCase("id1", "COMPLETED")
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `UpdateStatus when repository returns null should return failure result`() = runTest {
        coEvery { repository.updateStatus(any(), any()) } returns null
        val result = updateStatusUseCase("id1", "COMPLETED")
        assertTrue(result.isFailure)
        assertEquals("Failed to update status", result.exceptionOrNull()?.message)
    }

    // --- 2. UPDATE VOLUME TESTING ---

    @Test
    fun `UpdateVolume with blank donationId should return EmptyDonationId`() = runTest {
        val result = updateDonationVolumeUseCase("   ", "250")
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyDonationId, result.exceptionOrNull())
        coVerify(exactly = 0) { repository.updateDonationVolume(any(), any()) }
    }

    @Test
    fun `UpdateVolume with invalid volume string should return InvalidVolume`() = runTest {
        val result = updateDonationVolumeUseCase("id1", "abc")
        assertTrue(result.isFailure)
        assertEquals(DonationException.InvalidVolume, result.exceptionOrNull())
    }

    @Test
    fun `UpdateVolume with zero or negative volume should return InvalidVolume`() = runTest {
        assertEquals(DonationException.InvalidVolume, updateDonationVolumeUseCase("id1", "0").exceptionOrNull())
        assertEquals(DonationException.InvalidVolume, updateDonationVolumeUseCase("id1", "-5.0").exceptionOrNull())
    }

    @Test
    fun `UpdateVolume success should call repository once and return success result`() = runTest {
        val mockDonation = mockk<Donation>()
        coEvery { repository.updateDonationVolume("id1", "250.5") } returns mockDonation
        
        val result = updateDonationVolumeUseCase("id1", "250.5")
        
        assertTrue(result.isSuccess)
        assertEquals(mockDonation, result.getOrNull())
        coVerify(exactly = 1) { repository.updateDonationVolume("id1", "250.5") }
    }

    @Test
    fun `UpdateVolume should handle repository exceptions`() = runTest {
        coEvery { repository.updateDonationVolume(any(), any()) } throws Exception("Update failed")
        val result = updateDonationVolumeUseCase("id1", "300")
        assertTrue(result.isFailure)
        assertEquals("Update failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `UpdateVolume when repository returns null should return failure`() = runTest {
        coEvery { repository.updateDonationVolume(any(), any()) } returns null
        val result = updateDonationVolumeUseCase("id1", "250")
        assertTrue(result.isFailure)
        assertEquals("Failed to update volume", result.exceptionOrNull()?.message)
    }

    // --- 3. APPROVE DONATION TESTING ---

    @Test
    fun `ApproveDonation should call repository approve exactly once`() = runTest {
        // Fix: Repository returns Unit, not Result
        coEvery { repository.approveDonation("did", "donorid") } returns Unit
        
        val result = approveDonationUseCase("did", "donorid")
        
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.approveDonation("did", "donorid") }
        confirmVerified(repository)
    }

    @Test
    fun `ApproveDonation should handle repository exceptions`() = runTest {
        val errorMessage = "Approve error"
        coEvery { repository.approveDonation(any(), any()) } throws Exception(errorMessage)
        
        val result = approveDonationUseCase("did", "donorid")
        
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
}
