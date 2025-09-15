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

            val user = AuthUser(uid, email, username, role = "user")
            firestore.collection("users").document(uid).set(user.toDto()).await()

            Result.success(user)
        } catch (e: Exception) {
            auth.currentUser?.delete()?.await()
            Result.failure(e)
        }
    }

    override suspend fun signUpWithStaffCode(
        email: String,
        password: String,
        username: String,
        staffCode: String
    ): Result<AuthUser> {
        return try {
            // check staff code
            val staffDoc = firestore.collection("staffCodes").document(staffCode).get().await()
            if(!staffDoc.exists() || staffDoc.getString("usedBy") != null) {
                return Result.failure(Exception("Invalid or already used staff code"))
            }

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("User is null"))

            val user = AuthUser(uid = uid, email = email, username = username, role = "staff")
            firestore.collection("users").document(uid).set(user.toDto()).await()

            // update staffCodes -> assign usedBy
            firestore.collection("staffCodes").document(staffCode).update("usedBy", uid).await()

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

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
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