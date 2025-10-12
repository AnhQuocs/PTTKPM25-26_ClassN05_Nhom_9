package com.example.heartbeat.data.source.remote

import com.example.heartbeat.data.model.dto.EventDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("events")

    suspend fun fetchAllEvents(): List<Pair<String, EventDto>> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(EventDto::class.java)
            dto?.let { doc.id to it }
        }
    }

    suspend fun fetchEventById(eventId: String): Pair<String, EventDto>? {
        val doc = collection.document(eventId).get().await()
        val dto = doc.toObject(EventDto::class.java)
        return dto?.let { doc.id to it }
    }

    suspend fun addEvent(dto: EventDto, eventId: String) {
        collection.document(eventId).set(dto).await()
    }

    suspend fun updateEvent(eventId: String, dto: EventDto) {
        collection.document(eventId).set(dto).await()
    }

    suspend fun deleteEvent(eventId: String) {
        collection.document(eventId).delete().await()
    }
}