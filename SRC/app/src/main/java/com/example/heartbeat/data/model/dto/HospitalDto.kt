package com.example.heartbeat.data.model.dto

data class HospitalDto(
    val hospitalName: Map<String, String>? = null,
    val imgUrl: String? = null,
    val address: Map<String, String>? = null,
    val phone: String? = null,
    val province: Map<String, String>? = null,
    val district: Map<String, String>? = null
)