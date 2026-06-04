package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import javax.inject.Inject

class SearchEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(query: String): Result<List<Event>> = try {
        val allEvents = eventRepository.getAllEvents()
        val hospitalMap = hospitalRepository.getAllHospitals().associateBy { it.hospitalId }
        
        val tokens = query.trim().split(" ").filter { it.isNotBlank() }

        val filtered = allEvents.filter { event ->
            val hospital = hospitalMap[event.locationId]
            
            tokens.all { token ->
                if (event.name.contains(token, ignoreCase = true)) {
                    return@all true
                }
                
                if (hospital != null) {
                    if (hospital.hospitalName.contains(token, ignoreCase = true)) {
                        return@all true
                    }
                    if (hospital.province.contains(token, ignoreCase = true)) {
                        return@all true
                    }
                }
                false
            }
        }
        Result.success(filtered)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
