package com.example.heartbeat.presentation.features.donation.util

object DonationValidator {
    fun validateCitizenIdLength(citizenId: String): Boolean {
        return citizenId.length == 12 &&
                !citizenId.contains("\\s".toRegex())
    }
}