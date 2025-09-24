package com.example.heartbeat.di

import com.example.heartbeat.data.repository.users.auth.AuthRepositoryImpl
import com.example.heartbeat.domain.repository.users.auth.AuthRepository
import com.example.heartbeat.domain.usecase.users.auth.AuthUseCases
import com.example.heartbeat.domain.usecase.users.auth.CheckUserLoggedInUseCase
import com.example.heartbeat.domain.usecase.users.auth.GetCurrentUserUseCase
import com.example.heartbeat.domain.usecase.users.auth.LogOutUseCase
import com.example.heartbeat.domain.usecase.users.auth.LoginUseCase
import com.example.heartbeat.domain.usecase.users.auth.LoginWithCodeUseCase
import com.example.heartbeat.domain.usecase.users.auth.ResetPasswordUseCase
import com.example.heartbeat.domain.usecase.users.auth.SignUpUseCase
import com.example.heartbeat.domain.usecase.users.auth.SignUpWithCodeUseCase
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
        signUpWithCode = SignUpWithCodeUseCase(repository),
        login = LoginUseCase(repository),
        loginWithCode = LoginWithCodeUseCase(repository),
        logout = LogOutUseCase(repository),
        getCurrentUser = GetCurrentUserUseCase(repository),
        resetPassword = ResetPasswordUseCase(repository),
        checkUserLoggedInUseCase = CheckUserLoggedInUseCase(repository)
    )
}