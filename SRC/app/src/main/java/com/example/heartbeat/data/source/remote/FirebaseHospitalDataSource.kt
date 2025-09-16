package com.example.heartbeat.data.source.remote

import com.example.heartbeat.data.model.dto.HospitalDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirebaseHospitalDataSource {
    private val collection = Firebase.firestore.collection("hospitals")

    suspend fun fetchAllHospitals(): List<Pair<String, HospitalDto>> {
        return collection.get().await().map { doc ->
            doc.id to doc.toObject(HospitalDto::class.java)
        }
    }

    suspend fun fetchHospitalById(hospitalId: String): HospitalDto? {
        val doc = collection
            .document(hospitalId)
            .get()
            .await()

        return doc.toObject(HospitalDto::class.java)
    }
}