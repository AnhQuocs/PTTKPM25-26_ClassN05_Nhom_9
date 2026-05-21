package com.example.heartbeat.domain.usecase.donation

sealed class DonationException : Exception() {
    object InvalidVolume : DonationException()
    object EmptyDonorId : DonationException()
    object EmptyEventId : DonationException()
    object InvalidStatus : DonationException()
    object EmptyDonationId : DonationException()
}
