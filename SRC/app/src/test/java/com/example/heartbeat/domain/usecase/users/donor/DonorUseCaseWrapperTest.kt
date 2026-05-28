package com.example.heartbeat.domain.usecase.users.donor

import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class DonorUseCaseWrapperTest {

    @Test
    fun `DonorUseCase wrapper should hold all use cases`() {
        val repository = mockk<DonorRepository>()
        val donorUseCases = DonorUseCase(
            addDonorUseCase = AddDonorUseCase(repository),
            getCurrentDonorUseCase = GetCurrentDonorUseCase(repository),
            getDonorByIdUseCase = GetDonorByIdUseCase(repository),
            updateDonorUseCase = UpdateDonorUseCase(repository),
            isDonorProfileExistUseCase = IsDonorProfileExistUseCase(repository),
            uploadAvatarUseCase = UploadAvatarUseCase(repository),
            updateAvatarUseCase = UpdateAvatarUseCase(repository),
            saveAvatarUrlUseCase = SaveAvatarUrlUseCase(repository),
            getAvatarUseCase = GetAvatarUseCase(repository)
        )

        assertNotNull(donorUseCases.addDonorUseCase)
        assertNotNull(donorUseCases.getCurrentDonorUseCase)
        assertNotNull(donorUseCases.updateDonorUseCase)
        assertNotNull(donorUseCases.uploadAvatarUseCase)
        assertNotNull(donorUseCases.getAvatarUseCase)
    }
}
