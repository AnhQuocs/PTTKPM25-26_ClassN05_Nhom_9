package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import kotlinx.coroutines.flow.Flow

class ObserveDonationByDonorUseCase(
    private val repository: DonationRepository
) {
    operator fun invoke(eventId: String, donorId: String): Flow<Donation?> {
        return repository.observeDonationByDonor(eventId, donorId)
    }
}