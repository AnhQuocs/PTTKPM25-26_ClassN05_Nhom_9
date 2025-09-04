package com.example.heartbeat.domain.usecase.donor

import com.example.heartbeat.domain.entity.user.Donor
import com.example.heartbeat.domain.repository.donor.DonorRepository

class UpdateDonorUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorId: String, donor: Donor) {
        repository.updateDonor(donorId, donor)
    }
}