package com.example.heartbeat.domain.usecase.auth

import com.example.heartbeat.domain.entity.user.AuthUser
import com.example.heartbeat.domain.repository.auth.AuthRepository

data class AuthUseCases(
    val signUp: SignUpUseCase,
    val login: LoginUseCase,
    val logout: LogOutUseCase,
    val getCurrentUser: GetCurrentUserUseCase
)

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String) =
        repository.signUp(email, password, username)
}

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email, password)
}

class LogOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AuthUser? {
        return repository.getCurrentUserFromFirestore()
    }
}