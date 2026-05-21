package com.example.heartbeat.domain.usecase.donation.delete

import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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

    @Test
    fun `DeleteDonation with empty donationId should return EmptyDonationId`() = runBlocking {
        val result = deleteDonationUseCase("")
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(DonationException.EmptyDonationId, result.exceptionOrNull())
    }

    @Test
    fun `DeleteDonation success should return success result`() = runBlocking {
        coEvery { repository.deleteDonation("id1") } returns true
        val result = deleteDonationUseCase("id1")
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun `DeleteDonation failure from repo should return failure result`() = runBlocking {
        coEvery { repository.deleteDonation("id1") } returns false
        val result = deleteDonationUseCase("id1")
        Assert.assertTrue(result.isFailure)
    }
}