package com.example.heartbeat.domain.entity.user

data class AuthUser(
    val uid: String,
    val email: String?,
    val username: String?
)