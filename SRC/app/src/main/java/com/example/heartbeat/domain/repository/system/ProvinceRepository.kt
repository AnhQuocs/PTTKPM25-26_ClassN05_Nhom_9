package com.example.heartbeat.domain.repository.system

import com.example.heartbeat.domain.entity.system.Province

interface ProvinceRepository {
    suspend fun getAllProvinces(): List<Province>
}