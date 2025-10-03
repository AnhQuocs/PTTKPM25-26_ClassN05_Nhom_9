package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import kotlinx.coroutines.flow.Flow

class ObserveDonationsByEventUseCase(
    private val repository: DonationRepository
) {
    operator fun invoke(eventId: String): Flow<List<Donation>> {
        return repository.observeDonationsByEvent(eventId)
    }
}