package com.example.heartbeat.domain.usecase.auth

import com.example.heartbeat.domain.entity.user.AuthUser
import com.example.heartbeat.domain.repository.auth.AuthRepository
import javax.inject.Inject

data class AuthUseCases(
    val signUp: SignUpUseCase,
    val signUpWithStaffCode: SignUpWithStaffCodeUseCase,
    val login: LoginUseCase,
    val logout: LogOutUseCase,
    val resetPassword: ResetPasswordUseCase,
    val getCurrentUser: GetCurrentUserUseCase,
    val checkUserLoggedInUseCase: CheckUserLoggedInUseCase
)

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String) =
        repository.signUp(email, password, username)
}

class SignUpWithStaffCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String, staffCode: String) =
        repository.signUpWithStaffCode(email, password, username, staffCode)
}

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email, password)
}

class LogOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class ResetPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) =
        repository.resetPassword(email)
}

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AuthUser? {
        return repository.getCurrentUserFromFirestore()
    }
}

class CheckUserLoggedInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}