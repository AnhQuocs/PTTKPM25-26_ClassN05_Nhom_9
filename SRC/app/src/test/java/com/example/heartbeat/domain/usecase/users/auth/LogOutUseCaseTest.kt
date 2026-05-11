package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `Logout should call repository logout`() = runBlocking {
        coEvery { repository.logout() } returns Unit
        logoutUseCase()
        coVerify(exactly = 1) { repository.logout() }
    }
}
