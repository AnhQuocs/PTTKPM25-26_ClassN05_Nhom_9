package com.example.heartbeat.domain.usecase.users.auth

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

class ResetPasswordUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var resetPasswordUseCase: ResetPasswordUseCase

    @Before
    fun setUp() {
        repository = mockk()
        resetPasswordUseCase = ResetPasswordUseCase(repository)
    }

    // --- 1. Boundary Value Testing ---

    @Test
    fun `ResetPassword with blank email should return InvalidEmail`() = runTest {
        val result = resetPasswordUseCase("   ")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
        
        // Verify: Repository must NOT be called
        coVerify(exactly = 0) { repository.resetPassword(any()) }
    }

    @Test
    fun `ResetPassword with invalid email format should return InvalidEmail`() = runTest {
        val result = resetPasswordUseCase("user-at-domain.com")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    // --- 2. Behavior Verification Testing ---

    @Test
    fun `ResetPassword with valid email should call repository once and return success`() = runTest {
        val email = "test@gmail.com"
        coEvery { repository.resetPassword(email) } returns Result.success(Unit)
        
        val result = resetPasswordUseCase(email)
        
        assertTrue(result.isSuccess)
        // Verify correct interaction
        coVerify(exactly = 1) { repository.resetPassword(email) }
        confirmVerified(repository)
    }

    // --- 3. Exception & Error Injection Testing ---

    @Test
    fun `ResetPassword should handle repository exceptions (e_g_ Network Error)`() = runTest {
        val email = "test@gmail.com"
        coEvery { repository.resetPassword(email) } throws Exception("No Internet Connection")
        
        val result = resetPasswordUseCase(email)
        
        assertTrue(result.isFailure)
        assertEquals("No Internet Connection", result.exceptionOrNull()?.message)
    }

    // --- 4. Negative Testing ---

    @Test
    fun `ResetPassword should return failure when user email is not found`() = runTest {
        val email = "notfound@gmail.com"
        coEvery { repository.resetPassword(email) } returns Result.failure(AuthException.UserNotFound)
        
        val result = resetPasswordUseCase(email)
        
        assertTrue(result.isFailure)
        assertEquals(AuthException.UserNotFound, result.exceptionOrNull())
    }
}
