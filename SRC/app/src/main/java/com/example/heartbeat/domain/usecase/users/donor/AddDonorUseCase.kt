package com.example.heartbeat.domain.usecase.users.donor

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.repository.users.donor.DonorRepository

class AddDonorUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donor: Donor) {
        repository.addDonor(donor)
    }
}