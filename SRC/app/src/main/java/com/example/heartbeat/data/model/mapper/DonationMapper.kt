package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.DonationDto
import com.example.heartbeat.domain.entity.donation.Donation
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

fun DonationDto.toDomain(): Donation {
    return Donation(
        donationId = donationId.orEmpty(),
        donorId = donorId.orEmpty(),
        eventId = eventId.orEmpty(),
        citizenId = citizenId.orEmpty(),
        status = status.orEmpty(),
        donationVolume = donationVolume.orEmpty(),
        createAt = createAt?.toDonationLocalDateTime() ?: LocalDateTime.now(),
        donatedAt = donatedAt.orEmpty()
    )
}

fun Donation.toDto(): DonationDto {
    return DonationDto(
        donationId = donationId,
        donorId = donorId,
        eventId = eventId,
        citizenId = citizenId,
        status = status,
        donationVolume = donationVolume,
        createAt = createAt.toDonationTimestamp(),
        donatedAt = donatedAt
    )
}

fun Timestamp.toDonationLocalDateTime(): LocalDateTime {
    return this.toDate()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun LocalDateTime.toDonationTimestamp(): Timestamp {
    val instant = this.atZone(ZoneId.systemDefault()).toInstant()
    return Timestamp(Date.from(instant))
}