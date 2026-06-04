package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.repository.donation.DonationRepository
import java.time.DayOfWeek
import java.time.LocalDate

class GetDonationsByWeekUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(weekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY)): Result<Int> = try {
        Result.success(repository.getDonationsByWeek(weekStart))
    } catch (e: Exception) {
        Result.failure(e)
    }
}