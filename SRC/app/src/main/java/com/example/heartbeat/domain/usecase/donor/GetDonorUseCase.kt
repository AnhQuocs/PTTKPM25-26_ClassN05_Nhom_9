package com.example.heartbeat.domain.usecase.donor

import com.example.heartbeat.domain.repository.donor.DonorRepository

class GetDonorUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorId: String) {
        repository.getDonor(donorId)
    }
}