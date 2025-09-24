package com.example.heartbeat.di

import com.example.heartbeat.data.repository.event.EventRepositoryImpl
import com.example.heartbeat.domain.repository.event.EventRepository
import com.example.heartbeat.domain.usecase.event.AddEventUseCase
import com.example.heartbeat.domain.usecase.event.DeleteEventUseCase
import com.example.heartbeat.domain.usecase.event.EventUseCase
import com.example.heartbeat.domain.usecase.event.GetAllEventsUseCase
import com.example.heartbeat.domain.usecase.event.GetEventByIdUseCase
import com.example.heartbeat.domain.usecase.event.ObserveDonorCountUseCase
import com.example.heartbeat.domain.usecase.event.UpdateEventUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideEventRepository(
        firestore: FirebaseFirestore
    ): EventRepository {
        return EventRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideEventUseCase(repository: EventRepository) = EventUseCase(
        addEventUseCase = AddEventUseCase(repository),
        getEventByIdUseCase = GetEventByIdUseCase(repository),
        getAllEventsUseCase = GetAllEventsUseCase(repository),
        updateEventUseCase = UpdateEventUseCase(repository),
        deleteEventUseCase = DeleteEventUseCase(repository),
        observeDonorCountUseCase = ObserveDonorCountUseCase(repository)
    )
}