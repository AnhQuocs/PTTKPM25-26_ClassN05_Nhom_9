package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.search.SearchResultItem
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `UnifiedSuggestion map to SuggestionItem`() = runBlocking {
        coEvery { unifiedSearchUseCase("q") } returns listOf(SearchResultItem.EventItem(event1))
        val result = unifiedSuggestionUseCase("q")
        assertTrue(result[0] is SearchSuggestionItem.EventSuggestion)
    }

    @Test
    fun `UnifiedSuggestion handle multiple results`() = runBlocking {
        coEvery { unifiedSearchUseCase(any()) } returns listOf(SearchResultItem.EventItem(event1), SearchResultItem.EventItem(event1))
        assertEquals(2, unifiedSuggestionUseCase("any").size)
    }

    @Test
    fun `UnifiedSuggestion handle empty results`() = runBlocking {
        coEvery { unifiedSearchUseCase(any()) } returns emptyList()
        assertTrue(unifiedSuggestionUseCase("none").isEmpty())
    }

    @Test(expected = Exception::class)
    fun `UnifiedSuggestion propagate exception`() {
        runBlocking {
            coEvery { unifiedSearchUseCase(any()) } throws Exception("Error")
            unifiedSuggestionUseCase("error")
        }
    }
}