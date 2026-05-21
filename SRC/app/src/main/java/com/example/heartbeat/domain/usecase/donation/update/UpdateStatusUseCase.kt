package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class UpdateStatusUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, status: String): Result<Donation> {
        if (donationId.isBlank()) {
            return Result.failure(DonationException.EmptyDonationId)
        }
        if (status.isBlank()) {
            return Result.failure(DonationException.InvalidStatus)
        }

        val result = repository.updateStatus(donationId, status)
        return if (result != null) {
            Result.success(result)
        } else {
            Result.failure(Exception("Failed to update status"))
        }
    }
}
