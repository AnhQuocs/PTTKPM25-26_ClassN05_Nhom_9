package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.search.SearchResultItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UnifiedSearchUseCaseTest {
    private lateinit var searchEventsUseCase: SearchEventsUseCase
    private lateinit var unifiedSearchUseCase: UnifiedSearchUseCase
    private val now = LocalDateTime(2024, 1, 1, 0, 0)
    private val event1 = Event("e1", "h1", "Event 1", "Desc", "2024-01-01", "08:00", null, emptyList(), 100, 0, now)

    @Before
    fun setUp() {
        searchEventsUseCase = mockk()
        unifiedSearchUseCase = UnifiedSearchUseCase(searchEventsUseCase)
    }

    @Test
    fun `UnifiedSearch map Event to Item - runTest & Behavior Verification`() = runTest {
        // 1. Coroutine Testing with runTest
        coEvery { searchEventsUseCase("q") } returns Result.success(listOf(event1))
        
        val result = unifiedSearchUseCase("q")
        
        // 2. Behavior Verification
        coVerify(exactly = 1) { searchEventsUseCase("q") }
        confirmVerified(searchEventsUseCase)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.get(0) is SearchResultItem.EventItem)
    }

    @Test
    fun `UnifiedSearch handle multiple items`() = runTest {
        coEvery { searchEventsUseCase(any()) } returns Result.success(listOf(event1, event1))
        val result = unifiedSearchUseCase("any")
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `UnifiedSearch handle empty results - Boundary Value`() = runTest {
        // 4. Boundary Value Testing
        coEvery { searchEventsUseCase(any()) } returns Result.success(emptyList())
        val result = unifiedSearchUseCase("none")
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `UnifiedSearch handle error - Exception Injection`() = runTest {
        // 3. Exception & Error Injection
        val exception = RuntimeException("Search Failed")
        coEvery { searchEventsUseCase(any()) } throws exception
        
        val result = unifiedSearchUseCase("error")
        
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
    
    @Test
    fun `UnifiedSearch return failure result from child usecase`() = runTest {
        val exception = Exception("Domain Error")
        coEvery { searchEventsUseCase(any()) } returns Result.failure(exception)
        
        val result = unifiedSearchUseCase("fail")
        
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
