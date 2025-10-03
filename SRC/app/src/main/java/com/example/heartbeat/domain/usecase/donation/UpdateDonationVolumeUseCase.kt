package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class UpdateDonationVolumeUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, volume: String): Donation? {
        return repository.updateDonationVolume(donationId, volume)
    }
}
