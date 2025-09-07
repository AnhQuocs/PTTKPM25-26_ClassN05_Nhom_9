package com.example.heartbeat.domain.usecase.donor

import com.example.heartbeat.domain.entity.user.DonorAvatar
import com.example.heartbeat.domain.repository.donor.DonorRepository

class UploadAvatarUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorId: String, base64: String): String {
        return repository.uploadAvatarBase64(donorId, base64)
    }
}

class UpdateAvatarUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorId: String, base64: String): String {
        return repository.updateAvatarBase64(donorId, base64)
    }
}

class SaveAvatarUrlUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorAvatar: DonorAvatar) {
        repository.saveAvatarUrl(donorAvatar)
    }
}

class GetAvatarUseCase(
    private val repository: DonorRepository
) {
    suspend operator fun invoke(donorId: String): DonorAvatar {
        return repository.getAvatar(donorId)
    }
}