package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository

class DeleteEventUseCase (
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String) {
        return repository.deleteEvent(eventId)
    }
}