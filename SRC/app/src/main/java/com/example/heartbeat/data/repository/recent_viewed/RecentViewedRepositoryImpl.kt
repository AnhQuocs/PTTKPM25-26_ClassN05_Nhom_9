package com.example.heartbeat.data.repository.recent_viewed

import android.util.Log
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
        val userCollection = firestore.collection("users")
            .document(userId)
            .collection("recentViewed")

        val snapshot = userCollection
            .orderBy("viewedAt", Query.Direction.DESCENDING)
            .get()
            .await()

        val currentList = snapshot.documents.mapNotNull {
            it.toObject(RecentViewedDto::class.java)?.toDomain()
        }

        val existingDoc = snapshot.documents.find { it.id == recentViewed.id }
        if (existingDoc != null) {
            existingDoc.reference.update("viewedAt", recentViewed.viewedAt).await()
            return
        }

        if (currentList.size >= 3) {
            val oldest = snapshot.documents.last()
            oldest.reference.delete().await()
        }

        val docRef = userCollection.document(recentViewed.id)
        docRef.set(recentViewed.toDto()).await()
    }

    override suspend fun getRecentViewed(userId: String): List<RecentViewed> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("recentViewed")
            .orderBy("viewedAt", Query.Direction.DESCENDING)
            .get()
            .await()

        val list = snapshot.documents.mapNotNull { it.toObject(RecentViewedDto::class.java)?.toDomain() }

        Log.d("FirestoreDebug", "Loaded ${list.size} recent viewed items: $list")

        return list
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