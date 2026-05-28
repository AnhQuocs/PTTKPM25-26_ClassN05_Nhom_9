package com.example.heartbeat.domain.usecase.users.donor

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.entity.users.DonorAvatar
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class DonorUseCaseTest {
    private lateinit var repository: DonorRepository

    @Before
    fun setUp() {
        repository = mockk()
    }

    private fun createDummyDonor(id: String = "d1", name: String = "John Doe") = Donor(
        donorId = id,
        name = name,
        phoneNumber = "0123456789",
        bloodGroup = "O+",
        cityId = "c1",
        dateOfBirth = "1990-01-01",
        age = 33,
        gender = "Male",
        willingToDonate = true,
        about = "Healthy"
    )

    @Test
    fun `AddDonor should call repository`() = runBlocking {
        val donor = createDummyDonor()
        coEvery { repository.addDonor(any()) } returns Unit
        
        AddDonorUseCase(repository).invoke(donor)
        
        coVerify(exactly = 1) { repository.addDonor(donor) }
    }

    @Test
    fun `GetCurrentDonor should return donor from repo`() = runBlocking {
        val donor = createDummyDonor()
        coEvery { repository.getCurrentDonor("d1") } returns donor
        
        val result = GetCurrentDonorUseCase(repository).invoke("d1")
        assertEquals(donor, result)
    }

    @Test
    fun `IsDonorProfileExist should return boolean`() = runBlocking {
        coEvery { repository.isDonorProfileExist("u1") } returns true
        val result = IsDonorProfileExistUseCase(repository).invoke("u1")
        assertEquals(true, result)
    }

    @Test
    fun `DonorAvatarUseCase should upload and return url`() = runBlocking {
        coEvery { repository.uploadAvatarBase64("d1", "data") } returns "url_new"
        val result = DonorAvatarUseCase(repository).uploadAvatar("d1", "data")
        assertEquals("url_new", result)
    }

    @Test
    fun `DonorUseCase wrapper should initialize correctly`() {
        val donorUseCase = DonorUseCase(
            addDonorUseCase = AddDonorUseCase(repository),
            donorAvatarUseCase = DonorAvatarUseCase(repository),
            updateDonorUseCase = UpdateDonorUseCase(repository),
            getDonorByIdUseCase = GetDonorByIdUseCase(repository),
            getCurrentDonorUseCase = GetCurrentDonorUseCase(repository),
            isDonorProfileExistUseCase = IsDonorProfileExistUseCase(repository)
        )
        assertNotNull(donorUseCase.addDonorUseCase)
    }
}
