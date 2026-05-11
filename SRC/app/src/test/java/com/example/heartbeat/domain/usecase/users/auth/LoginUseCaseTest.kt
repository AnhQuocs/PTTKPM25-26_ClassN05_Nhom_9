package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `Login with empty email should return EmptyField`() = runBlocking {
        val result = loginUseCase("", "password")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `Login with empty password should return EmptyField`() = runBlocking {
        val result = loginUseCase("test@gmail.com", "")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `Login with valid data should return success`() = runBlocking {
        val mockUser = mockk<AuthUser>()
        coEvery { repository.login(any(), any()) } returns Result.success(mockUser)

        val result = loginUseCase("test@gmail.com", "password123")
        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
    }

    @Test
    fun `LoginWithCode with empty email should return EmptyField`() = runBlocking {
        val result = loginWithCodeUseCase("", "pass", "code")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `LoginWithCode with email not empty but empty password should return EmptyField`() = runBlocking {
        val result = loginWithCodeUseCase("test@gmail.com", "", "code")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `LoginWithCode with valid email and password but empty staffCode should return EmptyField`() = runBlocking {
        val result = loginWithCodeUseCase("test@gmail.com", "pass", "")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `LoginWithCode with valid data should return success`() = runBlocking {
        val mockUser = mockk<AuthUser>()
        coEvery { repository.loginWithCode(any(), any(), any()) } returns Result.success(mockUser)

        val result = loginWithCodeUseCase("test@gmail.com", "pass", "code")
        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
    }
}
