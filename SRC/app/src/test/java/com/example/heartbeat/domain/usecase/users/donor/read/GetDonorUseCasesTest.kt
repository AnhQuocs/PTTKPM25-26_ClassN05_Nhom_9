package com.example.heartbeat.domain.usecase.users.donor.read

import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import com.example.heartbeat.domain.usecase.users.donor.GetCurrentDonorUseCase
import com.example.heartbeat.domain.usecase.users.donor.GetDonorByIdUseCase
import com.example.heartbeat.domain.usecase.users.donor.IsDonorProfileExistUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetDonorUseCasesTest {

    private lateinit var repository: DonorRepository
    private lateinit var getCurrentDonorUseCase: GetCurrentDonorUseCase
    private lateinit var getDonorByIdUseCase: GetDonorByIdUseCase
    private lateinit var isDonorProfileExistUseCase: IsDonorProfileExistUseCase

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
        getCurrentDonorUseCase = GetCurrentDonorUseCase(repository)
        getDonorByIdUseCase = GetDonorByIdUseCase(repository)
        isDonorProfileExistUseCase = IsDonorProfileExistUseCase(repository)
    }

    // --- GetCurrentDonorUseCase ---
    @Test
    fun `GetCurrentDonor found should return donor`() = runBlocking {
        coEvery { repository.getCurrentDonor("d1") } returns testDonor
        val result = getCurrentDonorUseCase("d1")
        assertEquals(testDonor, result)
    }

    @Test
    fun `GetCurrentDonor not found should return null`() = runBlocking {
        coEvery { repository.getCurrentDonor("d2") } returns null
        val result = getCurrentDonorUseCase("d2")
        assertNull(result)
    }

    @Test(expected = Exception::class)
    fun `GetCurrentDonor error should throw exception`() {
        runBlocking {
            coEvery { repository.getCurrentDonor(any()) } throws Exception("Network Error")
            getCurrentDonorUseCase("d1")
        }
    }

    // --- GetDonorByIdUseCase ---
    @Test
    fun `GetDonorById found should return donor`() = runBlocking {
        coEvery { repository.getDonorById("d1") } returns testDonor
        val result = getDonorByIdUseCase("d1")
        assertEquals(testDonor, result)
    }

    @Test
    fun `GetDonorById not found should return null`() = runBlocking {
        coEvery { repository.getDonorById("d2") } returns null
        val result = getDonorByIdUseCase("d2")
        assertNull(result)
    }

    @Test(expected = Exception::class)
    fun `GetDonorById error should throw exception`() {
        runBlocking {
            coEvery { repository.getDonorById(any()) } throws Exception("Server Error")
            getDonorByIdUseCase("d1")
        }
    }

    // --- IsDonorProfileExistUseCase ---
    @Test
    fun `IsDonorProfileExist when exists should return true`() = runBlocking {
        coEvery { repository.isDonorProfileExist("u1") } returns true
        val result = isDonorProfileExistUseCase("u1")
        assertTrue(result)
    }

    @Test
    fun `IsDonorProfileExist when not exists should return false`() = runBlocking {
        coEvery { repository.isDonorProfileExist("u2") } returns false
        val result = isDonorProfileExistUseCase("u2")
        assertFalse(result)
    }

    @Test(expected = Exception::class)
    fun `IsDonorProfileExist error should throw exception`() {
        runBlocking {
            coEvery { repository.isDonorProfileExist(any()) } throws Exception("Error")
            isDonorProfileExistUseCase("u1")
        }
    }
}
