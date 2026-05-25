package com.example.heartbeat.domain.usecase.hospital

import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.hospital.HospitalRepository

class GetHospitalByIdUseCase (
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(hospitalId: String): Hospital? {
        // Logic validation để phục vụ kiểm thử Hộp trắng
        if (hospitalId.isBlank()) {
            return null
        }
        return repository.getHospitalById(hospitalId)
    }
}
