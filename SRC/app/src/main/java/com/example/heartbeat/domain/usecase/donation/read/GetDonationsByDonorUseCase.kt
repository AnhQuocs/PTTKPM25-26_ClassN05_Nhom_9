package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetDonationsByDonorUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donorId: String): Result<List<Donation>> = try {
        Result.success(repository.getDonationsByDonor(donorId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}