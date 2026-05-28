package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import javax.inject.Inject

class SearchEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(query: String): List<Event> {
        val allEvents = eventRepository.getAllEvents()
        val hospitalMap = hospitalRepository.getAllHospitals().associateBy { it.hospitalId }
        
        // Tách các từ khóa tìm kiếm. 
        // Nếu query trống, tokens sẽ trống và all {} trả về true cho mọi event.
        val tokens = query.trim().split(" ").filter { it.isNotBlank() }

        return allEvents.filter { event ->
            val hospital = hospitalMap[event.locationId]
            
            tokens.all { token ->
                // Nhánh 1: Khớp với tên Event
                if (event.name.contains(token, ignoreCase = true)) {
                    return@all true
                }
                
                // Nhánh 2: Nếu có thông tin bệnh viện, kiểm tra tên và tỉnh thành
                if (hospital != null) {
                    if (hospital.hospitalName.contains(token, ignoreCase = true)) {
                        return@all true
                    }
                    if (hospital.province.contains(token, ignoreCase = true)) {
                        return@all true
                    }
                }
                
                // Nhánh 3: Không khớp bất kỳ tiêu chí nào
                false
            }
        }
    }
}
