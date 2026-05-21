package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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

    // --- TEST UPDATE STATUS ---
    @Test
    fun `UpdateStatus with empty donationId should return EmptyDonationId`() = runBlocking {
        val result = updateStatusUseCase("", "COMPLETED")
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyDonationId, result.exceptionOrNull())
    }

    @Test
    fun `UpdateStatus with empty status should return InvalidStatus`() = runBlocking {
        val result = updateStatusUseCase("id1", "")
        assertTrue(result.isFailure)
        assertEquals(DonationException.InvalidStatus, result.exceptionOrNull())
    }

    @Test
    fun `UpdateStatus success should return success result`() = runBlocking {
        val mockDonation = mockk<Donation>()
        coEvery { repository.updateStatus("id1", "COMPLETED") } returns mockDonation
        val result = updateStatusUseCase("id1", "COMPLETED")
        assertTrue(result.isSuccess)
        assertEquals(mockDonation, result.getOrNull())
    }

    @Test
    fun `UpdateStatus when repository returns null should return failure`() = runBlocking {
        coEvery { repository.updateStatus(any(), any()) } returns null
        val result = updateStatusUseCase("id1", "COMPLETED")
        assertTrue(result.isFailure)
        assertEquals("Failed to update status", result.exceptionOrNull()?.message)
    }

    // --- TEST UPDATE VOLUME ---
    @Test
    fun `UpdateVolume with empty donationId should return EmptyDonationId`() = runBlocking {
        val result = updateDonationVolumeUseCase("", "250")
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyDonationId, result.exceptionOrNull())
    }

    @Test
    fun `UpdateVolume with invalid volume string should return InvalidVolume`() = runBlocking {
        val result = updateDonationVolumeUseCase("id1", "not_a_number")
        assertTrue(result.isFailure)
        assertEquals(DonationException.InvalidVolume, result.exceptionOrNull())
    }

    @Test
    fun `UpdateVolume with zero or negative volume should return InvalidVolume`() = runBlocking {
        assertEquals(DonationException.InvalidVolume, updateDonationVolumeUseCase("id1", "0").exceptionOrNull())
        assertEquals(DonationException.InvalidVolume, updateDonationVolumeUseCase("id1", "-1").exceptionOrNull())
    }

    @Test
    fun `UpdateVolume success should return success result`() = runBlocking {
        val mockDonation = mockk<Donation>()
        coEvery { repository.updateDonationVolume("id1", "250") } returns mockDonation
        val result = updateDonationVolumeUseCase("id1", "250")
        assertTrue(result.isSuccess)
        assertEquals(mockDonation, result.getOrNull())
    }

    @Test
    fun `UpdateVolume when repository returns null should return failure`() = runBlocking {
        coEvery { repository.updateDonationVolume(any(), any()) } returns null
        val result = updateDonationVolumeUseCase("id1", "250")
        assertTrue(result.isFailure)
        assertEquals("Failed to update volume", result.exceptionOrNull()?.message)
    }

    // --- TEST APPROVE ---
    @Test
    fun `ApproveDonation should call repository approve`() = runBlocking {
        coEvery { repository.approveDonation(any(), any()) } returns Unit
        approveDonationUseCase("did", "donorid")
        coVerify(exactly = 1) { repository.approveDonation("did", "donorid") }
    }
}
