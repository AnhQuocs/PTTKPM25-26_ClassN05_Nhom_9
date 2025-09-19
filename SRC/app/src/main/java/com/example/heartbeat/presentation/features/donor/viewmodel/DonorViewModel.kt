package com.example.heartbeat.presentation.features.donor.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.user.Donor
import com.example.heartbeat.domain.entity.user.DonorAvatar
import com.example.heartbeat.domain.usecase.donor.DonorUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DonorViewModel @Inject constructor(
    private val donorUseCase: DonorUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(DonorFormState())
    val formState: StateFlow<DonorFormState> = _formState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _donorAvatar = MutableStateFlow<DonorAvatar?>(null)
    val donorAvatar: StateFlow<DonorAvatar?> = _donorAvatar

    //Personal info
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

    // Basic info
    fun updateBasicInfo(
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

    // Avatar
    fun setLocalAvatar(uri: Uri?) {
        _formState.update { it.copy(profileAvatar = uri?.toString() ?: "") }
    }

    // Convert URI sang Base64
    private fun uriToBase64(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Cannot open input stream from URI: $uri")
        val bytes = inputStream.readBytes()
        inputStream.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    // Submit donor + upload avatar
    fun submitDonor(context: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                _formState.update { it.copy(isLoading = true, error = null, isSubmitSuccess = false) }

                // Upload avatar
                val avatarUri = _formState.value.profileAvatar
                if (avatarUri.isNotBlank() && avatarUri.startsWith("content://")) {
                    val base64 = uriToBase64(Uri.parse(avatarUri), context)
                    val avatarUrl = donorUseCase.uploadAvatarUseCase(userId, base64)

                    val donorAvatar = DonorAvatar(userId, avatarUrl)
                    donorUseCase.saveAvatarUrlUseCase(donorAvatar)
                }

                // Donor obj
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
                    about = _formState.value.about
                )

                // add donor
                donorUseCase.addDonorUseCase(donor)

                _formState.update { it.copy(isLoading = false, isSubmitSuccess = true) }
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, error = e.message, isSubmitSuccess = false) }
            }
        }
    }

    fun setStep(step: Int) {
        _formState.update { it.copy(currentStep = step) }
    }

    fun getDonor(onProfileExists: (Boolean) -> Unit = {}) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                _isLoading.value = true

                val exists = donorUseCase.isDonorProfileExistUseCase(userId)

                if (exists) {
                    val donor = donorUseCase.getDonorUseCase(userId)!!

                    _formState.update { current ->
                        current.copy(
                            name = donor.name,
                            phoneNumber = donor.phoneNumber,
                            bloodGroup = donor.bloodGroup,
                            city = donor.city,
                            dateOfBirth = donor.dateOfBirth,
                            age = donor.age,
                            gender = donor.gender,
                            willingToDonate = donor.willingToDonate,
                            about = donor.about
                        )
                    }
                }

                onProfileExists(exists)

            } catch (e: Exception) {
                _formState.update { it.copy(error = e.message) }
                onProfileExists(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAvatar(userId: String) {
        viewModelScope.launch {
            try {
                val avatar = donorUseCase.getAvatarUseCase(userId)
                _donorAvatar.value = avatar
            } catch (e: Exception) {
                _formState.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearError() {
        _formState.update { it.copy(error = null) }
    }
}