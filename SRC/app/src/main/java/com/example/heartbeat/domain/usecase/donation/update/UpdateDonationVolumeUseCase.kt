package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class UpdateDonationVolumeUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, volume: String): Result<Donation> {
        if (donationId.isBlank()) {
            return Result.failure(DonationException.EmptyDonationId)
        }
        
        val volumeValue = volume.toDoubleOrNull()
        if (volumeValue == null || volumeValue <= 0) {
            return Result.failure(DonationException.InvalidVolume)
        }

        val result = repository.updateDonationVolume(donationId, volume)
        return if (result != null) {
            Result.success(result)
        } else {
            Result.failure(Exception("Failed to update volume"))
        }
    }
}
