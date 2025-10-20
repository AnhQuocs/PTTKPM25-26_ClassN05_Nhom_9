package com.example.heartbeat.data.source.remote

import android.util.Log
import com.example.heartbeat.data.model.dto.ProvinceDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirebaseProvinceDataSource() {

    private val collection = Firebase.firestore.collection("provinces")

    suspend fun fetchAllProvinces(): List<Pair<String, ProvinceDto>> {
        return collection.get().await().map { doc ->
            doc.id to doc.toObject(ProvinceDto::class.java)
        }
    }

    suspend fun fetchProvinceById(id: String): Pair<String, ProvinceDto>? {
        Log.d("ProvinceDebug", "fetchProvinceById: id = '$id'")
        if (id.isBlank()) return null

        val document = collection.document(id).get().await()
        return if (document.exists()) {
            document.id to document.toObject(ProvinceDto::class.java)!!
        } else null
    }
}