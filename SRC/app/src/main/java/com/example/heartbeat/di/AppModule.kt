package com.example.heartbeat.di

import android.content.Context
import androidx.test.espresso.core.internal.deps.dagger.Module
import com.example.heartbeat.data.preferences.language.LanguagePreferenceManager
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
}