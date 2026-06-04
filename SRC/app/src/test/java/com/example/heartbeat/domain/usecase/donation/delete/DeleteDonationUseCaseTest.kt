package com.example.heartbeat.domain.usecase.donation.delete

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

class DeleteDonationUseCaseTest {
    private lateinit var repository: DonationRepository
    private lateinit var deleteDonationUseCase: DeleteDonationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        deleteDonationUseCase = DeleteDonationUseCase(repository)
    }

    // --- 1. Boundary Value Testing & Validation ---

    @Test
    fun `DeleteDonation with blank donationId should return EmptyDonationId`() = runTest {
        val result = deleteDonationUseCase("   ")
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyDonationId, result.exceptionOrNull())
        
        // Behavior Verification
        coVerify(exactly = 0) { repository.deleteDonation(any()) }
    }

    // --- 2. Behavior Verification Testing ---

    @Test
    fun `DeleteDonation success should call repository once and return success result`() = runTest {
        val donationId = "id1"
        coEvery { repository.deleteDonation(donationId) } returns true
        
        val result = deleteDonationUseCase(donationId)
        
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.deleteDonation(donationId) }
        confirmVerified(repository)
    }

    // --- 3. Exception & Error Injection Testing ---

    @Test
    fun `DeleteDonation should handle repository exceptions`() = runTest {
        val donationId = "id1"
        val errorMessage = "Database error"
        coEvery { repository.deleteDonation(donationId) } throws Exception(errorMessage)

        val result = deleteDonationUseCase(donationId)

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    // --- 4. Negative Testing ---

    @Test
    fun `DeleteDonation failure from repo should return failure result`() = runTest {
        val donationId = "id1"
        coEvery { repository.deleteDonation(donationId) } returns false
        
        val result = deleteDonationUseCase(donationId)
        
        assertTrue(result.isFailure)
        assertEquals("Failed to delete donation", result.exceptionOrNull()?.message)
        
        coVerify(exactly = 1) { repository.deleteDonation(donationId) }
    }
}
