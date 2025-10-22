package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import kotlinx.coroutines.flow.Flow

class ObserveEventByIdUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(eventId: String): Flow<Event?> {
        return repository.observeEventById(eventId)
    }
}