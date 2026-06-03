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

    // ĐỔI từ delegate sang backing state
    private val _hospitals = mutableStateOf<List<Hospital>>(emptyList())
    val hospitals: List<Hospital> get() = _hospitals.value

    private val _hospitalDetails = mutableStateOf<Map<String, Hospital>>(emptyMap())
    val hospitalDetails: Map<String, Hospital> get() = _hospitalDetails.value

    fun loadHospitals() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data = hospitalUseCase.getAllHospitalsUseCase()
                _hospitals.value = data
            } catch (e: Throwable) {
                // Xử lý lỗi
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadHospitalById(hospitalId: String) {
        if (_hospitalDetails.value.containsKey(hospitalId)) return

        viewModelScope.launch {
            try {
                val hospital = hospitalUseCase.getHospitalByIdUseCase(hospitalId)
                hospital?.let {
                    _hospitalDetails.value += (hospitalId to it)
                }
            } catch (e: Throwable) {
                // Đảm bảo coroutine kết thúc an toàn
            }
        }
    }
}