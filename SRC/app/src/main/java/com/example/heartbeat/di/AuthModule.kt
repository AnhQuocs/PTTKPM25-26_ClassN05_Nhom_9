package com.example.heartbeat.di

import com.example.heartbeat.data.repository.auth.AuthRepositoryImpl
import com.example.heartbeat.domain.repository.auth.AuthRepository
import com.example.heartbeat.domain.usecase.auth.AuthUseCases
import com.example.heartbeat.domain.usecase.auth.GetCurrentUserUseCase
import com.example.heartbeat.domain.usecase.auth.LogOutUseCase
import com.example.heartbeat.domain.usecase.auth.LoginUseCase
import com.example.heartbeat.domain.usecase.auth.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule{
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(auth, firestore)

    @Provides
    fun provideAuthUseCase(repository: AuthRepository) = AuthUseCases(
        signUp = SignUpUseCase(repository),
        login = LoginUseCase(repository),
        logout = LogOutUseCase(repository),
        getCurrentUser = GetCurrentUserUseCase(repository)
    )
}