package com.example.heartbeat.domain.usecase.users.auth

import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckUserLoggedInUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var checkUserLoggedInUseCase: CheckUserLoggedInUseCase

    @Before
    fun setUp() {
        repository = mockk()
        checkUserLoggedInUseCase = CheckUserLoggedInUseCase(repository)
    }

    @Test
    fun `CheckUserLoggedIn should return true when repo returns true`() {
        every { repository.isUserLoggedIn() } returns true
        assertTrue(checkUserLoggedInUseCase())
    }

    @Test
    fun `CheckUserLoggedIn should return false when repo returns false`() {
        every { repository.isUserLoggedIn() } returns false
        assertFalse(checkUserLoggedInUseCase())
    }
}
