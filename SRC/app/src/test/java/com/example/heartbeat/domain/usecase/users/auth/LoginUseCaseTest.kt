package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginWithCodeUseCase: LoginWithCodeUseCase

    @Before
    fun setUp() {
        repository = mockk()
        loginUseCase = LoginUseCase(repository)
        loginWithCodeUseCase = LoginWithCodeUseCase(repository)
    }

    @Test
    fun `Login with blank email (only spaces) should return EmptyField`() = runTest {
        val result = loginUseCase("   ", "password")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
        
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun `Login with empty password should return EmptyField`() = runTest {
        val result = loginUseCase("test@gmail.com", "")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `Login with valid data should call repository exactly once and return success`() = runTest {
        val email = "test@gmail.com"
        val password = "password123"
        val mockUser = mockk<AuthUser>()
        coEvery { repository.login(email, password) } returns Result.success(mockUser)

        val result = loginUseCase(email, password)

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())

        coVerify(exactly = 1) { repository.login(email, password) }
        confirmVerified(repository)
    }

    @Test
    fun `Login should return failure when repository throws unexpected exception`() = runTest {
        val email = "test@gmail.com"
        val password = "password123"
        val errorMessage = "Network Connection Error"
        
        coEvery { repository.login(email, password) } throws Exception(errorMessage)

        val result = loginUseCase(email, password)

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `Login with incorrect credentials should return repository failure result`() = runTest {
        val email = "wrong@gmail.com"
        val password = "wrongpassword"
        
        coEvery { repository.login(email, password) } returns Result.failure(AuthException.InvalidCredentials)

        val result = loginUseCase(email, password)

        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidCredentials, result.exceptionOrNull())
        
        coVerify(exactly = 1) { repository.login(email, password) }
    }

    @Test
    fun `LoginWithCode with blank email should return EmptyField`() = runTest {
        val result = loginWithCodeUseCase("   ", "password", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `LoginWithCode with blank password should return EmptyField`() = runTest {
        val result = loginWithCodeUseCase("test@gmail.com", "   ", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `LoginWithCode with blank staffCode should return EmptyField`() = runTest {
        val result = loginWithCodeUseCase("test@gmail.com", "pass", "   ")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `LoginWithCode with valid data should verify repository interaction`() = runTest {
        val email = "test@gmail.com"
        val pass = "pass"
        val code = "ST123"
        val mockUser = mockk<AuthUser>()
        coEvery { repository.loginWithCode(email, pass, code) } returns Result.success(mockUser)

        val result = loginWithCodeUseCase(email, pass, code)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.loginWithCode(email, pass, code) }
    }
}
