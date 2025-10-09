package com.example.heartbeat.di

import com.example.heartbeat.data.repository.users.donor.DonorRepositoryImpl
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import com.example.heartbeat.domain.usecase.users.donor.AddDonorUseCase
import com.example.heartbeat.domain.usecase.users.donor.DonorUseCase
import com.example.heartbeat.domain.usecase.users.donor.GetAvatarUseCase
import com.example.heartbeat.domain.usecase.users.donor.GetCurrentDonorUseCase
import com.example.heartbeat.domain.usecase.users.donor.GetDonorByIdUseCase
import com.example.heartbeat.domain.usecase.users.donor.IsDonorProfileExistUseCase
import com.example.heartbeat.domain.usecase.users.donor.SaveAvatarUrlUseCase
import com.example.heartbeat.domain.usecase.users.donor.UpdateAvatarUseCase
import com.example.heartbeat.domain.usecase.users.donor.UpdateDonorUseCase
import com.example.heartbeat.domain.usecase.users.donor.UploadAvatarUseCase
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
        getCurrentDonorUseCase = GetCurrentDonorUseCase(repository),
        getDonorByIdUseCase = GetDonorByIdUseCase(repository),
        updateDonorUseCase = UpdateDonorUseCase(repository),
        isDonorProfileExistUseCase = IsDonorProfileExistUseCase(repository),
        uploadAvatarUseCase = UploadAvatarUseCase(repository),
        updateAvatarUseCase = UpdateAvatarUseCase(repository),
        saveAvatarUrlUseCase = SaveAvatarUrlUseCase(repository),
        getAvatarUseCase = GetAvatarUseCase(repository)
    )
}