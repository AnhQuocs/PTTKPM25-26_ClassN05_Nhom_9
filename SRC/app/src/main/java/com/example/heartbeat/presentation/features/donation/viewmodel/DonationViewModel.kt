package com.example.heartbeat.presentation.features.donation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.donation.Donation
import com.example.heartbeat.domain.usecase.donation.DonationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            donationUseCases.addDonation(donation).onSuccess { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Donation added successfully",
                        donations = it.donations + result
                    )
                }
            }.onFailure { e ->
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
        donationUseCases.updateStatus(donationId, status).onSuccess { updated ->
            _uiState.update { state ->
                state.copy(
                    donations = state.donations.map { d -> if (d.donationId == updated.donationId) updated else d },
                    successMessage = "Status updated"
                )
            }
        }.onFailure { e ->
            _uiState.update { it.copy(errorMessage = e.message) }
        }
    }

    fun updateDonationVolume(donationId: String, volume: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        donationUseCases.updateDonationVolume(donationId, volume).onSuccess { updated ->
            _uiState.update { state ->
                state.copy(
                    donations = state.donations.map { d ->
                        if (d.donationId == updated.donationId) updated else d
                    },
                    successMessage = "Volume updated",
                    isLoading = false
                )
            }
            updateStatus(donationId = donationId, status = "DONATED")
        }.onFailure { e ->
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
        donationUseCases.deleteDonation(donationId).onSuccess {
            _uiState.update { state ->
                state.copy(
                    donations = state.donations.filterNot { it.donationId == donationId },
                    successMessage = "Donation deleted"
                )
            }
        }.onFailure { e ->
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
