package com.example.heartbeat.domain.entity.users

data class Donor(
    val donorId: String,
    val name: String,
    val phoneNumber: String,
    val bloodGroup: String,
    val cityId: String,
    val dateOfBirth: String,
    val age: Int,
    val gender: String,
    val willingToDonate: Boolean,
    val about: String
)