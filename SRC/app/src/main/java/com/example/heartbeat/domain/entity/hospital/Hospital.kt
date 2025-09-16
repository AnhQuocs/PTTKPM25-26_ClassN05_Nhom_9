package com.example.heartbeat.domain.entity.hospital

data class Hospital(
    val hospitalId: String,
    val hospitalName: String,
    val imgUrl: String,
    val address: String,
    val phone: String,
    val province: String,
    val district: String
)