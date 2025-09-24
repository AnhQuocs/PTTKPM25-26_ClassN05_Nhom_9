package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import javax.inject.Inject

class CheckUserLoggedUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}