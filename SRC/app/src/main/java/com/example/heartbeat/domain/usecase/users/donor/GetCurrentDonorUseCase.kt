package com.example.heartbeat.domain.usecase.users.donor

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.repository.users.donor.DonorRepository

class GetCurrentDonorUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorId: String): Donor? {
        return repository.getCurrentDonor(donorId)
    }
}