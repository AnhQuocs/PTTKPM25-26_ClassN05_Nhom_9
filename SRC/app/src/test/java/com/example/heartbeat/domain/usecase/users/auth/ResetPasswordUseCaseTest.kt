package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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

    @Test
    fun `ResetPassword with empty email should return InvalidEmail`() = runBlocking {
        // Phủ nhánh 1: email.isBlank() == true
        val result = resetPasswordUseCase("")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `ResetPassword with invalid email format should return InvalidEmail`() = runBlocking {
        // Phủ nhánh 2: isBlank == false và contains("@") == false
        val result = resetPasswordUseCase("wrong-email")
        assertTrue(result.isFailure)
        assertEquals(AuthException.InvalidEmail, result.exceptionOrNull())
    }

    @Test
    fun `ResetPassword with valid email should return success`() = runBlocking {
        // Phủ nhánh 3: Cả 2 vế đều false
        coEvery { repository.resetPassword(any()) } returns Result.success(Unit)
        val result = resetPasswordUseCase("test@gmail.com")
        assertTrue(result.isSuccess)
    }
}
