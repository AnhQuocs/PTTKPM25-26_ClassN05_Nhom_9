package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test

class EventUpdateUseCasesTest {
    private lateinit var repository: EventRepository

    @Before
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `UpdateEvent should call repository`() = runBlocking {
        val eventId = "e1"
        val event = mockk<Event>()
        coEvery { repository.updateEvent(eventId, event) } returns Unit

        UpdateEventUseCase(repository).invoke(eventId, event)

        coVerify(exactly = 1) { repository.updateEvent(eventId, event) }
    }

    @Test
    fun `UpdateDonorCount should call repository`() = runBlocking {
        val eventId = "e1"
        val delta = 1
        coEvery { repository.updateDonorCount(eventId, delta) } returns Unit

        UpdateDonorCountUseCase(repository).invoke(eventId, delta)

        coVerify(exactly = 1) { repository.updateDonorCount(eventId, delta) }
    }

    @Test
    fun `ObserveDonorCount should call repository`() {
        val eventId = "e1"
        val onUpdate: (Int) -> Unit = {}
        every { repository.observeDonorCount(eventId, any()) } returns Unit

        ObserveDonorCountUseCase(repository).invoke(eventId, onUpdate)

        verify(exactly = 1) { repository.observeDonorCount(eventId, any()) }
    }
}
