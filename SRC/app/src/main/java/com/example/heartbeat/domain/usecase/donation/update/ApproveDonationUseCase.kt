package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.repository.donation.DonationRepository

class ApproveDonationUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, donorId: String): Result<Unit> = try {
        repository.approveDonation(donationId, donorId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}