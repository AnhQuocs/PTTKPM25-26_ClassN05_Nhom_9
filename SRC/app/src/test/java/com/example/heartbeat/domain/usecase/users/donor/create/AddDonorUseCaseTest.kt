package com.example.heartbeat.domain.usecase.users.donor.create

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import com.example.heartbeat.domain.usecase.users.donor.AddDonorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddDonorUseCaseTest {

    private lateinit var repository: DonorRepository
    private lateinit var addDonorUseCase: AddDonorUseCase

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
        addDonorUseCase = AddDonorUseCase(repository)
    }

    @Test
    fun `AddDonor success should call repository`() = runBlocking {
        coEvery { repository.addDonor(any()) } returns Unit
        addDonorUseCase(testDonor)
        coVerify(exactly = 1) { repository.addDonor(testDonor) }
    }

    @Test(expected = Exception::class)
    fun `AddDonor failure should throw exception`() = runBlocking {
        coEvery { repository.addDonor(any()) } throws Exception("DB Error")
        addDonorUseCase(testDonor)
    }
}
