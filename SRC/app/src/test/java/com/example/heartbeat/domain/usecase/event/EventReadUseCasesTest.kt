package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class EventReadUseCasesTest {
    private lateinit var repository: EventRepository

    @Before
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `GetEventById should return event from repository`() = runBlocking {
        val eventId = "e1"
        val mockEvent = mockk<Event>()
        coEvery { repository.getEventById(eventId) } returns mockEvent

        val result = GetEventByIdUseCase(repository).invoke(eventId)

        assertEquals(mockEvent, result)
    }

    @Test
    fun `ObserveAllEvents should return flow from repository`() = runBlocking {
        val mockList = listOf(mockk<Event>())
        every { repository.observeAllEvents() } returns flowOf(mockList)

        ObserveAllEventsUseCase(repository).invoke().collect {
            assertEquals(mockList, it)
        }
    }

    @Test
    fun `ObserveEventById should return flow from repository`() = runBlocking {
        val eventId = "e1"
        val mockEvent = mockk<Event>()
        every { repository.observeEventById(eventId) } returns flowOf(mockEvent)

        ObserveEventByIdUseCase(repository).invoke(eventId).collect {
            assertEquals(mockEvent, it)
        }
    }

    @Test
    fun `ObserveEventsByDate with parameter should return flow`() = runBlocking {
        val date = LocalDate.now()
        val mockList = listOf(mockk<Event>())
        every { repository.observeEventsByDate(date) } returns flowOf(mockList)

        ObserveEventsByDateUseCase(repository).invoke(date).collect {
            assertEquals(mockList, it)
        }
    }

    @Test
    fun `ObserveEventsByDate without parameter should use default date`() = runBlocking {
        val mockList = listOf(mockk<Event>())
        every { repository.observeEventsByDate(any()) } returns flowOf(mockList)

        ObserveEventsByDateUseCase(repository).invoke().collect {
            assertEquals(mockList, it)
        }
    }
}
