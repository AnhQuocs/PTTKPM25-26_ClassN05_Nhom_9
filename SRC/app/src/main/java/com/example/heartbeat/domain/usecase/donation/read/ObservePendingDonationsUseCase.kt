package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import kotlinx.coroutines.flow.Flow

class ObservePendingDonationsUseCase (
    private val repository: DonationRepository
) {
    operator fun invoke(): Flow<List<Donation>> {
        return repository.observePendingDonations()
    }
}