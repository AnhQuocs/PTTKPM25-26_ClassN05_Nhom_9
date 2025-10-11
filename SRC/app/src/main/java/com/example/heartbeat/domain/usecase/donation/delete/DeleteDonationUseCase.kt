package com.example.heartbeat.domain.usecase.donation.delete

import com.example.heartbeat.domain.repository.donation.DonationRepository

class DeleteDonationUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String): Boolean {
        return repository.deleteDonation(donationId)
    }
}