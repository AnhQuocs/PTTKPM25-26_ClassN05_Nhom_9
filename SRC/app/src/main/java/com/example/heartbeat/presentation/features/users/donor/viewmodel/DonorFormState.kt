package com.example.heartbeat.presentation.features.users.donor.viewmodel

data class DonorFormState(
    val name: String = "",
    val phoneNumber: String = "",
    val bloodGroup: String = "",
    val cityId: String = "",
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
    fun isStepOneValid(provinceIds: List<String>, bloodList: List<String>): Boolean =
        name.isNotBlank() &&
                phoneNumber.isValidPhone() &&
                bloodList.contains(bloodGroup) &&
                cityId.isNotBlank() &&
                provinceIds.contains(cityId)

    fun isStepTwoValid(genderList: List<String>) =
        dateOfBirth.isNotBlank()
                && age > 18
                && genderList.contains(gender)

    fun isStepThreeValid() =
        profileAvatar.isNotBlank()
}

private fun String.isValidPhone(): Boolean {
    val regex = Regex("^0\\d{9}$")
    return matches(regex)
}