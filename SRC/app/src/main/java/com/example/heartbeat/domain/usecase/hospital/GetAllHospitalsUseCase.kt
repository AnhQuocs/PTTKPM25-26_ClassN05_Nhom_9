package com.example.heartbeat.domain.usecase.hospital

import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.hospital.HospitalRepository

class GetAllHospitalsUseCase(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(): List<Hospital> {
        return repository.getAllHospitals()
    }
}