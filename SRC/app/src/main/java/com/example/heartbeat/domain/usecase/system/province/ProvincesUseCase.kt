package com.example.heartbeat.domain.usecase.system.province

import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.repository.system.ProvinceRepository

class GetAllProvincesUseCase(private val repository: ProvinceRepository) {
    suspend operator fun invoke(): List<Province> {
        return repository.getAllProvinces()
    }
}

class GetProvinceByIdUseCase(private val repository: ProvinceRepository) {
    suspend operator fun invoke(id: String): Province? {
        return repository.getProvinceById(id)
    }
}