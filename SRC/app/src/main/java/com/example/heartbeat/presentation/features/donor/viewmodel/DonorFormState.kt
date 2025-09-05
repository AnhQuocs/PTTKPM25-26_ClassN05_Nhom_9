package com.example.heartbeat.presentation.features.donor.viewmodel

data class DonorFormState(
    val name: String = "",
    val phoneNumber: String = "",
    val bloodGroup: String = "",
    val city: String = "",
    val dateOfBirth: String = "",
    val age: Int = 0,
    val gender: String = "",
    val willingToDonate: Boolean = true,
    val about: String = "",
    val profileAvatar: String = "",

    // UI only
    val currentStep: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSubmitSuccess: Boolean = false
) {
    fun isStepOneValid() =
        name.isNotBlank() && phoneNumber.isNotBlank() && bloodGroup.isNotBlank() && city.isNotBlank()

    fun isStepTwoValid() =
        dateOfBirth.isNotBlank() && age > 18 && gender.isNotBlank()

    fun isStepThreeValid() =
        profileAvatar.isNotBlank()
}