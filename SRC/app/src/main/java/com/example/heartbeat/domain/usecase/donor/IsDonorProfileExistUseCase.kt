package com.example.heartbeat.domain.usecase.donor

import com.example.heartbeat.domain.repository.donor.DonorRepository

class IsDonorProfileExistUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(userId: String): Boolean {
        return repository.isDonorProfileExist(userId)
    }
}