package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateProfileUseCasesTest {

    private lateinit var repository: AuthRepository
    private lateinit var updateUserNameUseCase: UpdateUserNameUseCase
    private lateinit var updatePasswordUseCase: UpdatePasswordUseCase

    @Before
    fun setUp() {
        repository = mockk()
        updateUserNameUseCase = UpdateUserNameUseCase(repository)
        updatePasswordUseCase = UpdatePasswordUseCase(repository)
    }

    @Test
    fun `UpdateUserName with blank name should return EmptyField`() = runBlocking {
        val result = updateUserNameUseCase("")
        assertTrue(result.isFailure)
        assertEquals(AuthException.EmptyField, result.exceptionOrNull())
    }

    @Test
    fun `UpdateUserName with valid name should return success`() = runBlocking {
        coEvery { repository.updateUsername(any()) } returns Result.success(Unit)
        val result = updateUserNameUseCase("New Name")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `UpdatePassword with short password should return PasswordTooShort`() = runBlocking {
        val result = updatePasswordUseCase("123")
        assertTrue(result.isFailure)
        assertEquals(AuthException.PasswordTooShort, result.exceptionOrNull())
    }

    @Test
    fun `UpdatePassword with valid password should return success`() = runBlocking {
        coEvery { repository.updatePassword(any()) } returns Result.success(Unit)
        val result = updatePasswordUseCase("newpassword123")
        assertTrue(result.isSuccess)
    }
}
