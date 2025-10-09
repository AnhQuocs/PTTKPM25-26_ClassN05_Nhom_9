package com.example.heartbeat.data.repository.users.donor

import com.example.heartbeat.data.model.dto.DonorAvatarDto
import com.example.heartbeat.data.model.dto.DonorDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.entity.users.DonorAvatar
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class DonorRepositoryImpl(
    private val firestore: FirebaseFirestore
) : DonorRepository {

    private val donorCollection = firestore.collection("donors")
    private val avatarCollection = firestore.collection("donorAvatars")

    override suspend fun addDonor(donor: Donor) {
        donorCollection.document(donor.donorId)
            .set(donor.toDto())
            .await()
    }

    override suspend fun getCurrentDonor(donorId: String): Donor? {
        val snapshot = donorCollection.document(donorId).get().await()
        val dto = snapshot.toObject(DonorDto::class.java) ?: return null
        return dto.toDomain()
    }

    override suspend fun getDonorById(donorId: String): Donor? {
        val snapshot = donorCollection.document(donorId).get().await()
        val dto = snapshot.toObject(DonorDto::class.java) ?: return null
        return dto.toDomain()
    }

    override suspend fun updateDonor(donorId: String, donor: Donor) {
        donorCollection.document(donorId)
            .set(donor.toDto(), SetOptions.merge())
            .await()
    }

    override suspend fun isDonorProfileExist(userId: String): Boolean {
        val donor = getCurrentDonor(userId)
        return donor != null
    }

    // --- Avatar lưu Base64 ---
    override suspend fun uploadAvatarBase64(donorId: String, base64: String): String {
        val donorAvatar = DonorAvatar(donorId, base64)
        avatarCollection.document(donorId)
            .set(donorAvatar.toDto())
            .await()
        return base64
    }

    override suspend fun updateAvatarBase64(donorId: String, base64: String): String {
        return uploadAvatarBase64(donorId, base64)
    }

    override suspend fun saveAvatarUrl(donorAvatar: DonorAvatar) {
        avatarCollection.document(donorAvatar.donorId)
            .set(donorAvatar.toDto())
            .await()
    }

    override suspend fun getAvatar(donorId: String): DonorAvatar {
        val snapshot = avatarCollection.document(donorId).get().await()
        val dto = snapshot.toObject(DonorAvatarDto::class.java)
            ?: throw Exception("Donor avatar not found")
        return dto.toDomain()
    }
}