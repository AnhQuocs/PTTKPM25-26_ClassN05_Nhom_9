package com.example.heartbeat.domain.usecase.system.province

import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.repository.system.ProvinceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetAllProvincesUseCaseTest {

    @Test
    fun `invoke should return non-empty list of provinces`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        val mockProvinces = listOf(
            Province("01", "Ha Noi"),
            Province("02", "Ho Chi Minh"),
            Province("03", "Da Nang")
        )
        coEvery { repository.getAllProvinces() } returns mockProvinces

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(mockProvinces, result)
        Assert.assertEquals("Ha Noi", result[0].name)
        Assert.assertEquals("Ho Chi Minh", result[1].name)
    }

    @Test
    fun `invoke should return empty list when no provinces available`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        coEvery { repository.getAllProvinces() } returns emptyList()

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertTrue(result.isEmpty())
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `invoke should call repository getAllProvinces exactly once`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        coEvery { repository.getAllProvinces() } returns emptyList()

        // Act
        useCase.invoke()

        // Assert
        coVerify(exactly = 1) { repository.getAllProvinces() }
    }

    @Test
    fun `invoke should handle exception from repository`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        val exception = Exception("Network error")
        coEvery { repository.getAllProvinces() } throws exception

        // Act & Assert
        try {
            useCase.invoke()
            Assert.fail("Expected exception but none was thrown")
        } catch (e: Exception) {
            Assert.assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `invoke should return provinces with all correct properties`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        val mockProvinces = listOf(
            Province("01", "Ha Noi"),
            Province("02", "Ho Chi Minh")
        )
        coEvery { repository.getAllProvinces() } returns mockProvinces

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertEquals(2, result.size)
        result.forEach { province ->
            Assert.assertNotNull(province.id)
            Assert.assertNotNull(province.name)
            Assert.assertTrue(province.id.isNotBlank())
            Assert.assertTrue(province.name.isNotBlank())
        }
    }

    @Test
    fun `invoke should return multiple calls independently`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        val firstCall = listOf(Province("01", "Ha Noi"))
        val secondCall = listOf(
            Province("01", "Ha Noi"),
            Province("02", "Ho Chi Minh"),
            Province("03", "Da Nang")
        )
        coEvery { repository.getAllProvinces() } returns firstCall andThen secondCall

        // Act
        val result1 = useCase.invoke()
        val result2 = useCase.invoke()

        // Assert
        Assert.assertEquals(1, result1.size)
        Assert.assertEquals(3, result2.size)
    }

    @Test
    fun `invoke should handle large dataset of provinces`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        val largeProvincesList = (1..100).map { i ->
            Province("$i", "Province $i")
        }
        coEvery { repository.getAllProvinces() } returns largeProvincesList

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertEquals(100, result.size)
        Assert.assertEquals("Province 1", result[0].name)
        Assert.assertEquals("Province 100", result[99].name)
    }

    @Test
    fun `invoke should maintain order from repository`() = runBlocking {
        // Arrange
        val repository = mockk<ProvinceRepository>()
        val useCase = GetAllProvincesUseCase(repository)
        val orderedProvinces = listOf(
            Province("01", "Ha Noi"),
            Province("02", "Ho Chi Minh"),
            Province("03", "Da Nang"),
            Province("04", "Can Tho"),
            Province("05", "Hai Phong")
        )
        coEvery { repository.getAllProvinces() } returns orderedProvinces

        // Act
        val result = useCase.invoke()

        // Assert
        Assert.assertEquals(orderedProvinces, result)
        for (i in result.indices) {
            Assert.assertEquals(orderedProvinces[i], result[i])
        }
    }
}
