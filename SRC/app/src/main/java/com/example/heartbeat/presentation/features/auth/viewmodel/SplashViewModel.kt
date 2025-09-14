package com.example.heartbeat.presentation.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.example.heartbeat.domain.usecase.auth.CheckUserLoggedUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    private val _startDestination = MutableStateFlow<String>("onboarding")
    val startDestination: StateFlow<String> = _startDestination

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _startDestination.value = if(firebaseAuth.currentUser != null) "main" else "onboarding"
        }
    }
}