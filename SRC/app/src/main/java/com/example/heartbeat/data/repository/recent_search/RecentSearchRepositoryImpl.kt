package com.example.heartbeat.data.repository.recent_search

import com.example.heartbeat.data.model.dto.RecentSearchDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.repository.recent_search.RecentSearchRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RecentSearchRepositoryImpl(
    private val firestore: FirebaseFirestore
): RecentSearchRepository {

    override suspend fun addRecentSearch(userId: String, recent: RecentSearch) {
        val collectionRef = firestore.collection("users")
            .document(userId)
            .collection("recentSearch")

        val querySnapshot = collectionRef
            .whereEqualTo("title", recent.title)
            .whereEqualTo("subTitle", recent.subTitle)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            val docRef = collectionRef.document(recent.id)
            val dto = recent.toDto()
            docRef.set(dto).await()
        } else {
            val doc = querySnapshot.documents.first()
            doc.reference.update("historyAt", recent.historyAt).await()
        }
    }

    override suspend fun getRecentSearch(userId: String): List<RecentSearch> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("recentSearch")
            .orderBy("historyAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(RecentSearchDto::class.java) ?. toDomain() }
    }

    override suspend fun clearAllRecentSearch(userId: String) {
        val batch = firestore.batch()
        val collection = firestore.collection("users")
            .document(userId)
            .collection("recentSearch")
            .get()
            .await()

        collection.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
}