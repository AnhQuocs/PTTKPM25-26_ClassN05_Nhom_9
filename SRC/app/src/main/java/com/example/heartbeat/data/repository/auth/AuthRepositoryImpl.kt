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

    override suspend fun signUpWithStaffCode(
        email: String, password: String, username: String, staffCode: String
    ): Result<AuthUser> = runCatching {
        validateStaffCode(staffCode)

        val uid = createFirebaseUser(email, password)
        val user = AuthUser(uid, email, username, role = "staff")

        saveUserToFirestore(uid, user)
        markStaffCodeAsUsed(staffCode, uid)

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

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun getCurrentUser(): AuthUser? {
        val user = auth.currentUser ?: return null
        return AuthUser(
            uid = user.uid,
            email = user.email,
            username = user.displayName,
            role = "user"
        )
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // ===================== PRIVATE HELPERS =====================

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
}