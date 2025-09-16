package com.example.heartbeat.domain.usecase.hospital

import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.hospital.HospitalRepository

class GetHospitalByIdUseCase(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(hospitalId: String): Hospital? {
        return repository.getHospitalById(hospitalId)
    }
}