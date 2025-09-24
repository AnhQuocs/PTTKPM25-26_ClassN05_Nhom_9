package com.example.heartbeat.domain.repository.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser

interface AuthRepository {
    suspend fun signUp(email: String, password: String, username: String): Result<AuthUser>
    suspend fun signUpWithCode(
        email: String,
        password: String,
        username: String,
        roleCode: String
    ): Result<AuthUser>

    suspend fun login(email: String, password: String): Result<AuthUser>
    suspend fun loginWithCode(
        email: String,
        password: String,
        roleCode: String
    ): Result<AuthUser>

    suspend fun logout()
    suspend fun getCurrentUser(): AuthUser?
    suspend fun resetPassword(email: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
}