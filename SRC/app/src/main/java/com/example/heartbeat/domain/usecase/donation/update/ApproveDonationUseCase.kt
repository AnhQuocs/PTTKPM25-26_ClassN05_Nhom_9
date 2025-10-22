package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.repository.donation.DonationRepository

class ApproveDonationUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, donorId: String) {
        return repository.approveDonation(donationId, donorId)
    }
}