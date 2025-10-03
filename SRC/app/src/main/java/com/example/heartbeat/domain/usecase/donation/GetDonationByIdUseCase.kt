package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetDonationByIdUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String): Donation? {
        return repository.getDonationById(donationId)
    }
}