package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository

class DeleteEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String) {
        // Thêm validation để pass test TC_EV_05
        if (eventId.isBlank()) {
            return
        }
        return repository.deleteEvent(eventId)
    }
}
