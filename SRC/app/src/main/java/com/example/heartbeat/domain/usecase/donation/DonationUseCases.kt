package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.usecase.donation.create.AddDonationUseCase
import com.example.heartbeat.domain.usecase.donation.delete.DeleteDonationUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationByIdUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObservePendingDonationsUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationVolumeUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateStatusUseCase

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