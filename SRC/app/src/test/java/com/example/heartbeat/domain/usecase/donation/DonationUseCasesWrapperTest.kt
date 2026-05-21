package com.example.heartbeat.domain.usecase.donation

import com.example.heartbeat.domain.repository.donation.DonationRepository
import com.example.heartbeat.domain.usecase.donation.create.AddDonationUseCase
import com.example.heartbeat.domain.usecase.donation.delete.DeleteDonationUseCase
import com.example.heartbeat.domain.usecase.donation.read.*
import com.example.heartbeat.domain.usecase.donation.update.ApproveDonationUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateDonationVolumeUseCase
import com.example.heartbeat.domain.usecase.donation.update.UpdateStatusUseCase
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class DonationUseCasesWrapperTest {

    @Test
    fun `DonationUseCases wrapper should hold all use cases`() {
        val repository = mockk<DonationRepository>()
        val donationUseCases = DonationUseCases(
            addDonation = AddDonationUseCase(repository),
            getDonationsByDonor = GetDonationsByDonorUseCase(repository),
            updateStatus = UpdateStatusUseCase(repository),
            updateDonationVolume = UpdateDonationVolumeUseCase(repository),
            deleteDonation = DeleteDonationUseCase(repository),
            observePendingDonations = ObservePendingDonationsUseCase(repository),
            observeDonationsByEvent = ObserveDonationsByEventUseCase(repository),
            observeDonationByDonorUseCase = ObserveDonationByDonorUseCase(repository),
            getDonationsByDayUseCase = GetDonationsByDayUseCase(repository),
            getDonationsByWeekUseCase = GetDonationsByWeekUseCase(repository),
            getDonationsByMonthUseCase = GetDonationsByMonthUseCase(repository),
            getAllDonationsUseCase = GetAllDonationsUseCase(repository),
            getAllDonationsListUseCase = GetAllDonationsListUseCase(repository),
            approveDonationUseCase = ApproveDonationUseCase(repository)
        )
        
        assertNotNull(donationUseCases.addDonation)
        assertNotNull(donationUseCases.deleteDonation)
        assertNotNull(donationUseCases.updateStatus)
        assertNotNull(donationUseCases.getAllDonationsUseCase)
        assertNotNull(donationUseCases.approveDonationUseCase)
    }
}
