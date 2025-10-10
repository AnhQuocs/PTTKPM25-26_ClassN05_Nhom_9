package com.example.heartbeat.di

import com.example.heartbeat.data.repository.donation.DonationRepositoryImpl
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.AddDonationUseCase
import com.example.heartbeat.domain.usecase.donation.DeleteDonationUseCase
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import com.example.heartbeat.domain.usecase.donation.ObserveDonationByIdUseCase
import com.example.heartbeat.domain.usecase.donation.GetDonationsByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.GetDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.ObserveDonationByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.ObserveDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.ObservePendingDonationsUseCase
import com.example.heartbeat.domain.usecase.donation.UpdateDonationUseCase
import com.example.heartbeat.domain.usecase.donation.UpdateDonationVolumeUseCase
import com.example.heartbeat.domain.usecase.donation.UpdateStatusUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DonationModule {

    @Provides
    @Singleton
    fun provideDonationRepository(
        firestore: FirebaseFirestore
    ): DonationRepository = DonationRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideDonationUseCases(repository: DonationRepository): DonationUseCases {
        return DonationUseCases(
            addDonation = AddDonationUseCase(repository),
            observeDonationById = ObserveDonationByIdUseCase(repository),
            getDonationsByDonor = GetDonationsByDonorUseCase(repository),
            getDonationsByEvent = GetDonationsByEventUseCase(repository),
            updateDonation = UpdateDonationUseCase(repository),
            updateStatus = UpdateStatusUseCase(repository),
            updateDonationVolume = UpdateDonationVolumeUseCase(repository),
            deleteDonation = DeleteDonationUseCase(repository),
            observePendingDonations = ObservePendingDonationsUseCase(repository),
            observeDonationsByEvent = ObserveDonationsByEventUseCase(repository),
            observeDonationByDonorUseCase = ObserveDonationByDonorUseCase(repository)
        )
    }
}