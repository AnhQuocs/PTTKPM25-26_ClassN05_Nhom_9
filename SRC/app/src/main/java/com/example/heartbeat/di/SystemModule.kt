package com.example.heartbeat.di

import com.example.heartbeat.data.repository.system.ProvinceRepositoryImpl
import com.example.heartbeat.data.source.remote.FirebaseProvinceDataSource
import com.example.heartbeat.domain.repository.system.ProvinceRepository
import com.example.heartbeat.domain.usecase.system.province.GetAllProvincesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemModule {

    // PROVINCE
    @Provides
    @Singleton
    fun provideProvinceDataSource(): FirebaseProvinceDataSource {
        return FirebaseProvinceDataSource()
    }

    @Provides
    @Singleton
    fun provideProvinceRepository(
        dataSource: FirebaseProvinceDataSource
    ): ProvinceRepository {
        return ProvinceRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetAllProvinceUseCase(
        repository: ProvinceRepository
    ) = GetAllProvincesUseCase(repository)
}