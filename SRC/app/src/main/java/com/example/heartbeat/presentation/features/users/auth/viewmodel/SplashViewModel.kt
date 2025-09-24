package com.example.heartbeat.presentation.features.users.auth.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.data.preferences.language.languageDataStore
import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.usecase.users.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val onboardingDataStore: OnboardingDataStore
) : ViewModel() {

    val currentUser = MutableStateFlow<AuthUser?>(null)
    val hasOnboarded = MutableStateFlow(false)
    val isLoading = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            currentUser.value = getCurrentUserUseCase()
            hasOnboarded.value = onboardingDataStore.hasOnboarded.first()
            isLoading.value = false
        }
    }
}

class OnboardingDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.languageDataStore
    private val hasOnboardedKey = booleanPreferencesKey("hasOnboarded")

    val hasOnboarded: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[hasOnboardedKey] ?: false }

    suspend fun setHasOnboarded(value: Boolean) {
        dataStore.edit { prefs -> prefs[hasOnboardedKey] = value }
    }
}