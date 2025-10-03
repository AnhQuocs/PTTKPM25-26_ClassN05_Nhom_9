package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetDonationsByDonorUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donorId: String): List<Donation> {
        return repository.getDonationsByDonor(donorId)
    }
}