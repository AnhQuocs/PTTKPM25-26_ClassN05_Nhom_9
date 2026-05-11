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

class SignUpUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var signUpWithCodeUseCase: SignUpWithCodeUseCase

    @Before
    fun setUp() {
        repository = mockk()
        signUpUseCase = SignUpUseCase(repository)
        signUpWithCodeUseCase = SignUpWithCodeUseCase(repository)
    }

    @Test
    fun `SignUp with empty email should return InvalidEmail exception`() = runBlocking {
        val result = signUpUseCase("", "password123", "user123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUp with email without at symbol should return InvalidEmail exception`() = runBlocking {
        val result = signUpUseCase("invalidemail", "password123", "user123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUp with short password should return PasswordTooShort exception`() = runBlocking {
        val result = signUpUseCase("test@gmail.com", "123", "user123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.PasswordTooShort, result.exceptionOrNull())
    }

    @Test
    fun `SignUp with empty username should return EmptyField exception`() = runBlocking {
        val result = signUpUseCase("test@gmail.com", "password123", "")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `SignUp with valid data should return success`() = runBlocking {
        val mockUser = mockk<AuthUser>()
        coEvery { repository.signUp(any(), any(), any()) } returns Result.success(mockUser)

        val result = signUpUseCase("test@gmail.com", "password123", "user123")
        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
    }

    @Test
    fun `SignUpWithCode with empty email should return InvalidEmail`() = runBlocking {
        val result = signUpWithCodeUseCase("", "password123", "user", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with email without at symbol should return InvalidEmail`() = runBlocking {
        val result = signUpWithCodeUseCase("invalid", "123456", "user", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with short password should return PasswordTooShort`() = runBlocking {
        val result = signUpWithCodeUseCase("test@gmail.com", "123", "user", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.PasswordTooShort, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with empty staff code should return InvalidStaffCode`() = runBlocking {
        val result = signUpWithCodeUseCase("test@gmail.com", "123456", "user", "")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidStaffCode, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with valid data should return success`() = runBlocking {
        val mockUser = mockk<AuthUser>()
        coEvery { repository.signUpWithCode(any(), any(), any(), any()) } returns Result.success(mockUser)

        val result = signUpWithCodeUseCase("test@gmail.com", "password123", "user123", "ST123")
        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
    }
}
