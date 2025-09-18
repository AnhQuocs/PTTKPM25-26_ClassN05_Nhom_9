package com.example.heartbeat.data.repository.event

import com.example.heartbeat.data.model.dto.EventDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepositoryImpl(
    private val firestore: FirebaseFirestore
): EventRepository {

    private val collection = firestore.collection("events")

    override suspend fun addEvent(event: Event) {
        val docRef = collection.document()
        val dto = event.copy(id = docRef.id).toDto()
        docRef.set(dto).await()
    }

    override suspend fun getEventById(eventId: String): Event? {
        val snapshot = collection.document(eventId).get().await()
        return if(snapshot.exists()) {
            snapshot.toObject(EventDto::class.java)?.toDomain(snapshot.id)
        } else  {
            null
        }
    }

    override suspend fun getAllEvents(): List<Event> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(EventDto::class.java)?.toDomain(doc.id)
        }
    }

    override suspend fun updateEvent(eventId: String, event: Event) {
        val dto = event.toDto()
        collection.document(eventId).set(dto).await()
    }

    override suspend fun deleteEvent(eventId: String) {
        collection.document(eventId).delete().await()
    }
}