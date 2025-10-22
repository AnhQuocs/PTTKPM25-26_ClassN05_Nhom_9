package com.example.heartbeat.di

import com.example.heartbeat.data.repository.event.EventRepositoryImpl
import com.example.heartbeat.data.source.remote.EventRemoteDataSource
import com.example.heartbeat.domain.repository.event.EventRepository
import com.example.heartbeat.domain.usecase.event.AddEventUseCase
import com.example.heartbeat.domain.usecase.event.DeleteEventUseCase
import com.example.heartbeat.domain.usecase.event.EventUseCase
import com.example.heartbeat.domain.usecase.event.GetEventByIdUseCase
import com.example.heartbeat.domain.usecase.event.ObserveAllEventsUseCase
import com.example.heartbeat.domain.usecase.event.ObserveDonorCountUseCase
import com.example.heartbeat.domain.usecase.event.ObserveDonorListUseCase
import com.example.heartbeat.domain.usecase.event.ObserveEventByIdUseCase
import com.example.heartbeat.domain.usecase.event.ObserveEventsByDateUseCase
import com.example.heartbeat.domain.usecase.event.UpdateDonorCountUseCase
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
    fun provideEventRemoteDataSource(
        firestore: FirebaseFirestore
    ): EventRemoteDataSource = EventRemoteDataSource(firestore)

    @Provides
    @Singleton
    fun provideEventRepository(
        firestore: FirebaseFirestore,
        dataSource: EventRemoteDataSource
    ): EventRepository = EventRepositoryImpl(dataSource, firestore)

    @Provides
    @Singleton
    fun provideEventUseCase(repository: EventRepository): EventUseCase = EventUseCase(
        addEventUseCase = AddEventUseCase(repository),
        getEventByIdUseCase = GetEventByIdUseCase(repository),
        observeAllEventsUseCase = ObserveAllEventsUseCase(repository),
        updateEventUseCase = UpdateEventUseCase(repository),
        deleteEventUseCase = DeleteEventUseCase(repository),
        observeDonorCountUseCase = ObserveDonorCountUseCase(repository),
        observeDonorListUseCase = ObserveDonorListUseCase(repository),
        observeEventsByDateUseCase = ObserveEventsByDateUseCase(repository),
        updateDonorCountUseCase = UpdateDonorCountUseCase(repository),
        observeEventByIdUseCase = ObserveEventByIdUseCase(repository)
    )
}