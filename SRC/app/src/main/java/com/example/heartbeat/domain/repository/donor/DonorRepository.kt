package com.example.heartbeat.domain.repository.donor

import com.example.heartbeat.domain.entity.user.Donor
import com.example.heartbeat.domain.entity.user.DonorAvatar

interface DonorRepository {
    suspend fun addDonor(donor: Donor)
    suspend fun getDonor(donorId: String): Donor?
    suspend fun updateDonor(donorId: String, donor: Donor)
    suspend fun isDonorProfileExist(userId: String): Boolean

    suspend fun uploadAvatarBase64(donorId: String, base64: String): String
    suspend fun updateAvatarBase64(donorId: String, base64: String): String

    suspend fun saveAvatarUrl(donorAvatar: DonorAvatar)
    suspend fun getAvatar(donorId: String): DonorAvatar
}