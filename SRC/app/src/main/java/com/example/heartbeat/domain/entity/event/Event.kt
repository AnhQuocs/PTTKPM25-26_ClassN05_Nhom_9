package com.example.heartbeat.domain.entity.event

import com.example.heartbeat.domain.entity.users.Donor
import kotlinx.datetime.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val date: String,
    val time: String,
    val deadline: LocalDateTime,
    val donorList: List<Donor>,
    val capacity: Int,
    val donorCount: Int
)