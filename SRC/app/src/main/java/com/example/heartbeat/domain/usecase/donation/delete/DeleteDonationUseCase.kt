package com.example.heartbeat.domain.usecase.donation.delete

import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class DeleteDonationUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String): Result<Unit> {
        if (donationId.isBlank()) {
            return Result.failure(DonationException.EmptyDonationId)
        }
        
        return if (repository.deleteDonation(donationId)) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to delete donation"))
        }
    }
}
