package com.example.heartbeat.domain.entity.donation

import java.time.LocalDateTime

data class Donation(
    val donationId: String,
    val donorId: String,
    val eventId: String,
    val citizenId: String,
    val status: String,
    val donationVolume: String,
    val createAt: LocalDateTime,
    val donatedAt: LocalDateTime? = null
)