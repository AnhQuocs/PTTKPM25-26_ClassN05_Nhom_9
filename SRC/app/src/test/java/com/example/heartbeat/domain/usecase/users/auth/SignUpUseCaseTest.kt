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

    // --- 1. Boundary Value Testing & Validation (SignUpUseCase) ---

    @Test
    fun `SignUp with blank email should return InvalidEmail`() = runTest {
        val result = signUpUseCase("   ", "password123", "user123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
        coVerify(exactly = 0) { repository.signUp(any(), any(), any()) }
    }

    @Test
    fun `SignUp with email missing @ symbol should return InvalidEmail`() = runTest {
        val result = signUpUseCase("invalid-email", "password123", "user123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUp with password exactly 5 characters should return PasswordTooShort`() = runTest {
        val result = signUpUseCase("test@gmail.com", "12345", "user123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.PasswordTooShort, result.exceptionOrNull())
    }

    @Test
    fun `SignUp with blank username should return EmptyField`() = runTest {
        val result = signUpUseCase("test@gmail.com", "password123", "   ")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    // --- 2. Validation (SignUpWithCodeUseCase) ---

    @Test
    fun `SignUpWithCode with blank email should return InvalidEmail`() = runTest {
        val result = signUpWithCodeUseCase("   ", "password123", "user123", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with email missing @ symbol should return InvalidEmail`() = runTest {
        val result = signUpWithCodeUseCase("invalid-email", "password123", "user123", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with short password should return PasswordTooShort`() = runTest {
        val result = signUpWithCodeUseCase("test@gmail.com", "12345", "user123", "ST123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.PasswordTooShort, result.exceptionOrNull())
    }

    @Test
    fun `SignUpWithCode with blank staff code should return InvalidStaffCode`() = runTest {
        val result = signUpWithCodeUseCase("test@gmail.com", "password123", "user", "  ")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidStaffCode, result.exceptionOrNull())
    }

    // --- 3. Behavior & Success Cases ---

    @Test
    fun `SignUp with valid data should call repository with correct parameters`() = runTest {
        val email = "test@gmail.com"
        val pass = "password123"
        val user = "username"
        val mockUser = mockk<AuthUser>()
        coEvery { repository.signUp(email, pass, user) } returns Result.success(mockUser)

        val result = signUpUseCase(email, pass, user)

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
        coVerify(exactly = 1) { repository.signUp(email, pass, user) }
        confirmVerified(repository)
    }

    @Test
    fun `SignUpWithCode should verify all parameters are passed to repository`() = runTest {
        val email = "staff@gmail.com"
        val pass = "password123"
        val name = "StaffUser"
        val code = "ST123"
        val mockUser = mockk<AuthUser>()
        coEvery { repository.signUpWithCode(email, pass, name, code) } returns Result.success(mockUser)

        val result = signUpWithCodeUseCase(email, pass, name, code)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.signUpWithCode(email, pass, name, code) }
    }

    // --- 4. Exception & Negative Testing ---

    @Test
    fun `SignUp should return failure when email already exists`() = runTest {
        val email = "existing@gmail.com"
        coEvery { repository.signUp(email, any(), any()) } returns Result.failure(AuthException.UserAlreadyExists)

        val result = signUpUseCase(email, "password123", "user")

        assertTrue(result.isFailure)
        assertEquals(AuthException.UserAlreadyExists, result.exceptionOrNull())
    }

    @Test
    fun `SignUp should handle unexpected repository exceptions`() = runTest {
        coEvery { repository.signUp(any(), any(), any()) } throws RuntimeException("Database error")

        val result = signUpUseCase("test@gmail.com", "password123", "user")

        assertTrue(result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}
