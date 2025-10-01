package com.example.heartbeat.domain.entity.event

import kotlinx.datetime.LocalDateTime

data class Event(
    val id: String,
    val locationId: String,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val deadline: LocalDateTime?,
    val donorList: List<String>,
    val capacity: Int,
    val donorCount: Int
)