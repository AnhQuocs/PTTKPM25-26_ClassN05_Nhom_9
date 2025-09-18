package com.example.heartbeat.presentation.features.event.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.usecase.event.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun getAllEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = eventUseCase.getAllEventsUseCase()
                _events.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
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
                eventUseCase.addEventUseCase(event)
                getAllEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateEvent(id: String, event: Event) {
        viewModelScope.launch {
            try {
                eventUseCase.updateEventUseCase(id, event)
                getAllEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            try {
                eventUseCase.deleteEventUseCase(id)
                getAllEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}