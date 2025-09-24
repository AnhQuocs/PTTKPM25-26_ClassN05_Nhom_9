package com.example.heartbeat.domain.entity.users

data class AuthUser(
    val uid: String,
    val email: String?,
    val username: String?,
    val role: String = "user"
)