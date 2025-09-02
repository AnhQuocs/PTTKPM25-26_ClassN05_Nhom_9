package com.example.heartbeat.presentation.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.example.heartbeat.domain.usecase.auth.CheckUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserLoggedUseCase: CheckUserLoggedUseCase
): ViewModel() {

    private val _startDestination = MutableStateFlow("onboarding")
    val startDestination: StateFlow<String> = _startDestination

    init {
        if(checkUserLoggedUseCase()) {
            _startDestination.value = "main"
        } else {
            _startDestination.value = "onboarding"
        }
    }
}