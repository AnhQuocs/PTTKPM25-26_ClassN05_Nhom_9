package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository

class ObserveDonorCountUseCase(private val repository: EventRepository) {
    operator fun invoke(eventId: String, onUpdate: (Int) -> Unit) {
        repository.observeDonorCount(eventId, onUpdate)
    }
}