package com.example.heartbeat.data.repository.donation

import com.example.heartbeat.data.model.dto.DonationDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

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

    override fun observeDonationById(donationId: String): Flow<Donation?> = callbackFlow {
        val listener = collection.document(donationId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val donation = snapshot?.toObject(DonationDto::class.java)?.toDomain()
                trySendBlocking(donation)
            }

        awaitClose { listener.remove() }
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

    override suspend fun getDonationsByDay(day: LocalDate): Int {
        val startOfDay = day.atStartOfDay()
        val endOfDay = day.plusDays(1).atStartOfDay()

        val snapshot = collection
            .whereGreaterThanOrEqualTo("createAt", Timestamp(Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant())))
            .whereLessThan("createAt", Timestamp(Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant())))
            .get()
            .await()

        return snapshot.size()
    }

    override suspend fun getDonationsByWeek(weekStart: LocalDate): Int {
        val startOfWeek = weekStart.atStartOfDay()
        val endOfWeek = weekStart.plusDays(7).atStartOfDay()

        val snapshot = collection
            .whereGreaterThanOrEqualTo("createAt", Timestamp(Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant())))
            .whereLessThan("createAt", Timestamp(Date.from(endOfWeek.atZone(ZoneId.systemDefault()).toInstant())))
            .get()
            .await()

        return snapshot.size()
    }

    override suspend fun getDonationsByMonth(month: YearMonth): Int {
        val startOfMonth = month.atDay(1).atStartOfDay()
        val endOfMonth = month.plusMonths(1).atDay(1).atStartOfDay()

        val snapshot = collection
            .whereGreaterThanOrEqualTo("createAt", Timestamp(Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant())))
            .whereLessThan("createAt", Timestamp(Date.from(endOfMonth.atZone(ZoneId.systemDefault()).toInstant())))
            .get()
            .await()

        return snapshot.size()
    }

    override suspend fun getAllDonations(): Int {
        val snapshot = collection
            .get()
            .await()

        return snapshot.size()
    }

    override suspend fun getAllDonationsList(): List<Donation> {
        val snapshot = collection.get().await()
        return snapshot.toObjects(DonationDto::class.java).mapNotNull { it.toDomain() }
    }

    override suspend fun updateDonation(donation: Donation): Donation {
        collection.document(donation.donationId).set(donation.toDto()).await()
        return donation
    }

    override suspend fun updateStatus(donationId: String, status: String): Donation? {
        collection.document(donationId).update("status", status).await()
        return observeDonationById(donationId).firstOrNull()
    }

    override suspend fun updateDonationVolume(donationId: String, volume: String): Donation? {
        collection.document(donationId).update("donationVolume", volume).await()
        return observeDonationById(donationId).firstOrNull()
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

    override fun observeDonationByDonor(eventId: String, donorId: String): Flow<Donation?> = callbackFlow {
        val listener = collection
            .whereEqualTo("eventId", eventId)
            .whereEqualTo("donorId", donorId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val donation = snapshot?.toObjects(DonationDto::class.java)
                    ?.mapNotNull { it.toDomain() }
                    ?.firstOrNull()

                trySendBlocking(donation)
            }

        awaitClose { listener.remove() }
    }
}