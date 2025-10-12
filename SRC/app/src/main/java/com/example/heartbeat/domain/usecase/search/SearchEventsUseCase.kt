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
        val keyword = query.trim().lowercase()
        val tokens = keyword.split(" ")

        val hospitals = hospitalRepository.getAllHospitals()
        val hospitalMap = hospitals.associateBy { it.hospitalId }
        val events = eventRepository.getAllEvents()

        return events.filter { event ->
            val hospital = hospitalMap[event.locationId]
            val eventName = event.name.lowercase()
            val hospitalName = hospital?.hospitalName?.lowercase() ?: ""
            val province = hospital?.province?.lowercase() ?: ""

            tokens.all { token ->
                eventName.contains(token) ||
                        hospitalName.contains(token) ||
                        province.contains(token)
            }
        }
    }
}