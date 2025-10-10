package com.example.heartbeat.domain.usecase.donation

data class DonationUseCases(
    val addDonation: AddDonationUseCase,
    val observeDonationById: ObserveDonationByIdUseCase,
    val getDonationsByDonor: GetDonationsByDonorUseCase,
    val getDonationsByEvent: GetDonationsByEventUseCase,
    val updateDonation: UpdateDonationUseCase,
    val updateStatus: UpdateStatusUseCase,
    val updateDonationVolume: UpdateDonationVolumeUseCase,
    val deleteDonation: DeleteDonationUseCase,
    val observePendingDonations: ObservePendingDonationsUseCase,
    val observeDonationsByEvent: ObserveDonationsByEventUseCase,
    val observeDonationByDonorUseCase: ObserveDonationByDonorUseCase
)