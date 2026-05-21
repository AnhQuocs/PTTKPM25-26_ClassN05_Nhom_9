package com.example.heartbeat.domain.usecase.donation.create

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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

    @Test
    fun `AddDonation with empty donorId should return EmptyDonorId`() = runBlocking {
        val donation = createDummyDonation(donorId = "")
        val result = addDonationUseCase(donation)
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(DonationException.EmptyDonorId, result.exceptionOrNull())
    }

    @Test
    fun `AddDonation with empty eventId should return EmptyEventId`() = runBlocking {
        val donation = createDummyDonation(eventId = "")
        val result = addDonationUseCase(donation)
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(DonationException.EmptyEventId, result.exceptionOrNull())
    }

    @Test
    fun `AddDonation with valid data should return success`() = runBlocking {
        val donation = createDummyDonation()
        coEvery { repository.addDonation(any()) } returns donation

        val result = addDonationUseCase(donation)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(donation, result.getOrNull())
    }

    @Test
    fun `AddDonation when repository returns null should return failure`() = runBlocking {
        val donation = createDummyDonation()
        coEvery { repository.addDonation(any()) } returns null

        val result = addDonationUseCase(donation)
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals("Failed to add donation", result.exceptionOrNull()?.message)
    }
}
