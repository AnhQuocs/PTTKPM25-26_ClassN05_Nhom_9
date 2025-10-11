package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import kotlinx.coroutines.flow.Flow

class ObserveDonationByIdUseCase (
    private val repository: DonationRepository
) {
    operator fun invoke(donationId: String): Flow<Donation?> {
        return repository.observeDonationById(donationId)
    }
}