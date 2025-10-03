package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetDonationsByEventUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(eventId: String): List<Donation> {
        return repository.getDonationsByEvent(eventId)
    }
}