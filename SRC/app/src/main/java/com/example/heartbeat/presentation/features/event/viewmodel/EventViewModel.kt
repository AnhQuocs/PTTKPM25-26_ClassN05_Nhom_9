package com.example.heartbeat.presentation.features.event.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.usecase.event.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventUseCase: EventUseCase
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            try {
                eventUseCase.deleteEventUseCase(id)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private fun observeDonorCount(eventId: String) {
        eventUseCase.observeDonorCountUseCase(eventId) { donorCount ->
            _events.value = _events.value.map { event ->
                if (event.id == eventId) event.copy(donorCount = donorCount)
                else event
            }
        }
    }

    fun observeDonorList(eventId: String) {
        eventUseCase.observeDonorListUseCase(eventId) { donorList ->
            _events.value = _events.value.map { event ->
                if (event.id == eventId) event.copy(donorList = donorList)
                else event
            }

            _selectedEvent.value = _selectedEvent.value?.copy(donorList = donorList)
        }
    }
}