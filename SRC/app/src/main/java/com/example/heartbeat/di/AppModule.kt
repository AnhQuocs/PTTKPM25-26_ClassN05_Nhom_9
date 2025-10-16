package com.example.heartbeat.di

import android.content.Context
import com.example.heartbeat.data.preferences.language.LanguagePreferenceManager
import com.example.heartbeat.data.repository.hospital.HospitalRepositoryImpl
import com.example.heartbeat.data.repository.recent_search.RecentSearchRepositoryImpl
import com.example.heartbeat.data.repository.recent_viewed.RecentViewedRepositoryImpl
import com.example.heartbeat.data.source.remote.FirebaseHospitalDataSource
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import com.example.heartbeat.domain.repository.recent_search.RecentSearchRepository
import com.example.heartbeat.domain.repository.recent_viewed.RecentViewedRepository
import com.example.heartbeat.domain.usecase.hospital.GetAllHospitalsUseCase
import com.example.heartbeat.domain.usecase.hospital.GetHospitalByIdUseCase
import com.example.heartbeat.domain.usecase.hospital.HospitalUseCase
import com.example.heartbeat.domain.usecase.recent_search.AddRecentSearchUseCase
import com.example.heartbeat.domain.usecase.recent_search.ClearAllRecentSearchUseCase
import com.example.heartbeat.domain.usecase.recent_search.GetRecentSearchUseCase
import com.example.heartbeat.domain.usecase.recent_search.RecentSearchUseCase
import com.example.heartbeat.domain.usecase.recent_viewed.AddRecentViewedUseCase
import com.example.heartbeat.domain.usecase.recent_viewed.ClearRecentViewedUseCase
import com.example.heartbeat.domain.usecase.recent_viewed.GetRecentViewedUseCase
import com.example.heartbeat.domain.usecase.recent_viewed.RecentViewedUseCase
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
    fun provideHospitalDataSource(): FirebaseHospitalDataSource =
        FirebaseHospitalDataSource()

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

    // RECENT SEARCH
    @Provides
    @Singleton
    fun provideRecentSearchRepository(
        firestore: FirebaseFirestore
    ): RecentSearchRepository {
        return RecentSearchRepositoryImpl(firestore)
    }
    
    @Provides
    @Singleton
    fun provideRecentSearchUseCase(repository: RecentSearchRepository) = RecentSearchUseCase(
        addRecentSearchUseCase = AddRecentSearchUseCase(repository),
        getRecentSearchUseCase = GetRecentSearchUseCase(repository),
        clearAllRecentSearchUseCase = ClearAllRecentSearchUseCase(repository)
    )

    // RECENT VIEWED
    @Provides
    @Singleton
    fun provideRecentViewedRepository(
        firestore: FirebaseFirestore
    ): RecentViewedRepository {
        return RecentViewedRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideRecentViewedUseCase(repository: RecentViewedRepository) = RecentViewedUseCase(
        addRecentViewedUseCase = AddRecentViewedUseCase(repository),
        getRecentViewedUseCase = GetRecentViewedUseCase(repository),
        clearRecentViewedUseCase = ClearRecentViewedUseCase(repository)
    )
}