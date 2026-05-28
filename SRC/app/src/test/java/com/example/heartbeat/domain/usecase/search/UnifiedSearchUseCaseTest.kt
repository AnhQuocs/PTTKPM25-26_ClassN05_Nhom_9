package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.search.SearchResultItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `UnifiedSearch map Event to Item`() = runBlocking {
        coEvery { searchEventsUseCase("q") } returns listOf(event1)
        val result = unifiedSearchUseCase("q")
        assertTrue(result[0] is SearchResultItem.EventItem)
    }

    @Test
    fun `UnifiedSearch handle multiple items`() = runBlocking {
        coEvery { searchEventsUseCase(any()) } returns listOf(event1, event1)
        assertEquals(2, unifiedSearchUseCase("any").size)
    }

    @Test
    fun `UnifiedSearch handle empty results`() = runBlocking {
        coEvery { searchEventsUseCase(any()) } returns emptyList()
        assertTrue(unifiedSearchUseCase("none").isEmpty())
    }

    @Test(expected = Exception::class)
    fun `UnifiedSearch propagate error`() {
        runBlocking {
            coEvery { searchEventsUseCase(any()) } throws Exception("Fail")
            unifiedSearchUseCase("error")
        }
    }
}