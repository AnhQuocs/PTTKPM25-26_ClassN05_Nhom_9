package com.example.heartbeat.domain.usecase.donation.read

import com.example.heartbeat.domain.repository.donation.DonationRepository
import java.time.DayOfWeek
import java.time.LocalDate

class GetDonationsByWeekUseCase(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(weekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY)): Int {
        return repository.getDonationsByWeek(weekStart)
    }
}