package com.example.heartbeat.presentation.features.donor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.user.Donor
import com.example.heartbeat.domain.usecase.donor.DonorUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonorViewModel @Inject constructor(
    private val donorUseCase: DonorUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(DonorFormState())
    val formState: StateFlow<DonorFormState> = _formState

    fun updatePersonalInfo(
        name: String,
        phoneNumber: String,
        bloodGroup: String,
        city: String
    ) {
        _formState.update {
            it.copy(
                name = name,
                phoneNumber = phoneNumber,
                bloodGroup = bloodGroup,
                city = city
            )
        }
    }

    fun updateAdditionalInfo(
        dateOfBirth: String,
        age: Int,
        gender: String,
        willingToDonate: Boolean,
        about: String,
    ) {
        _formState.update {
            it.copy(
                dateOfBirth = dateOfBirth,
                age = age,
                gender = gender,
                willingToDonate = willingToDonate,
                about = about
            )
        }
    }

    fun updatePersonalAvatar(
        profileAvatar: String
    ) {
        _formState.update {
            it.copy(
                profileAvatar = profileAvatar
            )
        }
    }

    fun submitDonor() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "test"

        val donor = Donor(
            donorId = userId,
            name = _formState.value.name,
            phoneNumber = _formState.value.phoneNumber,
            bloodGroup = _formState.value.bloodGroup,
            city = _formState.value.city,
            dateOfBirth = _formState.value.dateOfBirth,
            age = _formState.value.age,
            gender = _formState.value.gender,
            willingToDonate = _formState.value.willingToDonate,
            about = _formState.value.about,
            profileAvatar = _formState.value.profileAvatar
        )

        viewModelScope.launch {
            try {
                _formState.update { it.copy(isLoading = true, error = null, isSubmitSuccess = false) }
                donorUseCase.addDonorUseCase(donor)
                _formState.update { it.copy(isLoading = false, isSubmitSuccess = true) } // thành công
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, error = e.message, isSubmitSuccess = false) }
            }
        }
    }

    fun setStep(step: Int) {
        _formState.update { it.copy(currentStep = step) }
    }
}