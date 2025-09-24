package com.example.heartbeat.domain.repository.event

import com.example.heartbeat.domain.entity.event.Event

interface EventRepository {
    suspend fun addEvent(event: Event)
    suspend fun getEventById(eventId: String): Event?
    suspend fun updateEvent(eventId: String, event: Event)
    suspend fun deleteEvent(eventId: String)
    suspend fun getAllEvents(): List<Event>
    fun observeDonorCount(eventId: String, onUpdate: (Int) -> Unit)
}