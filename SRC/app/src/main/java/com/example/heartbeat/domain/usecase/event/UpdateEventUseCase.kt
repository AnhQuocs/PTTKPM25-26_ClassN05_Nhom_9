package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository

class UpdateEventUseCase (
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String, event: Event) {
        return repository.updateEvent(eventId, event)
    }
}