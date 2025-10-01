package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository

class ObserveDonorListUseCase(
    private val repo: EventRepository
) {
    operator fun invoke(
        eventId: String,
        onUpdate: (List<String>) -> Unit
    ) = repo.observeDonorList(eventId, onUpdate)
}