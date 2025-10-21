package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetAllDonationsListUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(): List<Donation> {
        return repository.getAllDonationsList()
    }
}