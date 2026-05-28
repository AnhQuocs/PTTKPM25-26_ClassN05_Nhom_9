package com.example.heartbeat.domain.usecase.system.province

import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.repository.system.ProvinceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetProvinceByIdUseCaseTest {

    @Test
    fun `invoke should return province when found`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "01"
            val mockProvince = Province(provinceId, "Ha Noi")
            coEvery { repository.getProvinceById(provinceId) } returns mockProvince

            // Act
            val result = useCase.invoke(provinceId)

            // Assert
            Assert.assertNotNull(result)
            Assert.assertEquals(mockProvince, result)
            Assert.assertEquals("Ha Noi", result?.name)
        }
    }

    @Test
    fun `invoke should return null when province not found`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "invalid_id"
            coEvery { repository.getProvinceById(provinceId) } returns null

            // Act
            val result = useCase.invoke(provinceId)

            // Assert
            Assert.assertNull(result)
        }
    }

    @Test
    fun `invoke should pass correct ID to repository`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "02"
            coEvery { repository.getProvinceById(provinceId) } returns null

            // Act
            useCase.invoke(provinceId)

            // Assert
            coVerify(exactly = 1) { repository.getProvinceById(provinceId) }
        }
    }

    @Test
    fun `invoke should call repository getProvinceById exactly once`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "03"
            coEvery { repository.getProvinceById(provinceId) } returns null

            // Act
            useCase.invoke(provinceId)

            // Assert
            coVerify(exactly = 1) { repository.getProvinceById(provinceId) }
        }
    }

    @Test
    fun `invoke should handle exception from repository`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "error_id"
            val exception = Exception("Database error")
            coEvery { repository.getProvinceById(provinceId) } throws exception

            // Act & Assert
            try {
                useCase.invoke(provinceId)
                Assert.fail("Expected exception but none was thrown")
            } catch (e: Exception) {
                Assert.assertEquals("Database error", e.message)
            }
        }
    }

    @Test
    fun `invoke should return province with all correct properties`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "04"
            val mockProvince = Province(provinceId, "Can Tho")
            coEvery { repository.getProvinceById(provinceId) } returns mockProvince

            // Act
            val result = useCase.invoke(provinceId)

            // Assert
            Assert.assertNotNull(result)
            result?.let {
                Assert.assertEquals(provinceId, it.id)
                Assert.assertEquals("Can Tho", it.name)
            }
        }
    }

    @Test
    fun `invoke should handle different province IDs correctly`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val province1 = Province("01", "Ha Noi")
            val province2 = Province("02", "Ho Chi Minh")
            
            coEvery { repository.getProvinceById("01") } returns province1
            coEvery { repository.getProvinceById("02") } returns province2

            // Act
            val result1 = useCase.invoke("01")
            val result2 = useCase.invoke("02")

            // Assert
            Assert.assertEquals(province1, result1)
            Assert.assertEquals(province2, result2)
            Assert.assertEquals("Ha Noi", result1?.name)
            Assert.assertEquals("Ho Chi Minh", result2?.name)
        }
    }

    @Test
    fun `invoke should handle empty string ID`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = ""
            coEvery { repository.getProvinceById(provinceId) } returns null

            // Act
            val result = useCase.invoke(provinceId)

            // Assert
            Assert.assertNull(result)
            coVerify(exactly = 1) { repository.getProvinceById("") }
        }
    }

    @Test
    fun `invoke should not modify the returned province`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val provinceId = "05"
            val originalProvince = Province(provinceId, "Hai Phong")
            coEvery { repository.getProvinceById(provinceId) } returns originalProvince

            // Act
            val result = useCase.invoke(provinceId)

            // Assert
            Assert.assertEquals(originalProvince, result)
            Assert.assertSame(originalProvince, result)
        }
    }

    @Test
    fun `invoke should handle special characters in ID`() {
        runBlocking {
            // Arrange
            val repository = mockk<ProvinceRepository>()
            val useCase = GetProvinceByIdUseCase(repository)
            val specialId = "pr-001-special"
            val mockProvince = Province(specialId, "Special Province")
            coEvery { repository.getProvinceById(specialId) } returns mockProvince

            // Act
            val result = useCase.invoke(specialId)

            // Assert
            Assert.assertNotNull(result)
            Assert.assertEquals(specialId, result?.id)
            Assert.assertEquals("Special Province", result?.name)
        }
    }
}
