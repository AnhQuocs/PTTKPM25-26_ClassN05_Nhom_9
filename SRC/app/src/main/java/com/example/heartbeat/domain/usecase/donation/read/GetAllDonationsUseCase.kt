package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetAllDonationsUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getAllDonations()
    }
}