package com.example.heartbeat.data.model.dto

import com.google.firebase.Timestamp

data class EventDto(
    val locationId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val date: String? = null,
    val time: String? = null,
    val deadline: Timestamp? = null,
    val donorList: List<String>? = null,
    val capacity: Int? = null,
    val donorCount: Int? = null
)