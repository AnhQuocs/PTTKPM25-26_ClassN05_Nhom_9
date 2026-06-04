package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.DonationException

class UpdateDonationVolumeUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, volume: String): Result<Donation> = try {
        if (donationId.isBlank()) {
            Result.failure(DonationException.EmptyDonationId)
        } else {
            val volumeValue = volume.toDoubleOrNull()
            if (volumeValue == null || volumeValue <= 0) {
                Result.failure(DonationException.InvalidVolume)
            } else {
                val result = repository.updateDonationVolume(donationId, volume)
                if (result != null) {
                    Result.success(result)
                } else {
                    Result.failure(Exception("Failed to update volume"))
                }
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
