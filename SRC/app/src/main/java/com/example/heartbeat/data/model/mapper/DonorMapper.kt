package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.DonorDto
import com.example.heartbeat.domain.entity.users.Donor

fun DonorDto.toDomain(): Donor {
    return Donor(
        donorId = donorId.orEmpty(),
        name = name.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        bloodGroup = bloodGroup.orEmpty(),
        cityId = cityId.orEmpty(),
        dateOfBirth = dateOfBirth.orEmpty(),
        age = age ?: 0,
        gender = gender.orEmpty(),
        willingToDonate = willingToDonate ?: false,
        about = about.orEmpty()
    )
}

fun Donor.toDto(): DonorDto {
    return DonorDto(
        donorId = donorId,
        name = name,
        phoneNumber = phoneNumber,
        bloodGroup = bloodGroup,
        cityId = cityId,
        dateOfBirth = dateOfBirth,
        age = age,
        gender = gender,
        willingToDonate = willingToDonate,
        about = about
    )
}