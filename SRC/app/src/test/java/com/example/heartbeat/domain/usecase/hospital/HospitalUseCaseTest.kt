package com.example.heartbeat.domain.usecase.hospital

import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HospitalUseCaseTest {
    private lateinit var repository: HospitalRepository
    private lateinit var getAllHospitalsUseCase: GetAllHospitalsUseCase
    private lateinit var getHospitalByIdUseCase: GetHospitalByIdUseCase
    private lateinit var hospitalUseCase: HospitalUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getAllHospitalsUseCase = GetAllHospitalsUseCase(repository)
        getHospitalByIdUseCase = GetHospitalByIdUseCase(repository)
        hospitalUseCase = HospitalUseCase(
            getAllHospitalsUseCase = getAllHospitalsUseCase,
            getHospitalByIdUseCase = getHospitalByIdUseCase
        )
    }

    @Test
    fun `HospitalUseCase should initialize with both usecases`() {
        // Arrange & Act
        val useCase = HospitalUseCase(
            getAllHospitalsUseCase = getAllHospitalsUseCase,
            getHospitalByIdUseCase = getHospitalByIdUseCase
        )

        // Assert
        Assert.assertNotNull(useCase)
    }

    @Test
    fun `HospitalUseCase should hold getAllHospitalsUseCase`() {
        // Assert
        Assert.assertNotNull(hospitalUseCase.getAllHospitalsUseCase)
        Assert.assertSame(getAllHospitalsUseCase, hospitalUseCase.getAllHospitalsUseCase)
    }

    @Test
    fun `HospitalUseCase should hold getHospitalByIdUseCase`() {
        // Assert
        Assert.assertNotNull(hospitalUseCase.getHospitalByIdUseCase)
        Assert.assertSame(getHospitalByIdUseCase, hospitalUseCase.getHospitalByIdUseCase)
    }

    @Test
    fun `HospitalUseCase should make both usecases accessible`() {
        // Assert
        Assert.assertNotNull(hospitalUseCase.getAllHospitalsUseCase)
        Assert.assertNotNull(hospitalUseCase.getHospitalByIdUseCase)
    }

    @Test
    fun `HospitalUseCase usecases should be independently functional`() {
        // Act
        val getAllHospitals = hospitalUseCase.getAllHospitalsUseCase
        val getHospitalById = hospitalUseCase.getHospitalByIdUseCase

        // Assert
        Assert.assertNotNull(getAllHospitals)
        Assert.assertNotNull(getHospitalById)
        Assert.assertNotEquals(getAllHospitals, getHospitalById)
    }

    @Test
    fun `HospitalUseCase should maintain usecase references`() {
        // Act
        val firstAccess = hospitalUseCase.getAllHospitalsUseCase
        val secondAccess = hospitalUseCase.getAllHospitalsUseCase

        // Assert
        Assert.assertSame(firstAccess, secondAccess)
    }
}
