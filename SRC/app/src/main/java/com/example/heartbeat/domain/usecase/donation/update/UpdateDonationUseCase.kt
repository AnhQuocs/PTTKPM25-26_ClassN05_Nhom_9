package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class UpdateDonationUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donation: Donation): Donation {
        return repository.updateDonation(donation)
    }
}