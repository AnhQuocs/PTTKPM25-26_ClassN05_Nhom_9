package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetCurrentUserUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getCurrentUserUseCase = GetCurrentUserUseCase(repository)
    }

    @Test
    fun `GetCurrentUser should return user from repository when logged in`() = runTest {
        // Behavior Verification & Positive Case
        val mockUser = mockk<AuthUser>()
        coEvery { repository.getCurrentUser() } returns mockUser
        
        val result = getCurrentUserUseCase()
        
        assertEquals(mockUser, result)
        coVerify(exactly = 1) { repository.getCurrentUser() }
        confirmVerified(repository)
    }

    @Test
    fun `GetCurrentUser should return null when no user is logged in`() = runTest {
        // Negative Testing
        coEvery { repository.getCurrentUser() } returns null
        
        val result = getCurrentUserUseCase()
        
        assertNull(result)
        coVerify(exactly = 1) { repository.getCurrentUser() }
    }

    @Test
    fun `GetCurrentUser should handle repository exceptions`() = runTest {
        // Exception Injection
        val errorMessage = "Database error"
        coEvery { repository.getCurrentUser() } throws Exception(errorMessage)
        
        try {
            getCurrentUserUseCase()
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
        
        coVerify(exactly = 1) { repository.getCurrentUser() }
    }
}
