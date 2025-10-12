package com.example.heartbeat.domain.repository.event

import com.example.heartbeat.domain.entity.event.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun observeAllEvents(): Flow<List<Event>>

    suspend fun getAllEvents(): List<Event>
    suspend fun getEventById(eventId: String): Event?

    suspend fun addEvent(event: Event)
    suspend fun updateEvent(eventId: String, event: Event)
    suspend fun deleteEvent(eventId: String)

    fun observeDonorCount(eventId: String, onUpdate: (Int) -> Unit)
    fun observeDonorList(eventId: String, onUpdate: (List<String>) -> Unit)
}