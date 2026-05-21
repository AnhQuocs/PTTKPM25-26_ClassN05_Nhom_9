package com.example.heartbeat.domain.usecase.donation.create

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class AddDonationUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donation: Donation): Result<Donation> {
        if (donation.donorId.isBlank()) {
            return Result.failure(DonationException.EmptyDonorId)
        }
        if (donation.eventId.isBlank()) {
            return Result.failure(DonationException.EmptyEventId)
        }
        
        val result = repository.addDonation(donation)
        return if (result != null) {
            Result.success(result)
        } else {
            Result.failure(Exception("Failed to add donation"))
        }
    }
}
