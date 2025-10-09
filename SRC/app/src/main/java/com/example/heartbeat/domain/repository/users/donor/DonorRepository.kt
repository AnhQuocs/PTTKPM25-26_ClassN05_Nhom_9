package com.example.heartbeat.domain.repository.users.donor

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.entity.users.DonorAvatar

interface DonorRepository {
    suspend fun addDonor(donor: Donor)
    suspend fun getCurrentDonor(donorId: String): Donor?
    suspend fun getDonorById(donorId: String): Donor?
    suspend fun updateDonor(donorId: String, donor: Donor)
    suspend fun isDonorProfileExist(userId: String): Boolean

    suspend fun uploadAvatarBase64(donorId: String, base64: String): String
    suspend fun updateAvatarBase64(donorId: String, base64: String): String

    suspend fun saveAvatarUrl(donorAvatar: DonorAvatar)
    suspend fun getAvatar(donorId: String): DonorAvatar
}