package com.example.heartbeat.domain.repository.auth

import com.example.heartbeat.domain.entity.user.AuthUser

interface AuthRepository {
    suspend fun signUp(email: String, password: String, username: String): Result<AuthUser>
    suspend fun signUpWithStaffCode(email: String, password: String, username: String, staffCode: String): Result<AuthUser>

    suspend fun login(email: String, password: String): Result<AuthUser>
    suspend fun logout()
    suspend fun getCurrentUser(): AuthUser?
    suspend fun resetPassword(email: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
}