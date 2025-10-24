package com.example.heartbeat.presentation.features.event.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.usecase.event.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventUseCase: EventUseCase
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _filteredEvents = MutableStateFlow<List<Event>>(emptyList())
    val filteredEvents: StateFlow<List<Event>> = _filteredEvents

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    private val _observedEvent = MutableStateFlow<Event?>(null)
    val observedEvent: StateFlow<Event?> = _observedEvent

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var observeJob: Job? = null
    private var observeSingleEventJob: Job? = null

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            eventUseCase.observeAllEventsUseCase()
                .catch { e -> _error.value = e.message }
                .collect { events ->
                    _events.value = events

                    events.forEach { event ->
                        observeDonorCount(event.id)
                    }
                }
        }
    }

    fun observeEventsByDate(selectedDate: LocalDate = LocalDate.now()) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            _isLoading.value = true
            eventUseCase.observeEventsByDateUseCase(selectedDate)
                .catch { e -> _error.value = e.message }
                .collect { events ->
                    _filteredEvents.value = events
                    _isLoading.value = false

                    Log.d("EventDebug", "Loaded ${events.size} events for $selectedDate")

                    events.forEach { event ->
                        observeDonorCount(event.id)
                    }
                }
        }
    }

    fun getEventById(id: String) {
        viewModelScope.launch {
            try {
                val result = eventUseCase.getEventByIdUseCase(id)
                _selectedEvent.value = result
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun observeEventById(eventId: String) {
        observeSingleEventJob?.cancel()
        observeSingleEventJob = viewModelScope.launch {
            eventUseCase.observeEventByIdUseCase(eventId)
                .catch { e -> _error.value = e.message }
                .collect { event ->
                    _observedEvent.value = event
                }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                eventUseCase.addEventUseCase(event)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateEvent(id: String, event: Event) {
        viewModelScope.launch {
            try {
                eventUseCase.updateEventUseCase(id, event)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateDonorCount(eventId: String, delta: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                eventUseCase.updateDonorCountUseCase(eventId, delta)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            try {
                eventUseCase.deleteEventUseCase(id)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun observeDonorCount(eventId: String) {
        eventUseCase.observeDonorCountUseCase(eventId) { donorCount ->
            _events.value = _events.value.map { event ->
                if (event.id == eventId) event.copy(donorCount = donorCount)
                else event
            }
        }
    }

    suspend fun getEventByIdDirect(id: String): Event {
        return eventUseCase.getEventByIdUseCase(id)!!
    }
}