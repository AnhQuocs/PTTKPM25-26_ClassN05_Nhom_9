package com.example.heartbeat.domain.repository.donor

import com.example.heartbeat.domain.entity.user.Donor

interface DonorRepository {
    suspend fun addDonor(donor: Donor)
    suspend fun getDonor(donorId: String): Donor
    suspend fun updateDonor(donorId: String, donor: Donor)
}