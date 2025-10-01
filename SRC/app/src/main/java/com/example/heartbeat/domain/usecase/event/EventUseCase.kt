package com.example.heartbeat.domain.usecase.event

class EventUseCase (
    val addEventUseCase: AddEventUseCase,
    val getEventByIdUseCase: GetEventByIdUseCase,
    val observeAllEventsUseCase: ObserveAllEventsUseCase,
    val updateEventUseCase: UpdateEventUseCase,
    val deleteEventUseCase: DeleteEventUseCase,
    val observeDonorCountUseCase: ObserveDonorCountUseCase,
    val observeDonorListUseCase: ObserveDonorListUseCase
)