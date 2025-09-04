package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.DonorDto
import com.example.heartbeat.domain.entity.user.Donor

fun DonorDto.toDomain(): Donor {
    return Donor(
        donorId = donorId.orEmpty(),
        name = name.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        bloodGroup = bloodGroup.orEmpty(),
        city = city.orEmpty(),
        dateOfBirth = dateOfBirth.orEmpty(),
        age = age ?: 0,
        gender = gender.orEmpty(),
        willingToDonate = willingToDonate ?: false,
        about = about.orEmpty(),
        profileAvatar = profileAvatar.orEmpty(),
    )
}

fun Donor.toDto(): DonorDto {
    return DonorDto(
        donorId = donorId,
        name = name,
        phoneNumber = phoneNumber,
        bloodGroup = bloodGroup,
        city = city,
        dateOfBirth = dateOfBirth,
        age = age,
        gender = gender,
        willingToDonate = willingToDonate,
        about = about,
        profileAvatar = profileAvatar
    )
}