package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class EventUseCaseWrapperTest {

    @Test
    fun `EventUseCase wrapper should hold all use cases`() {
        val repository = mockk<EventRepository>()
        val eventUseCase = EventUseCase(
            addEventUseCase = AddEventUseCase(repository),
            getEventByIdUseCase = GetEventByIdUseCase(repository),
            observeAllEventsUseCase = ObserveAllEventsUseCase(repository),
            updateEventUseCase = UpdateEventUseCase(repository),
            deleteEventUseCase = DeleteEventUseCase(repository),
            observeDonorCountUseCase = ObserveDonorCountUseCase(repository),
            observeEventsByDateUseCase = ObserveEventsByDateUseCase(repository),
            updateDonorCountUseCase = UpdateDonorCountUseCase(repository),
            observeEventByIdUseCase = ObserveEventByIdUseCase(repository)
        )

        assertNotNull(eventUseCase.addEventUseCase)
        assertNotNull(eventUseCase.getEventByIdUseCase)
        assertNotNull(eventUseCase.observeAllEventsUseCase)
        assertNotNull(eventUseCase.updateEventUseCase)
        assertNotNull(eventUseCase.deleteEventUseCase)
        assertNotNull(eventUseCase.observeDonorCountUseCase)
        assertNotNull(eventUseCase.observeEventsByDateUseCase)
        assertNotNull(eventUseCase.updateDonorCountUseCase)
        assertNotNull(eventUseCase.observeEventByIdUseCase)
    }
}
