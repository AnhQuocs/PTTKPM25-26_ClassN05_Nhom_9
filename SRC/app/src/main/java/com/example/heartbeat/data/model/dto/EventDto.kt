package com.example.heartbeat.data.model.dto

import com.example.heartbeat.domain.entity.users.Donor
import com.google.firebase.Timestamp

data class EventDto(
    val name: String? = null,
    val description: String? = null,
    val location: String? = null,
    val date: String? = null,
    val time: String? = null,
    val deadline: Timestamp? = null,
    val donorList: List<Donor>? = null,
    val capacity: Int? = null,
    val donorCount: Int? = null
)