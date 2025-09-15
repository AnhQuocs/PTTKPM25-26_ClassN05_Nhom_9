package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.AuthUserDto
import com.example.heartbeat.domain.entity.user.AuthUser

fun AuthUserDto.toDomain(): AuthUser {
    return AuthUser(uid, email, username, role)
}

fun AuthUser.toDto(): AuthUserDto {
    return AuthUserDto(uid, email, username, role)
}