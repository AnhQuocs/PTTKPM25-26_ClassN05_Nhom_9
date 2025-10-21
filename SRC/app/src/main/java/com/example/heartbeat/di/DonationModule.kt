package com.example.heartbeat.di

import com.example.heartbeat.data.repository.donation.DonationRepositoryImpl
import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.create.AddDonationUseCase
import com.example.heartbeat.domain.usecase.donation.delete.DeleteDonationUseCase
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import com.example.heartbeat.domain.usecase.donation.read.GetAllDonationsListUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetAllDonationsUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByDayUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationByIdUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByMonthUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByWeekUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObservePendingDonationsUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationVolumeUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateStatusUseCase
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
            observeDonationByDonorUseCase = ObserveDonationByDonorUseCase(repository),
            getDonationsByDayUseCase = GetDonationsByDayUseCase(repository),
            getDonationsByWeekUseCase = GetDonationsByWeekUseCase(repository),
            getDonationsByMonthUseCase = GetDonationsByMonthUseCase(repository),
            getAllDonationsUseCase = GetAllDonationsUseCase(repository),
            getAllDonationsListUseCase = GetAllDonationsListUseCase(repository)
        )
    }
}