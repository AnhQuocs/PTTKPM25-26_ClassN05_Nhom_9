package com.example.heartbeat.domain.usecase.donation.create

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
import java.time.LocalDateTime

class CreateDonationUseCaseTest {
    private lateinit var repository: DonationRepository
    private lateinit var addDonationUseCase: AddDonationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        addDonationUseCase = AddDonationUseCase(repository)
    }

    private fun createDummyDonation(
        donorId: String = "d1",
        eventId: String = "e1"
    ) = Donation(
        donationId = "id1",
        donorId = donorId,
        eventId = eventId,
        citizenId = "c1",
        status = "PENDING",
        donationVolume = "0",
        createAt = LocalDateTime.now(),
        donatedAt = ""
    )

    // --- 1. Boundary Value Testing & Validation ---

    @Test
    fun `AddDonation with blank donorId should return EmptyDonorId`() = runTest {
        val donation = createDummyDonation(donorId = "   ")
        val result = addDonationUseCase(donation)
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyDonorId, result.exceptionOrNull())
        
        // Behavior Verification
        coVerify(exactly = 0) { repository.addDonation(any()) }
    }

    @Test
    fun `AddDonation with blank eventId should return EmptyEventId`() = runTest {
        val donation = createDummyDonation(eventId = "   ")
        val result = addDonationUseCase(donation)
        assertTrue(result.isFailure)
        assertEquals(DonationException.EmptyEventId, result.exceptionOrNull())
        
        coVerify(exactly = 0) { repository.addDonation(any()) }
    }

    // --- 2. Behavior Verification Testing ---

    @Test
    fun `AddDonation with valid data should call repository and return success`() = runTest {
        val donation = createDummyDonation()
        coEvery { repository.addDonation(donation) } returns donation

        val result = addDonationUseCase(donation)
        
        assertTrue(result.isSuccess)
        assertEquals(donation, result.getOrNull())
        
        coVerify(exactly = 1) { repository.addDonation(donation) }
        confirmVerified(repository)
    }

    // --- 3. Exception & Error Injection Testing ---

    @Test
    fun `AddDonation should handle repository exceptions`() = runTest {
        val donation = createDummyDonation()
        val errorMessage = "Database connection failed"
        coEvery { repository.addDonation(any()) } throws Exception(errorMessage)

        val result = addDonationUseCase(donation)

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    // --- 4. Negative Testing ---

    @Test
    fun `AddDonation when repository returns null should return failure result`() = runTest {
        val donation = createDummyDonation()
        coEvery { repository.addDonation(any()) } returns null

        val result = addDonationUseCase(donation)
        
        assertTrue(result.isFailure)
        assertEquals("Failed to add donation", result.exceptionOrNull()?.message)
        
        coVerify(exactly = 1) { repository.addDonation(any()) }
    }
}
