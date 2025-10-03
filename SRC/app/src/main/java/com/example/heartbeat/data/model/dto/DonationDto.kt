package com.example.heartbeat.data.model.dto

import com.google.firebase.Timestamp

data class DonationDto(
    val donationId: String? = null,
    val donorId: String? = null,
    val eventId: String? = null,
    val citizenId: String? = null,
    val status: String? = null,
    val donationVolume: String? = null,
    val createAt: Timestamp? = null,
    val donatedAt: Timestamp? = null
)