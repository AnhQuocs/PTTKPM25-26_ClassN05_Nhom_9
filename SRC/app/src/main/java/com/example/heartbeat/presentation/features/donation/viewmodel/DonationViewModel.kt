package com.example.heartbeat.presentation.features.donation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DonationUiState(
    val isLoading: Boolean = false,
    val donations: List<Donation> = emptyList(),
    val selectedDonation: Donation? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val donationUseCases: DonationUseCases
) : ViewModel () {
    private val _uiState = MutableStateFlow(DonationUiState())
    val uiState: StateFlow<DonationUiState> = _uiState

    init {
        Log.d("DonationVM", "Instance hash: ${this.hashCode()}")
        observePendingDonations()
    }

    // CREATE
    fun addDonation(donation: Donation) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val result = donationUseCases.addDonation(donation)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Text Success",
                        donations = it.donations + listOfNotNull(result)
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    // READ
    fun observeDonation(donationId: String) {
        viewModelScope.launch {
            donationUseCases.observeDonationById(donationId)
                .collect { donation ->
                    _uiState.update { it.copy(selectedDonation = donation) }
                }
        }
    }

    fun getDonationsByDonor(donorId: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val list = donationUseCases.getDonationsByDonor(donorId)
            _uiState.update { it.copy(isLoading = false, donations = list) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    fun getDonationsByEvent(eventId: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val list = donationUseCases.getDonationsByEvent(eventId)
            _uiState.update { it.copy(isLoading = false, donations = list) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    // UPDATE
    fun updateDonation(donation: Donation) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val updated = donationUseCases.updateDonation(donation)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    successMessage = "Donation updated",
                    donations = it.donations.map { d -> if (d.donationId == updated.donationId) updated else d }
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    fun updateStatus(donationId: String, status: String) = viewModelScope.launch {
        try {
            val updated = donationUseCases.updateStatus(donationId, status)
            updated?.let {
                _uiState.update { state ->
                    state.copy(
                        donations = state.donations.map { d -> if (d.donationId == it.donationId) it else d },
                        successMessage = "Status updated"
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message) }
        }
    }

    fun updateDonationVolume(donationId: String, volume: String) = viewModelScope.launch {
        try {
            val updated = donationUseCases.updateDonationVolume(donationId, volume)
            updated?.let {
                _uiState.update { state ->
                    state.copy(
                        donations = state.donations.map { d -> if (d.donationId == it.donationId) it else d },
                        successMessage = "Volume updated"
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message) }
        }
    }

    // DELETE
    fun deleteDonation(donationId: String) = viewModelScope.launch {
        try {
            val success = donationUseCases.deleteDonation(donationId)
            if (success) {
                _uiState.update { state ->
                    state.copy(
                        donations = state.donations.filterNot { it.donationId == donationId },
                        successMessage = "Donation deleted"
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message) }
        }
    }

    // OBSERVE
    private fun observePendingDonations() {
        viewModelScope.launch {
            donationUseCases.observePendingDonations()
                .collect { donations ->
                    _uiState.update { it.copy(donations = donations) }
                }
        }
    }

    fun observeDonationsByEvent(eventId: String) {
        viewModelScope.launch {
            donationUseCases.observeDonationsByEvent(eventId)
                .collect { donations ->
                    val donationForEvent = donations.firstOrNull { it.eventId == eventId }

                    _uiState.update { state ->
                        state.copy(
                            donations = donations,
                            selectedDonation = donationForEvent ?: state.selectedDonation
                        )
                    }
                }
        }
    }

    // reset state
    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}