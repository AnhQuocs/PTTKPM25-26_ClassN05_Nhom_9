package com.example.heartbeat.domain.repository.event

import com.example.heartbeat.domain.entity.event.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventRepository {
    fun observeAllEvents(): Flow<List<Event>>

    suspend fun getAllEvents(): List<Event>
    suspend fun getEventById(eventId: String): Event?

    suspend fun addEvent(event: Event)
    suspend fun updateEvent(eventId: String, event: Event)
    suspend fun updateDonorCount(eventId: String, delta: Int)

    suspend fun deleteEvent(eventId: String)

    fun observeDonorCount(eventId: String, onUpdate: (Int) -> Unit)
    fun observeDonorList(eventId: String, onUpdate: (List<String>) -> Unit)
    fun observeEventsByDate(selectedDate: LocalDate = LocalDate.now()): Flow<List<Event>>
    fun observeEventById(eventId: String): Flow<Event?>
}