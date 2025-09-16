package com.example.heartbeat.di

import android.content.Context
import com.example.heartbeat.data.preferences.language.LanguagePreferenceManager
import com.example.heartbeat.data.repository.hospital.HospitalRepositoryImpl
import com.example.heartbeat.data.source.remote.FirebaseHospitalDataSource
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import com.example.heartbeat.domain.usecase.hospital.GetAllHospitalsUseCase
import com.example.heartbeat.domain.usecase.hospital.GetHospitalByIdUseCase
import com.example.heartbeat.domain.usecase.hospital.HospitalUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // LANGUAGE
    @dagger.Provides
    @Singleton
    fun provideLanguagePreferenceManager(
        @ApplicationContext context: Context
    ): LanguagePreferenceManager = LanguagePreferenceManager(context)

    // FIRE STORE
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // HOSPITAL
    @Provides
    @Singleton
    fun provideHospitalRepository(
        dataSource: FirebaseHospitalDataSource
    ): HospitalRepository {
        return HospitalRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideHospitalUseCase(repository: HospitalRepository) = HospitalUseCase(
        getAllHospitalsUseCase = GetAllHospitalsUseCase(repository),
        getHospitalByIdUseCase = GetHospitalByIdUseCase(repository)
    )
}