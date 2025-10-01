package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllEventsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(): Flow<List<Event>> {
        return repository.observeAllEvents()
    }
}