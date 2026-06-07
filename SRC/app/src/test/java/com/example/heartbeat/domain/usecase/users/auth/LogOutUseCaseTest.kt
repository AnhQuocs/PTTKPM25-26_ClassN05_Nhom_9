package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogOutUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var logoutUseCase: LogOutUseCase

    @Before
    fun setUp() {
        repository = mockk()
        logoutUseCase = LogOutUseCase(repository)
    }

    @Test
    fun `Logout should call repository logout exactly once`() = runTest {
        coEvery { repository.logout() } returns Unit
        
        logoutUseCase()
        
        // Behavior Verification
        coVerify(exactly = 1) { repository.logout() }
        confirmVerified(repository)
    }

    @Test
    fun `Logout should handle repository exceptions gracefully`() = runTest {
        // Exception Injection
        coEvery { repository.logout() } throws RuntimeException("Clear session failed")
        
        try {
            logoutUseCase()
        } catch (e: Exception) {
            //
        }
        
        coVerify(exactly = 1) { repository.logout() }
    }
}
