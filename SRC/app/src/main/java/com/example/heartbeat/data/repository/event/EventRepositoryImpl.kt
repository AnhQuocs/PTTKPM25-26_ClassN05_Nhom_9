package com.example.heartbeat.data.repository.event

import com.example.heartbeat.data.model.dto.DonationDto
import com.example.heartbeat.data.model.dto.EventDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.data.model.mapper.toLocalDateTime
import com.example.heartbeat.data.source.remote.EventRemoteDataSource
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class EventRepositoryImpl(
    private val dataSource: EventRemoteDataSource,
    private val firestore: FirebaseFirestore
) : EventRepository {

    override fun observeAllEvents(): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection("events")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val events = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(com.example.heartbeat.data.model.dto.EventDto::class.java)
                            ?.toDomain(doc.id)
                    }
                    trySend(events)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getAllEvents(): List<Event> {
        return dataSource.fetchAllEvents().map { (id, dto) -> dto.toDomain(id) }
    }

    override suspend fun addEvent(event: Event) {
        val docRef = firestore.collection("events").document()
        val now = Timestamp.now()

        val dto = event.copy(
            id = docRef.id,
            createdAt = event.createdAt,
            updatedAt = event.updatedAt ?: now.toLocalDateTime()
        ).toDto()

        dataSource.addEvent(dto, docRef.id)
    }

    override suspend fun getEventById(eventId: String): Event? {
        return dataSource.fetchEventById(eventId)?.let { (id, dto) -> dto.toDomain(id) }
    }

    override suspend fun updateEvent(eventId: String, event: Event) {
        val now = Timestamp.now()
        val dto = event.copy(updatedAt = now.toLocalDateTime()).toDto()
        dataSource.updateEvent(eventId, dto)
    }

    override suspend fun deleteEvent(eventId: String) {
        dataSource.deleteEvent(eventId)
    }

    override fun observeDonorCount(eventId: String, onUpdate: (Int) -> Unit) {
        firestore.collection("donations")
            .whereEqualTo("eventId", eventId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val donorCount = snapshot.toObjects(DonationDto::class.java)
                    .mapNotNull { it.toDomain() }
                    .count { it.status != "REJECTED" }
                onUpdate(donorCount)
            }
    }

    override fun observeDonorList(eventId: String, onUpdate: (List<String>) -> Unit) {
        firestore.collection("donations")
            .whereEqualTo("eventId", eventId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val donorIds = snapshot.documents.mapNotNull { it.getString("donorId") }
                onUpdate(donorIds)
            }
    }
}