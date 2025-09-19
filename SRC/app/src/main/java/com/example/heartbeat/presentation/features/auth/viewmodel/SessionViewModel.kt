package com.example.heartbeat.presentation.features.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.user.AuthUser
import com.example.heartbeat.domain.usecase.auth.AuthUseCases
import com.example.heartbeat.domain.usecase.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _startDestination = MutableStateFlow("onboarding")
    val startDestination: StateFlow<String?> = _startDestination

    init {
        if(authUseCases.checkUserLoggedInUseCase()) {
            _startDestination.value = "main"
        } else {
            _startDestination.value = "onboarding"
        }
        Log.d("AuthDeBug", "SessionViewModel-Route: ${startDestination.value}")
    }
}