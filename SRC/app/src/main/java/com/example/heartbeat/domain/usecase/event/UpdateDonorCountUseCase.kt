package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository

class UpdateDonorCountUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String, delta: Int) {
        repository.updateDonorCount(eventId, delta)
    }
}