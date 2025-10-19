package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import javax.inject.Inject

data class AuthUseCases(
    val signUp: SignUpUseCase,
    val signUpWithCode: SignUpWithCodeUseCase,
    val login: LoginUseCase,
    val loginWithCode: LoginWithCodeUseCase,
    val logout: LogOutUseCase,
    val resetPassword: ResetPasswordUseCase,
    val getCurrentUser: GetCurrentUserUseCase,
    val checkUserLoggedInUseCase: CheckUserLoggedInUseCase,
    val updatePasswordUseCase: UpdatePasswordUseCase,
    val updateUserNameUseCase: UpdateUserNameUseCase
)

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String) =
        repository.signUp(email, password, username)
}

class SignUpWithCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String, staffCode: String) =
        repository.signUpWithCode(email, password, username, staffCode)
}

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email, password)
}

class LoginWithCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, staffCode: String) =
        repository.loginWithCode(email, password, staffCode)
}

class LogOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class ResetPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) =
        repository.resetPassword(email)
}

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthUser? {
        return authRepository.getCurrentUser()
    }
}

class CheckUserLoggedInUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}

class UpdateUserNameUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(newUserName: String): Result<Unit> {
        return repository.updateUsername(newUserName)
    }
}

class UpdatePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(newPassword: String): Result<Unit> {
        return repository.updatePassword(newPassword)
    }
}