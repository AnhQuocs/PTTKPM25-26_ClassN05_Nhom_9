package com.example.heartbeat.domain.usecase.donation.delete

import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class DeleteDonationUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String): Result<Unit> = try {
        if (donationId.isBlank()) {
            Result.failure(DonationException.EmptyDonationId)
        } else {
            if (repository.deleteDonation(donationId)) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete donation"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
