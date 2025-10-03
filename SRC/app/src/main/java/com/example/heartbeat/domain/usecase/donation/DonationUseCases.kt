package com.example.heartbeat.domain.usecase.donation

data class DonationUseCases(
    val addDonation: AddDonationUseCase,
    val getDonationById: GetDonationByIdUseCase,
    val getDonationsByDonor: GetDonationsByDonorUseCase,
    val getDonationsByEvent: GetDonationsByEventUseCase,
    val updateDonation: UpdateDonationUseCase,
    val updateStatus: UpdateStatusUseCase,
    val updateDonationVolume: UpdateDonationVolumeUseCase,
    val deleteDonation: DeleteDonationUseCase,
    val observePendingDonations: ObservePendingDonationsUseCase,
    val observeDonationsByEvent: ObserveDonationsByEventUseCase
)