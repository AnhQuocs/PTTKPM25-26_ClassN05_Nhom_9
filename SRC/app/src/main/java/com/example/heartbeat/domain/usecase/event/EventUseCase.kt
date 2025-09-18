package com.example.heartbeat.domain.usecase.event

class EventUseCase (
    val addEventUseCase: AddEventUseCase,
    val getEventByIdUseCase: GetEventByIdUseCase,
    val getAllEventsUseCase: GetAllEventsUseCase,
    val updateEventUseCase: UpdateEventUseCase,
    val deleteEventUseCase: DeleteEventUseCase
)