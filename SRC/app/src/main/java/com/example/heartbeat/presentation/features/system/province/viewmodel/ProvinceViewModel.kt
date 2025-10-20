package com.example.heartbeat.presentation.features.system.province.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.usecase.system.province.GetAllProvincesUseCase
import com.example.heartbeat.domain.usecase.system.province.GetProvinceByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvinceViewModel @Inject constructor(
    private val getAllProvincesUseCase: GetAllProvincesUseCase,
    private val getProvinceByIdUseCase: GetProvinceByIdUseCase
) : ViewModel() {

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces

    private val _selectedProvince = MutableStateFlow<Province?>(null)
    val selectedProvince: StateFlow<Province?> = _selectedProvince

    init {
        viewModelScope.launch {
            _provinces.value = getAllProvincesUseCase()
        }
    }

    fun loadProvinceById(id: String) {
        viewModelScope.launch {
            _selectedProvince.value = getProvinceByIdUseCase(id)
        }
    }
}