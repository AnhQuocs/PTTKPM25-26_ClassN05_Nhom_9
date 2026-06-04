package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.repository.donation.DonationRepository
import java.time.LocalDate

class GetDonationsByDayUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(day: LocalDate = LocalDate.now()): Result<Int> = try {
        Result.success(repository.getDonationsByDay(day))
    } catch (e: Exception) {
        Result.failure(e)
    }
}