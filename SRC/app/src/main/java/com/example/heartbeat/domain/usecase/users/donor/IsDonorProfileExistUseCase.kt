package com.example.heartbeat.domain.usecase.users.donor

import com.example.heartbeat.domain.repository.users.donor.DonorRepository

class IsDonorProfileExistUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(userId: String): Boolean {
        return repository.isDonorProfileExist(userId)
    }
}