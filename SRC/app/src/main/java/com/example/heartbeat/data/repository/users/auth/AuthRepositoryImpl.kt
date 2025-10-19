package com.example.heartbeat.data.repository.users.auth

import com.example.heartbeat.data.model.dto.AuthUserDto
import com.example.heartbeat.data.model.mapper.toDomain
import com.example.heartbeat.data.model.mapper.toDto
import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(
        email: String, password: String, username: String
    ): Result<AuthUser> = runCatching {
        val uid = createFirebaseUser(email, password)
        val user = AuthUser(uid, email, username, role = "user")

        saveUserToFirestore(uid, user)
        user
    }.onFailure {
        deleteCurrentUserIfExists()
    }

    override suspend fun signUpWithCode(
        email: String,
        password: String,
        username: String,
        roleCode: String
    ): Result<AuthUser> = runCatching {
        val uid = createFirebaseUser(email, password)

        val role = when {
            roleCode == "HBAM999" -> "admin"
            else -> {
                val codeDoc = firestore.collection("staffCodes")
                    .document(roleCode)
                    .get()
                    .await()
                if (!codeDoc.exists() || codeDoc.getString("usedBy") != null) {
                    throw Exception("Invalid or already used staff code")
                }
                "staff"
            }
        }

        val user = AuthUser(uid, email, username, role)
        saveUserToFirestore(uid, user)

        try {
            firestore.collection("staffCodes")
                .document(roleCode)
                .update("usedBy", uid)
                .await()
        } catch (e: Exception) {
            deleteCurrentUserIfExists()
            throw e
        }

        user
    }.onFailure {
        deleteCurrentUserIfExists()
    }

    override suspend fun login(
        email: String, password: String
    ): Result<AuthUser> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("User is null")
        getUserFromFirestore(uid) ?: throw Exception("User not found")
    }

    override suspend fun loginWithCode(
        email: String,
        password: String,
        roleCode: String
    ): Result<AuthUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("Login failed"))

            val codeDoc = firestore.collection("staffCodes")
                .document(roleCode)
                .get()
                .await()

            if (!codeDoc.exists()) {
                auth.signOut()
                return Result.failure(Exception("Invalid staff code"))
            }

            val usedBy = codeDoc.getString("usedBy")
            if (usedBy != user.uid) {
                auth.signOut()
                return Result.failure(Exception("Staff code does not belong to this user"))
            }

            val role = if (roleCode == "HBAM999") {
                "admin"
            } else {
                codeDoc.getString("role") ?: "staff"
            }

            Result.success(
                AuthUser(
                    uid = user.uid,
                    email = user.email ?: "",
                    username = user.displayName,
                    role = role
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun getCurrentUser(): AuthUser? {
        val user = auth.currentUser ?: return null
        val doc = firestore.collection("users").document(user.uid).get().await()
        return doc.toObject(AuthUserDto::class.java)?.toDomain()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    //PRIVATE HELPERS
    private suspend fun createFirebaseUser(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("User is null")
    }

    private suspend fun saveUserToFirestore(uid: String, user: AuthUser) {
        firestore.collection("users").document(uid).set(user.toDto()).await()
    }

    private suspend fun getUserFromFirestore(uid: String): AuthUser? {
        val snapshot = firestore.collection("users").document(uid).get().await()
        return snapshot.toObject(AuthUserDto::class.java)?.toDomain()
    }

    private suspend fun validateStaffCode(staffCode: String) {
        val staffDoc = firestore.collection("staffCodes").document(staffCode).get().await()
        if (!staffDoc.exists() || staffDoc.getString("usedBy") != null) {
            throw Exception("Invalid or already used staff code")
        }
    }

    private suspend fun markStaffCodeAsUsed(staffCode: String, uid: String) {
        firestore.collection("staffCodes").document(staffCode).update("usedBy", uid).await()
    }

    private suspend fun deleteCurrentUserIfExists() {
        auth.currentUser?.delete()?.await()
    }

    override suspend fun updateUsername(newUsername: String): Result<Unit> = runCatching {
        val user = auth.currentUser ?: throw Exception("No logged-in user")
        val uid = user.uid

        firestore.collection("users")
            .document(uid)
            .update("username", newUsername)
            .await()

        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(newUsername)
            .build()
        user.updateProfile(profileUpdates).await()
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = runCatching {
        val user = auth.currentUser ?: throw Exception("No logged-in user")
        user.updatePassword(newPassword).await()
    }
}