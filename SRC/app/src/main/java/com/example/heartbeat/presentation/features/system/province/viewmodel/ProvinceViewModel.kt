package com.example.heartbeat.presentation.features.system.province.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.usecase.system.province.GetAllProvincesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvinceViewModel @Inject constructor(
    private val getAllProvincesUseCase: GetAllProvincesUseCase
) : ViewModel() {

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces

    init {
        viewModelScope.launch {
            _provinces.value = getAllProvincesUseCase()
        }
    }
}