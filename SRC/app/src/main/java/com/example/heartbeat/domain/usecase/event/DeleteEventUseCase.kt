package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository

/**
 * CODE CŨ (FAIL TEST):
 * class DeleteEventUseCase (
 *     private val repository: EventRepository
 * ) {
 *     suspend operator fun invoke(eventId: String) {
 *         return repository.deleteEvent(eventId)
 *     }
 * }
 */

class DeleteEventUseCase (
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String) {
        // LOGIC MỚI: Thêm validation để pass test TC_EV_05
        if (eventId.isBlank()) {
            return
        }
        return repository.deleteEvent(eventId)
    }
}
