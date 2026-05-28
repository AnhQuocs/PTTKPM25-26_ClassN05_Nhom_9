package com.example.heartbeat.domain.usecase.users.donor.update

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import com.example.heartbeat.domain.usecase.users.donor.UpdateDonorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateDonorUseCaseTest {

    private lateinit var repository: DonorRepository
    private lateinit var updateDonorUseCase: UpdateDonorUseCase

    private val testDonor = Donor(
        donorId = "d1",
        name = "Test Donor",
        phoneNumber = "0123456789",
        bloodGroup = "A+",
        cityId = "city1",
        dateOfBirth = "2000-01-01",
        age = 24,
        gender = "Male",
        willingToDonate = true,
        about = "I want to help"
    )

    @Before
    fun setUp() {
        repository = mockk()
        updateDonorUseCase = UpdateDonorUseCase(repository)
    }

    @Test
    fun `UpdateDonor success should call repository`() = runBlocking {
        coEvery { repository.updateDonor(any(), any()) } returns Unit
        updateDonorUseCase("d1", testDonor)
        coVerify(exactly = 1) { repository.updateDonor("d1", testDonor) }
    }

    @Test(expected = Exception::class)
    fun `UpdateDonor failure should throw exception`() = runBlocking {
        coEvery { repository.updateDonor(any(), any()) } throws Exception("Update failed")
        updateDonorUseCase("d1", testDonor)
    }
}
