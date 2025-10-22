package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ObserveEventsByDateUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(selectedDate: LocalDate = LocalDate.now()): Flow<List<Event>> {
        return repository.observeEventsByDate(selectedDate)
    }
}