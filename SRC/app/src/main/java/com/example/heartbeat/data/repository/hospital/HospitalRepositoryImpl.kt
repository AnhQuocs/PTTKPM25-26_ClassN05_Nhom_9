package com.example.heartbeat.data.repository.hospital

import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.source.remote.FirebaseHospitalDataSource
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.hospital.HospitalRepository

class HospitalRepositoryImpl(
    private val dataSource: FirebaseHospitalDataSource
): HospitalRepository {
    override suspend fun getAllHospitals(): List<Hospital> {
        return dataSource.fetchAllHospitals().map { (id, dto) ->
            dto.toDomain(id)
        }
    }

    override suspend fun getHospitalById(hospitalId: String): Hospital? {
        val dto = dataSource.fetchHospitalById(hospitalId) ?: return null
        return dto.toDomain(hospitalId)
    }
}