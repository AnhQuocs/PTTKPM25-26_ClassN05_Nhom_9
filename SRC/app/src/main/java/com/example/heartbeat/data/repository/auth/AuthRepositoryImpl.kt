package com.example.heartbeat.data.repository.auth

import com.example.heartbeat.data.model.dto.AuthUserDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.user.AuthUser
import com.example.heartbeat.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {
    override suspend fun signUp(email: String, password: String, username: String): Result<AuthUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("User is null"))

            val user = AuthUser(uid, email, username)
            firestore.collection("users").document(uid).set(user.toDto()).await()

            Result.success(user)
        } catch (e: Exception) {
            auth.currentUser?.delete()?.await()
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("User is null"))

            val snapshot = firestore.collection("users").document(uid).get().await()
            val userDto = snapshot.toObject(AuthUserDto::class.java)
                ?: return Result.failure(Exception("User not found"))
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentUserFromFirestore(): AuthUser? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = firestore.collection("users").document(uid).get().await()
        val userDto = snapshot.toObject(AuthUserDto::class.java) ?: return null
        return userDto.toDomain()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}