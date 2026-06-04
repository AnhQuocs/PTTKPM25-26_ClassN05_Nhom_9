package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.search.SearchResultItem
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
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

class UnifiedSuggestionUseCaseTest {
    private lateinit var unifiedSearchUseCase: UnifiedSearchUseCase
    private lateinit var unifiedSuggestionUseCase: UnifiedSuggestionUseCase
    private val now = LocalDateTime(2024, 1, 1, 0, 0)
    private val event1 = Event("e1", "h1", "Event 1", "Desc", "2024-01-01", "08:00", null, emptyList(), 100, 0, now)

    @Before
    fun setUp() {
        unifiedSearchUseCase = mockk()
        unifiedSuggestionUseCase = UnifiedSuggestionUseCase(unifiedSearchUseCase)
    }

    @Test
    fun `UnifiedSuggestion map to SuggestionItem - runTest & Behavior Verification`() = runTest {
        // 1. Coroutine Testing with runTest
        coEvery { unifiedSearchUseCase("q") } returns Result.success(listOf(SearchResultItem.EventItem(event1)))
        
        val result = unifiedSuggestionUseCase("q")
        
        // 2. Behavior Verification
        coVerify(exactly = 1) { unifiedSearchUseCase("q") }
        confirmVerified(unifiedSearchUseCase)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.get(0) is SearchSuggestionItem.EventSuggestion)
    }

    @Test
    fun `UnifiedSuggestion handle multiple results`() = runTest {
        coEvery { unifiedSearchUseCase(any()) } returns Result.success(listOf(
            SearchResultItem.EventItem(event1), 
            SearchResultItem.EventItem(event1)
        ))
        val result = unifiedSuggestionUseCase("any")
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `UnifiedSuggestion handle empty results - Boundary Value`() = runTest {
        // 4. Boundary Value Testing
        coEvery { unifiedSearchUseCase(any()) } returns Result.success(emptyList())
        val result = unifiedSuggestionUseCase("none")
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `UnifiedSuggestion handle error - Exception Injection`() = runTest {
        // 3. Exception & Error Injection
        val exception = RuntimeException("Suggestion Failed")
        coEvery { unifiedSearchUseCase(any()) } throws exception
        
        val result = unifiedSuggestionUseCase("error")
        
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `UnifiedSuggestion return failure result from search usecase`() = runTest {
        val exception = Exception("Search Error")
        coEvery { unifiedSearchUseCase(any()) } returns Result.failure(exception)
        
        val result = unifiedSuggestionUseCase("fail")
        
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
