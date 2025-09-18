package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository

class AddEventUseCase (
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        return repository.addEvent(event)
    }
}