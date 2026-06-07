package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteEventUseCaseTest {
    private lateinit var repository: EventRepository
    private lateinit var deleteEventUseCase: DeleteEventUseCase

    @Before
    fun setUp() {
        repository = mockk()
        deleteEventUseCase = DeleteEventUseCase(repository)
    }

    @Test
    fun `DeleteEvent should call repository`() = runBlocking {
        val eventId = "e1"
        coEvery { repository.deleteEvent(any()) } returns Unit

        deleteEventUseCase(eventId)

        coVerify(exactly = 1) { repository.deleteEvent(eventId) }
    }

    @Test
    fun `DeleteEvent with blank ID should NOT call repository`() = runBlocking {
        val eventId = ""
        coEvery { repository.deleteEvent(any()) } returns Unit

        deleteEventUseCase(eventId)


        coVerify(exactly = 0) { repository.deleteEvent(any()) }
    }
}
