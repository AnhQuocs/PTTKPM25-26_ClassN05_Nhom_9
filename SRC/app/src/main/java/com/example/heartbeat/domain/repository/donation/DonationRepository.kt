package com.example.heartbeat.domain.repository.donation

import com.example.heartbeat.domain.entity.donation.Donation
import kotlinx.coroutines.flow.Flow

interface DonationRepository {
    // CREATE
    suspend fun addDonation(donation: Donation): Donation?

    // READ
    suspend fun getDonationById(donationId: String): Donation?
    suspend fun getDonationsByDonor(donorId: String): List<Donation>
    suspend fun getDonationsByEvent(eventId: String): List<Donation>

    // UPDATE
    suspend fun updateDonation(donation: Donation): Donation
    suspend fun updateStatus(donationId: String, status: String): Donation?
    suspend fun updateDonationVolume(donationId: String, volume: String): Donation?

    // DELETE
    suspend fun deleteDonation(donationId: String): Boolean

    // OBSERVE (Realtime)
    fun observePendingDonations(): Flow<List<Donation>>
    fun observeDonationsByEvent(eventId: String): Flow<List<Donation>>
}