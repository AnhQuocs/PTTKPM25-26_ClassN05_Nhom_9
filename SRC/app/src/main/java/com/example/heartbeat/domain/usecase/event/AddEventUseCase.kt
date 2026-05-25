package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository

/**
 * CODE CŨ (FAIL TEST):
 * class AddEventUseCase (
 *     private val repository: EventRepository
 * ) {
 *     suspend operator fun invoke(event: Event) {
 *         return repository.addEvent(event)
 *     }
 * }
 */

class AddEventUseCase (
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        // LOGIC MỚI: Thêm validation để pass test TC_EV_02 và TC_EV_03
        if (event.name.isBlank() || event.capacity <= 0) {
            return
        }
        return repository.addEvent(event)
    }
}
