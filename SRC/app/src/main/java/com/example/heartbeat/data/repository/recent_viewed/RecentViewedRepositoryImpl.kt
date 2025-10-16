package com.example.heartbeat.data.repository.recent_viewed

import com.example.heartbeat.data.model.dto.RecentViewedDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.recent_viewed.RecentViewed
import com.example.heartbeat.domain.repository.recent_viewed.RecentViewedRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RecentViewedRepositoryImpl(
    private val firestore: FirebaseFirestore
): RecentViewedRepository{
    override suspend fun addRecentViewed(userId: String, recentViewed: RecentViewed) {
        val docRef = firestore.collection("users")
            .document(userId)
            .collection("recentViewed")
            .document(recentViewed.id)

        val dto = recentViewed.toDto()
        docRef.set(dto)
    }

    override suspend fun getRecentViewed(userId: String): List<RecentViewed> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("recentViewed")
            .orderBy("viewAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(RecentViewedDto::class.java)?.toDomain() }
    }

    override suspend fun clearRecentViewed(userId: String) {
        val batch = firestore.batch()
        val collection = firestore.collection("users")
            .document(userId)
            .collection("recentViewed")
            .get()
            .await()

        collection.documents.forEach { batch.delete((it.reference)) }
        batch.commit().await()
    }
}