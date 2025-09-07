package com.example.heartbeat.data.model.dto

data class DonorDto(
    val donorId: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val bloodGroup: String? = null,
    val city: String? = null,
    val dateOfBirth: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val willingToDonate: Boolean? = false,
    val about: String? = null
)