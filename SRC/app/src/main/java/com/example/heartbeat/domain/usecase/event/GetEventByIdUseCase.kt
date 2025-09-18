package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository

class GetEventByIdUseCase (
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String): Event? {
        return repository.getEventById(eventId)
    }
}