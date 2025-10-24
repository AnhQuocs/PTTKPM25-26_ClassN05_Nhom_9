package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.usecase.donation.create.AddDonationUseCase
import com.example.heartbeat.domain.usecase.donation.delete.DeleteDonationUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetAllDonationsListUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetAllDonationsUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByDayUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByMonthUseCase
import com.example.heartbeat.domain.usecase.donation.read.GetDonationsByWeekUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationByDonorUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObserveDonationsByEventUseCase
import com.example.heartbeat.domain.usecase.donation.read.ObservePendingDonationsUseCase
import com.example.heartbeat.domain.usecase.donation.update.ApproveDonationUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationVolumeUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateStatusUseCase

data class DonationUseCases(
    val addDonation: AddDonationUseCase,
    val getDonationsByDonor: GetDonationsByDonorUseCase,
    val updateStatus: UpdateStatusUseCase,
    val updateDonationVolume: UpdateDonationVolumeUseCase,
    val deleteDonation: DeleteDonationUseCase,
    val observePendingDonations: ObservePendingDonationsUseCase,
    val observeDonationsByEvent: ObserveDonationsByEventUseCase,
    val observeDonationByDonorUseCase: ObserveDonationByDonorUseCase,
    val getDonationsByDayUseCase: GetDonationsByDayUseCase,
    val getDonationsByWeekUseCase: GetDonationsByWeekUseCase,
    val getDonationsByMonthUseCase: GetDonationsByMonthUseCase,
    val getAllDonationsUseCase: GetAllDonationsUseCase,
    val getAllDonationsListUseCase: GetAllDonationsListUseCase,
    val approveDonationUseCase: ApproveDonationUseCase
)