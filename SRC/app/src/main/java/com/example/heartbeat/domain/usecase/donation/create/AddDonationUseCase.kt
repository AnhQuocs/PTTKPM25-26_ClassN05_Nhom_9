package com.example.heartbeat.domain.usecase.donation.create

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class AddDonationUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donation: Donation): Result<Donation> = try {
        if (donation.donorId.isBlank()) {
            Result.failure(DonationException.EmptyDonorId)
        } else if (donation.eventId.isBlank()) {
            Result.failure(DonationException.EmptyEventId)
        } else {
            val result = repository.addDonation(donation)
            if (result != null) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to add donation"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
