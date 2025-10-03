package com.example.heartbeat.data.repository.donation

import com.example.heartbeat.data.model.dto.DonationDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class DonationRepositoryImpl(
    firestore: FirebaseFirestore,
): DonationRepository {
    private val collection = firestore.collection("donations")

    override suspend fun addDonation(donation: Donation): Donation? {
        val dto = donation.toDto()
        val docRef = collection.document(donation.donationId)
        docRef.set(dto).await()
        val snapshot = docRef.get().await()
        return snapshot.toObject(DonationDto::class.java)?.toDomain()
    }

    override suspend fun getDonationById(donationId: String): Donation? {
        val snapshot = collection.document(donationId).get().await()
        return snapshot.toObject(DonationDto::class.java)?.toDomain()
    }

    override suspend fun getDonationsByDonor(donorId: String): List<Donation> {
        val snapshot = collection
            .whereEqualTo("donorId", donorId)
            .get().await()
        return snapshot.toObjects(DonationDto::class.java).mapNotNull { it.toDomain() }
    }

    override suspend fun getDonationsByEvent(eventId: String): List<Donation> {
        val snapshot = collection
            .whereEqualTo("eventId", eventId)
            .get().await()
        return snapshot.toObjects(DonationDto::class.java).mapNotNull { it.toDomain() }
    }

    override suspend fun updateDonation(donation: Donation): Donation {
        collection.document(donation.donationId).set(donation.toDto()).await()
        return donation
    }

    override suspend fun updateStatus(donationId: String, status: String): Donation? {
        collection.document(donationId).update("status", status).await()
        return getDonationById(donationId)
    }

    override suspend fun updateDonationVolume(donationId: String, volume: String): Donation? {
        collection.document(donationId).update("donationVolume", volume).await()
        return getDonationById(donationId)
    }

    override suspend fun deleteDonation(donationId: String): Boolean {
        return try {
            collection.document(donationId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun observePendingDonations(): Flow<List<Donation>> = callbackFlow {
        val listener = collection
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, _ ->
                val donations = snapshot?.toObjects(DonationDto::class.java)
                    ?.mapNotNull { it.toDomain() } ?: emptyList()
                trySendBlocking(donations)
            }
        awaitClose { listener.remove() }
    }

    override fun observeDonationsByEvent(eventId: String): Flow<List<Donation>> = callbackFlow {
        val listener = collection
            .whereEqualTo("eventId", eventId)
            .addSnapshotListener { snapshot, _ ->
                val donations = snapshot?.toObjects(DonationDto::class.java)
                    ?.mapNotNull { it.toDomain() } ?: emptyList()
                trySendBlocking(donations)
            }

        awaitClose { listener.remove() }
    }
}