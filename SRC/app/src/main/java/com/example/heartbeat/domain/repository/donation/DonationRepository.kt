package com.example.heartbeat.domain.repository.donation

import com.example.heartbeat.domain.entity.donation.Donation
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

interface DonationRepository {
    // CREATE
    suspend fun addDonation(donation: Donation): Donation?

    // READ
    fun observeDonationById(donationId: String): Flow<Donation?>
    suspend fun getDonationsByDonor(donorId: String): List<Donation>
    suspend fun getDonationsByEvent(eventId: String): List<Donation>
    suspend fun getDonationsByDay(day: LocalDate = LocalDate.now()): Int
    suspend fun getDonationsByWeek(weekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY)): Int
    suspend fun getDonationsByMonth(month: YearMonth = YearMonth.now()): Int

    // UPDATE
    suspend fun updateDonation(donation: Donation): Donation
    suspend fun updateStatus(donationId: String, status: String): Donation?
    suspend fun updateDonationVolume(donationId: String, volume: String): Donation?
    suspend fun getAllDonations(): Int

    // DELETE
    suspend fun deleteDonation(donationId: String): Boolean

    // OBSERVE (Realtime)
    fun observePendingDonations(): Flow<List<Donation>>
    fun observeDonationsByEvent(eventId: String): Flow<List<Donation>>
    fun observeDonationByDonor(eventId: String, donorId: String): Flow<Donation?>
}