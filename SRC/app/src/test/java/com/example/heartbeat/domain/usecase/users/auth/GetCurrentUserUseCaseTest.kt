package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
    fun `GetCurrentUser should return user from repository`() = runBlocking {
        val mockUser = mockk<AuthUser>()
        coEvery { repository.getCurrentUser() } returns mockUser
        
        val result = getCurrentUserUseCase()
        assertEquals(mockUser, result)
    }
}
