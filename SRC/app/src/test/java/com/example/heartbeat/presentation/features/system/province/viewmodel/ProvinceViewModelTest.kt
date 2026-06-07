package com.example.heartbeat.presentation.features.system.province.viewmodel

import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.domain.usecase.system.province.GetAllProvincesUseCase
import com.example.heartbeat.domain.usecase.system.province.GetProvinceByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProvinceViewModelTest {

    private lateinit var viewModel: ProvinceViewModel
    private val getAllProvincesUseCase = mockk<GetAllProvincesUseCase>()
    private val getProvinceByIdUseCase = mockk<GetProvinceByIdUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    private val testProvinces = listOf(
        Province("P1", "Hà Nội"),
        Province("P2", "TP. Hồ Chí Minh")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getAllProvincesUseCase() } returns emptyList()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct before init completes`() = runTest {
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        
        assertTrue(viewModel.provinces.value.isEmpty())
        assertNull(viewModel.selectedProvince.value)
    }

    @Test
    fun `init block loads provinces successfully`() = runTest {
        coEvery { getAllProvincesUseCase() } returns testProvinces
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        advanceUntilIdle()
        
        assertEquals(testProvinces, viewModel.provinces.value)
    }

    @Test
    fun `init block handles empty list from repository`() = runTest {
        coEvery { getAllProvincesUseCase() } returns emptyList()
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        advanceUntilIdle()
        
        assertTrue(viewModel.provinces.value.isEmpty())
    }

    @Test
    fun `loadProvinceById updates selectedProvince on success`() = runTest {
        val province = testProvinces[0]
        coEvery { getProvinceByIdUseCase("P1") } returns province
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        viewModel.loadProvinceById("P1")
        advanceUntilIdle()
        
        assertEquals(province, viewModel.selectedProvince.value)
    }

    @Test
    fun `loadProvinceById sets selectedProvince to null if not found`() = runTest {
        coEvery { getProvinceByIdUseCase("UNKNOWN") } returns null
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        viewModel.loadProvinceById("UNKNOWN")
        advanceUntilIdle()
        
        assertNull(viewModel.selectedProvince.value)
    }

    @Test
    fun `getProvinceById direct call cache miss calls usecase`() = runTest {
        val province = testProvinces[1]
        coEvery { getProvinceByIdUseCase("P2") } returns province
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        
        val result = viewModel.getProvinceById("P2")
        
        assertEquals(province, result)
        coVerify(exactly = 1) { getProvinceByIdUseCase("P2") }
    }

    @Test
    fun `getProvinceById direct call cache hit does not call usecase`() = runTest {
        val province = testProvinces[1]
        coEvery { getProvinceByIdUseCase("P2") } returns province
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        
        viewModel.getProvinceById("P2")
        
        val result = viewModel.getProvinceById("P2")
        
        assertEquals(province, result)
        coVerify(exactly = 1) { getProvinceByIdUseCase("P2") }
    }

    @Test
    fun `getProvinceById returns null if usecase returns null`() = runTest {
        coEvery { getProvinceByIdUseCase("P3") } returns null
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        
        val result = viewModel.getProvinceById("P3")
        
        assertNull(result)
        coVerify(exactly = 1) { getProvinceByIdUseCase("P3") }
    }

    @Test
    fun `getProvinceById caches multiple different provinces correctly`() = runTest {
        val p1 = testProvinces[0]
        val p2 = testProvinces[1]
        coEvery { getProvinceByIdUseCase("P1") } returns p1
        coEvery { getProvinceByIdUseCase("P2") } returns p2
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        
        val res1 = viewModel.getProvinceById("P1")
        val res2 = viewModel.getProvinceById("P2")
        
        assertEquals(p1, res1)
        assertEquals(p2, res2)
        
        viewModel.getProvinceById("P1")
        viewModel.getProvinceById("P2")
        
        coVerify(exactly = 1) { getProvinceByIdUseCase("P1") }
        coVerify(exactly = 1) { getProvinceByIdUseCase("P2") }
    }

    @Test
    fun `loadProvinceById handles sequential updates to same state`() = runTest {
        val p1 = testProvinces[0]
        val p2 = testProvinces[1]
        coEvery { getProvinceByIdUseCase("P1") } returns p1
        coEvery { getProvinceByIdUseCase("P2") } returns p2
        
        viewModel = ProvinceViewModel(getAllProvincesUseCase, getProvinceByIdUseCase)
        
        viewModel.loadProvinceById("P1")
        advanceUntilIdle()
        assertEquals(p1, viewModel.selectedProvince.value)
        
        viewModel.loadProvinceById("P2")
        advanceUntilIdle()
        assertEquals(p2, viewModel.selectedProvince.value)
    }
}
