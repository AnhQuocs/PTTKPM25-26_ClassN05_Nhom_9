package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.DonorAvatarDto
import com.example.heartbeat.domain.entity.users.DonorAvatar

fun DonorAvatarDto.toDomain(): DonorAvatar {
    return DonorAvatar(
        donorId = donorId.orEmpty(),
        avatarUrl = avatarUrl.orEmpty()
    )
}

fun DonorAvatar.toDto(): DonorAvatarDto {
    return DonorAvatarDto(
        donorId = donorId,
        avatarUrl = avatarUrl
    )
}