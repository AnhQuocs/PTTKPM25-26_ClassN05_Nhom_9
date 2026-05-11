package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class AuthUseCasesWrapperTest {

    @Test
    fun `AuthUseCases wrapper should hold all use cases`() {
        val repository = mockk<AuthRepository>()
        val authUseCases = AuthUseCases(
            signUp = SignUpUseCase(repository),
            signUpWithCode = SignUpWithCodeUseCase(repository),
            login = LoginUseCase(repository),
            loginWithCode = LoginWithCodeUseCase(repository),
            logout = LogOutUseCase(repository),
            resetPassword = ResetPasswordUseCase(repository),
            getCurrentUser = GetCurrentUserUseCase(repository),
            checkUserLoggedInUseCase = CheckUserLoggedInUseCase(repository),
            updatePasswordUseCase = UpdatePasswordUseCase(repository),
            updateUserNameUseCase = UpdateUserNameUseCase(repository)
        )
        
        assertNotNull(authUseCases.signUp)
        assertNotNull(authUseCases.login)
        assertNotNull(authUseCases.logout)
        assertNotNull(authUseCases.resetPassword)
        assertNotNull(authUseCases.getCurrentUser)
        assertNotNull(authUseCases.checkUserLoggedInUseCase)
        assertNotNull(authUseCases.updatePasswordUseCase)
        assertNotNull(authUseCases.updateUserNameUseCase)
    }
}
