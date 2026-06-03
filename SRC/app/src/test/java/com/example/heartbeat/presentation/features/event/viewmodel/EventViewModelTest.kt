package com.example.heartbeat.presentation.features.event.viewmodel

import android.util.Log
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.usecase.event.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModelTest {

    private lateinit var viewModel: EventViewModel
    private lateinit var eventUseCase: EventUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val addEventUseCase = mockk<AddEventUseCase>(relaxed = true)
    private val getEventByIdUseCase = mockk<GetEventByIdUseCase>(relaxed = true)
    private val observeAllEventsUseCase = mockk<ObserveAllEventsUseCase>(relaxed = true)
    private val updateEventUseCase = mockk<UpdateEventUseCase>(relaxed = true)
    private val deleteEventUseCase = mockk<DeleteEventUseCase>(relaxed = true)
    private val observeDonorCountUseCase = mockk<ObserveDonorCountUseCase>(relaxed = true)
    private val observeEventsByDateUseCase = mockk<ObserveEventsByDateUseCase>(relaxed = true)
    private val updateDonorCountUseCase = mockk<UpdateDonorCountUseCase>(relaxed = true)
    private val observeEventByIdUseCase = mockk<ObserveEventByIdUseCase>(relaxed = true)

    private val testEvent = Event(
        id = "E001",
        locationId = "L001",
        name = "Test Event",
        description = "Desc",
        date = "2024-06-01",
        time = "08:00",
        deadline = null,
        donorList = emptyList(),
        capacity = 100,
        donorCount = 0,
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        eventUseCase = EventUseCase(
            addEventUseCase, getEventByIdUseCase, observeAllEventsUseCase,
            updateEventUseCase, deleteEventUseCase, observeDonorCountUseCase,
            observeEventsByDateUseCase, updateDonorCountUseCase, observeEventByIdUseCase
        )

        every { observeAllEventsUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `init block covers observeEvents success path with events and loop`() = runTest {
        val eventList = listOf(testEvent)
        every { observeAllEventsUseCase() } returns flowOf(eventList)
        
        viewModel = EventViewModel(eventUseCase)
        advanceUntilIdle()
        
        assertEquals(eventList, viewModel.events.value)
        verify { observeDonorCountUseCase(testEvent.id, any()) }
    }

    @Test
    fun `init block covers observeEvents catch path`() = runTest {
        val errorMsg = "Initial Error"
        every { observeAllEventsUseCase() } returns flow { throw Exception(errorMsg) }
        
        viewModel = EventViewModel(eventUseCase)
        advanceUntilIdle()
        
        assertEquals(errorMsg, viewModel.error.value)
    }

    @Test
    fun `observeEventsByDate covers success, cancel job, loop and default param`() = runTest {
        val eventList = listOf(testEvent)
        every { observeEventsByDateUseCase(any()) } returns flowOf(eventList)
        
        viewModel = EventViewModel(eventUseCase)
        
        viewModel.observeEventsByDate()
        assertTrue(viewModel.isLoading.value)
        
        viewModel.observeEventsByDate(LocalDate.of(2024, 6, 1))
        
        advanceUntilIdle()
        
        assertEquals(eventList, viewModel.filteredEvents.value)
        assertFalse(viewModel.isLoading.value)
        verify(atLeast = 1) { Log.d("EventDebug", any()) }
        verify { observeDonorCountUseCase(testEvent.id, any()) }
    }

    @Test
    fun `observeEventsByDate covers catch path`() = runTest {
        val errorMsg = "Filter Error"
        every { observeEventsByDateUseCase(any()) } returns flow { throw Exception(errorMsg) }
        
        viewModel = EventViewModel(eventUseCase)
        viewModel.observeEventsByDate()
        advanceUntilIdle()
        
        assertEquals(errorMsg, viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `getEventById covers success and catch path`() = runTest {
        coEvery { getEventByIdUseCase("E1") } returns testEvent
        viewModel = EventViewModel(eventUseCase)
        
        viewModel.getEventById("E1")
        advanceUntilIdle()
        assertEquals(testEvent, viewModel.selectedEvent.value)
        
        coEvery { getEventByIdUseCase("E2") } throws Exception("Get Error")
        viewModel.getEventById("E2")
        advanceUntilIdle()
        assertEquals("Get Error", viewModel.error.value)
    }

    @Test
    fun `observeEventById covers success, cancel job and catch path`() = runTest {
        every { observeEventByIdUseCase("E1") } returns flowOf(testEvent)
        viewModel = EventViewModel(eventUseCase)
        
        viewModel.observeEventById("E1")
        viewModel.observeEventById("E1")
        
        advanceUntilIdle()
        assertEquals(testEvent, viewModel.observedEvent.value)
        
        val errorMsg = "Single Error"
        every { observeEventByIdUseCase("E2") } returns flow { throw Exception(errorMsg) }
        viewModel.observeEventById("E2")
        advanceUntilIdle()
        assertEquals(errorMsg, viewModel.error.value)
    }

    @Test
    fun `addEvent covers success and catch-finally path`() = runTest {
        viewModel = EventViewModel(eventUseCase)
        
        coEvery { addEventUseCase(testEvent) } just runs
        viewModel.addEvent(testEvent)
        assertTrue(viewModel.isLoading.value)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
        
        val errorMsg = "Add Error"
        coEvery { addEventUseCase(any()) } throws Exception(errorMsg)
        viewModel.addEvent(testEvent)
        advanceUntilIdle()
        assertEquals(errorMsg, viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `updateEvent covers success and catch path`() = runTest {
        viewModel = EventViewModel(eventUseCase)
        
        coEvery { updateEventUseCase("E1", testEvent) } just runs
        viewModel.updateEvent("E1", testEvent)
        advanceUntilIdle()
        
        val errorMsg = "Update Error"
        coEvery { updateEventUseCase(any(), any()) } throws Exception(errorMsg)
        viewModel.updateEvent("E1", testEvent)
        advanceUntilIdle()
        assertEquals(errorMsg, viewModel.error.value)
    }

    @Test
    fun `updateDonorCount covers success and catch-finally path`() = runTest {
        viewModel = EventViewModel(eventUseCase)
        
        coEvery { updateDonorCountUseCase("E1", 1) } just runs
        viewModel.updateDonorCount("E1", 1)
        assertTrue(viewModel.isLoading.value)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
        
        val errorMsg = "Donor Error"
        coEvery { updateDonorCountUseCase(any(), any()) } throws Exception(errorMsg)
        viewModel.updateDonorCount("E1", 1)
        advanceUntilIdle()
        assertEquals(errorMsg, viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `deleteEvent covers success and catch path`() = runTest {
        viewModel = EventViewModel(eventUseCase)
        
        coEvery { deleteEventUseCase("E1") } just runs
        viewModel.deleteEvent("E1")
        advanceUntilIdle()
        
        val errorMsg = "Delete Error"
        coEvery { deleteEventUseCase(any()) } throws Exception(errorMsg)
        viewModel.deleteEvent("E1")
        advanceUntilIdle()
        assertEquals(errorMsg, viewModel.error.value)
    }

    @Test
    fun `observeDonorCount covers mapping logic (if-else branches)`() = runTest {
        val e1 = testEvent.copy(id = "E1", donorCount = 0)
        val e2 = testEvent.copy(id = "E2", donorCount = 0)
        every { observeAllEventsUseCase() } returns flowOf(listOf(e1, e2))
        
        val slot = slot<(Int) -> Unit>()
        every { observeDonorCountUseCase("E1", capture(slot)) } just runs
        
        viewModel = EventViewModel(eventUseCase)
        advanceUntilIdle()
        
        slot.captured.invoke(50)
        
        val updatedEvents = viewModel.events.value
        assertEquals(50, updatedEvents.find { it.id == "E1" }?.donorCount)
        assertEquals(0, updatedEvents.find { it.id == "E2" }?.donorCount) // ELSE branch
    }

    @Test
    fun `getEventByIdDirect covers success and non-null assertion failure`() = runTest {
        viewModel = EventViewModel(eventUseCase)
        
        coEvery { getEventByIdUseCase("E1") } returns testEvent
        val result = viewModel.getEventByIdDirect("E1")
        assertEquals(testEvent, result)
        
        coEvery { getEventByIdUseCase("E2") } returns null
        try {
            viewModel.getEventByIdDirect("E2")
            fail("Should have thrown NullPointerException")
        } catch (e: NullPointerException) {
            // expected
        }
    }
}
