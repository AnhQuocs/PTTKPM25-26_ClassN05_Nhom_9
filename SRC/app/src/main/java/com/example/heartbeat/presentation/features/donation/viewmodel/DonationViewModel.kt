package com.example.heartbeat.presentation.features.donation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.hospital.viewmodel.HospitalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DonationUiState(
    val isLoading: Boolean = false,
    val donations: List<Donation> = emptyList(),
    val donatedList: List<Donation> = emptyList(),
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

    private var observeJob: Job? = null

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
    fun getDonationsByDonor(donorId: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val list = donationUseCases.getDonationsByDonor(donorId)
            _uiState.update { it.copy(isLoading = false, donations = list) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    fun getDonatedDonations(donorId: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val allDonations = donationUseCases.getDonationsByDonor(donorId)
            val donated = allDonations.filter { it.status == "DONATED" }
            _uiState.update { it.copy(isLoading = false, donatedList = donated) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    // UPDATE
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
        _uiState.update { it.copy(isLoading = true) }
        try {
            val updated = donationUseCases.updateDonationVolume(donationId, volume)
            updated?.let {
                _uiState.update { state ->
                    state.copy(
                        donations = state.donations.map { d ->
                            if (d.donationId == it.donationId) it else d
                        },
                        successMessage = "Volume updated",
                        isLoading = false
                    )
                }
            }
            updateStatus(donationId = donationId, status = "DONATED")
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun approveDonation(donationId: String, donorId: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
        try {
            donationUseCases.approveDonationUseCase(donationId, donorId)

            _uiState.update { state ->
                val updatedDonations = state.donations.map { donation ->
                    if (donation.donationId == donationId) {
                        donation.copy(status = "APPROVED")
                    } else donation
                }

                state.copy(
                    donations = updatedDonations,
                    successMessage = "Donation approved",
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
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
    fun observePendingDonations() {
        viewModelScope.launch {
            donationUseCases.observePendingDonations()
                .collect { donations ->
                    _uiState.update { it.copy(donations = donations) }
                }
        }
    }

    fun observeDonationsByEvent(eventId: String) {
        observeJob?.cancel()

        observeJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val startTime = System.currentTimeMillis()

            try {
                donationUseCases.observeDonationsByEvent(eventId)
                    .collect { donations ->
                        val elapsed = System.currentTimeMillis() - startTime
                        if (elapsed < 500) delay(500 - elapsed)

                        Log.d("DonationVM", "📡 Update Firestore | eventId=$eventId | total=${donations.size}")

                        val filtered = donations.filter { it.eventId == eventId }
                        Log.d("DonationVM", "-> After filter ${filtered.size} donations\n")

                        _uiState.update {
                            it.copy(
                                donations = filtered,
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("DonationVM", "observeDonationsByEvent error: ${e.message}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun getAllDonatedDonations() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val allDonations = donationUseCases.getAllDonationsListUseCase()
            val donated = allDonations.filter { it.status == "DONATED" }
            _uiState.update { it.copy(isLoading = false, donatedList = donated) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    fun observeDonationForDonor(eventId: String, donorId: String) {
        viewModelScope.launch {
            donationUseCases.observeDonationByDonorUseCase(eventId, donorId)
                .collect { donation ->
                    _uiState.update { state ->
                        state.copy(selectedDonation = donation)
                    }
                }
        }
    }

    // reset state
    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}