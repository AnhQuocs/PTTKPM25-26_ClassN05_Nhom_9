package com.example.heartbeat.domain.repository.hospital

import com.example.heartbeat.domain.entity.hospital.Hospital

interface HospitalRepository {
    suspend fun getAllHospitals(): List<Hospital>
    suspend fun getHospitalById(hospitalId: String): Hospital?
}