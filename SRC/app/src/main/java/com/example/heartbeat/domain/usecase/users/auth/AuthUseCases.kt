package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import javax.inject.Inject

sealed class AuthException : Exception() {
    object InvalidEmail : AuthException()
    object PasswordTooShort : AuthException()
    object EmptyField : AuthException()
    object InvalidStaffCode : AuthException()
    object InvalidCredentials : AuthException()
    object UserAlreadyExists : AuthException()
    object UserNotFound : AuthException()
}

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
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String
    ): Result<AuthUser> {
        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(AuthException.InvalidEmail)
        }
        if (password.length < 6) {
            return Result.failure(AuthException.PasswordTooShort)
        }
        if (username.isBlank()) {
            return Result.failure(AuthException.EmptyField)
        }
        return try {
            repository.signUp(email, password, username)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignUpWithCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String,
        staffCode: String
    ): Result<AuthUser> {
        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(AuthException.InvalidEmail)
        }
        if (password.length < 6) {
            return Result.failure(AuthException.PasswordTooShort)
        }
        if (staffCode.isBlank()) {
            return Result.failure(AuthException.InvalidStaffCode)
        }
        return try {
            repository.signUpWithCode(email, password, username, staffCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<AuthUser> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(AuthException.EmptyField)
        }
        return try {
            repository.login(email, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class LoginWithCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        staffCode: String
    ): Result<AuthUser> {
        if (email.isBlank() || password.isBlank() || staffCode.isBlank()) {
            return Result.failure(AuthException.EmptyField)
        }
        return try {
            repository.loginWithCode(email, password, staffCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class LogOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class ResetPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(AuthException.InvalidEmail)
        }
        return try {
            repository.resetPassword(email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
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
        if (newUserName.isBlank()) {
            return Result.failure(AuthException.EmptyField)
        }
        return try {
            repository.updateUsername(newUserName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UpdatePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(newPassword: String): Result<Unit> {
        if (newPassword.length < 6) {
            return Result.failure(AuthException.PasswordTooShort)
        }
        return try {
            repository.updatePassword(newPassword)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}