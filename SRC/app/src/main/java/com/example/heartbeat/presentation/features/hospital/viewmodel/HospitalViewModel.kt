package com.example.heartbeat.presentation.features.hospital.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.usecase.hospital.HospitalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val hospitalUseCase: HospitalUseCase
) : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    var hospitals by mutableStateOf<List<Hospital>>(emptyList())
        private set

    var hospitalDetails by mutableStateOf<Map<String, Hospital>>(emptyMap())
        private set

    fun loadHospitals() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = hospitalUseCase.getAllHospitalsUseCase()
            hospitals = data
            _isLoading.value = false
        }
    }

    fun loadHospitalById(hospitalId: String) {
        val normalizedHospitalId = hospitalId.trim()
        if (
            normalizedHospitalId.isEmpty() ||
            normalizedHospitalId == "hospitals" ||
            hospitalDetails.containsKey(normalizedHospitalId)
        ) return

        viewModelScope.launch {
            val hospital = hospitalUseCase.getHospitalByIdUseCase(normalizedHospitalId)
            hospital?.let {
                hospitalDetails = hospitalDetails + (normalizedHospitalId to it)
            }
        }
    }
}