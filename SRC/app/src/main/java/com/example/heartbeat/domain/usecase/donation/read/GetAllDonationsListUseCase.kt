package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository

class GetAllDonationsListUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(): Result<List<Donation>> = try {
        Result.success(repository.getAllDonationsList())
    } catch (e: Exception) {
        Result.failure(e)
    }
}