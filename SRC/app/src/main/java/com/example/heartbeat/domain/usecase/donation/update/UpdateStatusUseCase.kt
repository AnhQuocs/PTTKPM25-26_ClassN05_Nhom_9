package com.example.heartbeat.domain.usecase.donation.update

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class UpdateStatusUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, status: String): Donation? {
        return repository.updateStatus(donationId, status)
    }
}