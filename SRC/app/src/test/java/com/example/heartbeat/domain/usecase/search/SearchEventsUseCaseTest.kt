package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.event.EventRepository
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
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

class SearchEventsUseCaseTest {

    private lateinit var eventRepository: EventRepository
    private lateinit var hospitalRepository: HospitalRepository
    private lateinit var searchEventsUseCase: SearchEventsUseCase

    private val now = LocalDateTime(2024, 1, 1, 0, 0)

    private val hospital1 = Hospital(
        hospitalId   = "h1",
        hospitalName = "Central Hospital",
        imgUrl       = "",
        address      = "Addr 1",
        phone        = "123",
        province     = "Hanoi",
        district     = "Ba Dinh"
    )

    private val event1 = Event("e1", "h1", "Big Donation", "Desc", "2024-02-01", "08:00", null, emptyList(), 100, 0, now)
    private val event2 = Event("e2", "h1", "Urgent", "Desc", "2024-02-05", "09:00", null, emptyList(), 50, 0, now)
    private val event3 = Event("e3", "h1", "Blood", "Desc", "2024-03-01", "10:00", null, emptyList(), 200, 0, now)
    private val event4 = Event("e4", "unknown", "Orphan Event", "Desc", "2024-04-01", "11:00", null, emptyList(), 10, 0, now)

    @Before
    fun setUp() {
        eventRepository = mockk()
        hospitalRepository = mockk()
        searchEventsUseCase = SearchEventsUseCase(eventRepository, hospitalRepository)

        coEvery { hospitalRepository.getAllHospitals() } returns listOf(hospital1)
        coEvery { eventRepository.getAllEvents() } returns listOf(event1, event2, event3, event4)
    }

    @Test
    fun `Match Event Name branch - Coroutine runTest & Behavior Verification`() = runTest {
        // 1. Coroutine Testing with runTest
        val result = searchEventsUseCase("Big")
        
        // 2. Behavior Verification
        coVerify(exactly = 1) { eventRepository.getAllEvents() }
        coVerify(exactly = 1) { hospitalRepository.getAllHospitals() }
        confirmVerified(eventRepository, hospitalRepository)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("e1", result.getOrNull()?.get(0)?.id)
    }

    @Test
    fun `Match Hospital Name branch`() = runTest {
        val result = searchEventsUseCase("Central")
        assertTrue(result.getOrNull()?.any { it.id == "e2" } == true)
    }

    @Test
    fun `Match Province branch`() = runTest {
        val result = searchEventsUseCase("Hanoi")
        assertTrue(result.getOrNull()?.any { it.id == "e3" } == true)
    }

    @Test
    fun `Search No Match branch`() = runTest {
        val result = searchEventsUseCase("Saigon")
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `Boundary Value Testing - Empty and Messy Spaces`() = runTest {
        // 4. Boundary Value Testing
        val resultEmpty = searchEventsUseCase("")
        assertEquals(4, resultEmpty.getOrNull()?.size)

        val resultSpaces = searchEventsUseCase("   ")
        assertEquals(4, resultSpaces.getOrNull()?.size)

        val resultMessy = searchEventsUseCase("   Big    Donation   ")
        assertEquals(1, resultMessy.getOrNull()?.size)
    }

    @Test
    fun `Exception Injection - Repository Failure`() = runTest {
        // 3. Exception & Error Injection
        coEvery { eventRepository.getAllEvents() } throws RuntimeException("Database Error")
        
        val result = searchEventsUseCase("any")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        assertEquals("Database Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `Branch Coverage - Hospital Null branch`() = runTest {
        // 5. Branch Coverage
        val result = searchEventsUseCase("Orphan")
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("e4", result.getOrNull()?.get(0)?.id)
    }

    @Test
    fun `Case Insensitive match`() = runTest {
        val result = searchEventsUseCase("bIg dOnAtIoN")
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `Multiple tokens - All must match`() = runTest {
        val resultSuccess = searchEventsUseCase("Big Central")
        assertEquals(1, resultSuccess.getOrNull()?.size)

        val resultFail = searchEventsUseCase("Big Unknown")
        assertTrue(resultFail.getOrNull()?.isEmpty() == true)
    }
}
