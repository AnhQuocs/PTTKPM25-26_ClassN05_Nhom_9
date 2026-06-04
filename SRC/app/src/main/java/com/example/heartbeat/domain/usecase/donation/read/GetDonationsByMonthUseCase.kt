package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.repository.donation.DonationRepository
import java.time.YearMonth

class GetDonationsByMonthUseCase (
    private val repository: DonationRepository
) {
    suspend operator fun invoke(month: YearMonth = YearMonth.now()): Result<Int> = try {
        Result.success(repository.getDonationsByMonth(month))
    } catch (e: Exception) {
        Result.failure(e)
    }
}