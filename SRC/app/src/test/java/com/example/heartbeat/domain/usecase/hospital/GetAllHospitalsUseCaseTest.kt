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

class GetAllHospitalsUseCaseTest {
    private lateinit var repository: HospitalRepository
    private lateinit var useCase: GetAllHospitalsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAllHospitalsUseCase(repository)
    }

    @Test
    fun `invoke should return non-empty list of hospitals`() = runBlocking {
        // Arrange
        val mockHospitals = listOf(
            Hospital(
                hospitalId = "1",
                hospitalName = "Hospital A",
                imgUrl = "https://example.com/hospital1.jpg",
                address = "123 Main St",
                phone = "123-456-7890",
                province = "Province A",
                district = "District A"
            ),
            Hospital(
                hospitalId = "2",
                hospitalName = "Hospital B",
                imgUrl = "https://example.com/hospital2.jpg",
                address = "456 Oak Ave",
                phone = "987-654-3210",
                province = "Province B",
                district = "District B"
            )
        )
        coEvery { repository.getAllHospitals() } returns mockHospitals

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(mockHospitals, result)
        Assert.assertEquals("Hospital A", result[0].hospitalName)
        Assert.assertEquals("Hospital B", result[1].hospitalName)
    }

    @Test
    fun `invoke should return empty list when no hospitals available`() = runBlocking {
        // Arrange
        coEvery { repository.getAllHospitals() } returns emptyList()

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertTrue(result.isEmpty())
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `invoke should call repository getAllHospitals exactly once`() = runBlocking {
        // Arrange
        coEvery { repository.getAllHospitals() } returns emptyList()

        // Act
        useCase.invoke()

        // Assert
        coVerify(exactly = 1) { repository.getAllHospitals() }
    }

    @Test
    fun `invoke should handle exception from repository`() = runBlocking {
        // Arrange
        val exception = Exception("Network error")
        coEvery { repository.getAllHospitals() } throws exception

        // Act & Assert
        try {
            useCase.invoke()
            Assert.fail("Expected exception but none was thrown")
        } catch (e: Exception) {
            Assert.assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `invoke should return hospitals with all correct properties`() = runBlocking {
        // Arrange
        val mockHospital = Hospital(
            hospitalId = "123",
            hospitalName = "Test Hospital",
            imgUrl = "https://test.com/hospital.jpg",
            address = "789 Test Rd",
            phone = "555-1234",
            province = "Test Province",
            district = "Test District"
        )
        coEvery { repository.getAllHospitals() } returns listOf(mockHospital)

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertEquals(1, result.size)
        val hospital = result[0]
        Assert.assertEquals("123", hospital.hospitalId)
        Assert.assertEquals("Test Hospital", hospital.hospitalName)
        Assert.assertEquals("https://test.com/hospital.jpg", hospital.imgUrl)
        Assert.assertEquals("789 Test Rd", hospital.address)
        Assert.assertEquals("555-1234", hospital.phone)
        Assert.assertEquals("Test Province", hospital.province)
        Assert.assertEquals("Test District", hospital.district)
    }

    @Test
    fun `invoke should return multiple calls independently`() = runBlocking {
        // Arrange
        val firstCall = listOf(
            Hospital("1", "Hospital A", "url1", "addr1", "phone1", "prov1", "dist1")
        )
        val secondCall = listOf(
            Hospital("1", "Hospital A", "url1", "addr1", "phone1", "prov1", "dist1"),
            Hospital("2", "Hospital B", "url2", "addr2", "phone2", "prov2", "dist2")
        )
        coEvery { repository.getAllHospitals() } returns firstCall andThen secondCall

        // Act
        val result1 = useCase.invoke()
        val result2 = useCase.invoke()

        // Assert
        Assert.assertEquals(1, result1.size)
        Assert.assertEquals(2, result2.size)
    }
}
