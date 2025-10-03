package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class AddDonationUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donation: Donation): Donation? {
        return repository.addDonation(donation)
    }
}