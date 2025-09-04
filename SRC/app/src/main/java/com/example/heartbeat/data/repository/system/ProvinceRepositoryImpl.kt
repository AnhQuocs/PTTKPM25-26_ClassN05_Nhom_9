package com.example.heartbeat.data.repository.system

import com.example.heartbeat.data.model.mapper.toProvince
import com.example.heartbeat.data.source.remote.ProvinceDataSource
import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.repository.system.ProvinceRepository

class ProvinceRepositoryImpl(
    private val dataSource: ProvinceDataSource
): ProvinceRepository {

    override suspend fun getAllProvinces(): List<Province> {
        return dataSource.fetchAllProvinces().map { (id, dto) ->
            dto.toProvince(id)
        }
    }
}