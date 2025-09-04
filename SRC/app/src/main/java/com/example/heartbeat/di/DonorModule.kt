package com.example.heartbeat.di

import com.example.heartbeat.data.repository.donor.DonorRepositoryImpl
import com.example.heartbeat.domain.repository.donor.DonorRepository
import com.example.heartbeat.domain.usecase.donor.AddDonorUseCase
import com.example.heartbeat.domain.usecase.donor.DonorUseCase
import com.example.heartbeat.domain.usecase.donor.GetDonorUseCase
import com.example.heartbeat.domain.usecase.donor.UpdateDonorUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DonorModule {
    @Provides
    @Singleton
    fun provideDonorRepository(
        firestore: FirebaseFirestore
    ): DonorRepository {
        return DonorRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideDonorUseCase(repository: DonorRepository) = DonorUseCase(
        addDonorUseCase = AddDonorUseCase(repository),
        getDonorUseCase = GetDonorUseCase(repository),
        updateDonorUseCase = UpdateDonorUseCase(repository)
    )
}