package com.example.heartbeat.domain.usecase.hospital

import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetHospitalByIdUseCaseTest {

    private lateinit var repository: HospitalRepository
    private lateinit var useCase: GetHospitalByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetHospitalByIdUseCase(repository)
    }

    @Test
    fun `invoke should return hospital when found`() = runBlocking {
        val hospitalId = "h_01"
        val mockHospital = Hospital(hospitalId, "City Clinic", "url", "Addr", "123", "P1", "D1")
        coEvery { repository.getHospitalById(hospitalId) } returns mockHospital

        val result = useCase.invoke(hospitalId)

        Assert.assertNotNull(result)
        Assert.assertEquals("City Clinic", result?.hospitalName)
    }

    @Test
    fun `invoke should return null when ID not found`() = runBlocking {
        coEvery { repository.getHospitalById(any()) } returns null
        Assert.assertNull(useCase.invoke("not_found"))
    }

    @Test
    fun `invoke should call repository with correct ID`() = runBlocking {
        coEvery { repository.getHospitalById("test_id") } returns null
        useCase.invoke("test_id")
        coVerify(exactly = 1) { repository.getHospitalById("test_id") }
    }

    @Test
    fun `invoke with blank ID should return null and not crash`() = runBlocking {
        val result = useCase.invoke("")
        Assert.assertNull(result)
    }

    @Test
    fun `invoke should handle repository exceptions`() = runBlocking {
        coEvery { repository.getHospitalById(any()) } throws Exception("Data error")
        try {
            useCase.invoke("id")
            Assert.fail("Should throw exception")
        } catch (e: Exception) {
            Assert.assertEquals("Data error", e.message)
        }
    }

    @Test
    fun `invoke should return identical hospital object`() = runBlocking {
        val mock = mockk<Hospital>()
        coEvery { repository.getHospitalById("id") } returns mock
        Assert.assertSame(mock, useCase.invoke("id"))
    }

    @Test
    fun `invoke should return correct properties of hospital`() = runBlocking {
        val mock = Hospital("1", "H1", "U1", "A1", "P1", "PR1", "D1")
        coEvery { repository.getHospitalById("1") } returns mock
        val res = useCase.invoke("1")
        Assert.assertEquals("A1", res?.address)
        Assert.assertEquals("P1", res?.phone)
    }

    @Test
    fun `invoke should handle multiple requests independently`() = runBlocking {
        coEvery { repository.getHospitalById("1") } returns mockk()
        coEvery { repository.getHospitalById("2") } returns mockk()
        useCase.invoke("1")
        useCase.invoke("2")
        coVerify(exactly = 1) { repository.getHospitalById("1") }
        coVerify(exactly = 1) { repository.getHospitalById("2") }
    }

    @Test
    fun `invoke should not call repository multiple times for single call`() = runBlocking {
        coEvery { repository.getHospitalById(any()) } returns null
        useCase.invoke("id")
        coVerify(exactly = 1) { repository.getHospitalById("id") }
    }
}
