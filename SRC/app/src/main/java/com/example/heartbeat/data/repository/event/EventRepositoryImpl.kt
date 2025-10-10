package com.example.heartbeat.data.repository.event

import android.util.Log
import com.example.heartbeat.data.model.dto.DonationDto
import com.example.heartbeat.data.model.dto.EventDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.data.model.mapper.toLocalDateTime
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class EventRepositoryImpl(
    private val firestore: FirebaseFirestore
): EventRepository {

    private val collection = firestore.collection("events")

    override fun observeAllEvents(): Flow<List<Event>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, e ->
            if(e != null) {
                close(e)
                return@addSnapshotListener
            }
            if(snapshot != null) {
                val events = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(EventDto::class.java)?.toDomain(doc.id)
                }
                trySend(events)
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun addEvent(event: Event) {
        val docRef = collection.document()
        val now = com.google.firebase.Timestamp.now()

        val dto = event.copy(
            id = docRef.id,
            createdAt = event.createdAt ?: now.toLocalDateTime(),
            updatedAt = event.updatedAt ?: now.toLocalDateTime()
        ).toDto()

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

    override suspend fun updateEvent(eventId: String, event: Event) {
        val now = com.google.firebase.Timestamp.now()
        val dto = event.copy(
            updatedAt = now.toLocalDateTime()
        ).toDto()

        collection.document(eventId).set(dto).await()
    }

    override suspend fun deleteEvent(eventId: String) {
        collection.document(eventId).delete().await()
    }

    override fun observeDonorCount(eventId: String, onUpdate: (Int) -> Unit) {
        firestore.collection("donations")
            .whereEqualTo("eventId", eventId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    return@addSnapshotListener
                }

                val donorCount = snapshot.toObjects(DonationDto::class.java)
                    .mapNotNull { it.toDomain() }
                    .count { it.status != "REJECTED" }

                onUpdate(donorCount)
            }
    }

    override fun observeDonorList(
        eventId: String,
        onUpdate: (List<String>) -> Unit
    ) {
        firestore.collection("donations")
            .whereEqualTo("eventId", eventId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val donorIds = snapshot.documents.mapNotNull { it.getString("donorId") }
                onUpdate(donorIds)
            }
    }
}