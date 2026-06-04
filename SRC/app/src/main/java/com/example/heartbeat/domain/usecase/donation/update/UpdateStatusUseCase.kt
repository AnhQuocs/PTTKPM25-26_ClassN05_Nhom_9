package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class UpdateStatusUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, status: String): Result<Donation> = try {
        if (donationId.isBlank()) {
            Result.failure(DonationException.EmptyDonationId)
        } else if (status.isBlank()) {
            Result.failure(DonationException.InvalidStatus)
        } else {
            val result = repository.updateStatus(donationId, status)
            if (result != null) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to update status"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
