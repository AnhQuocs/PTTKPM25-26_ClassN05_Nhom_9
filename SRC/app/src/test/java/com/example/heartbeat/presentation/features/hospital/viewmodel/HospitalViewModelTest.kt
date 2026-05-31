package com.example.heartbeat.presentation.features.hospital.viewmodel

import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.usecase.hospital.GetAllHospitalsUseCase
import com.example.heartbeat.domain.usecase.hospital.GetHospitalByIdUseCase
import com.example.heartbeat.domain.usecase.hospital.HospitalUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HospitalViewModelTest {

    private lateinit var viewModel: HospitalViewModel
    private lateinit var hospitalUseCase: HospitalUseCase
    private val getAllHospitalsUseCase = mockk<GetAllHospitalsUseCase>()
    private val getHospitalByIdUseCase = mockk<GetHospitalByIdUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    private val testHospital = Hospital(
        hospitalId = "H1",
        hospitalName = "Hospital 1",
        imgUrl = "url",
        address = "addr",
        phone = "123",
        province = "prov",
        district = "dist"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        hospitalUseCase = HospitalUseCase(getAllHospitalsUseCase, getHospitalByIdUseCase)
        viewModel = HospitalViewModel(hospitalUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `VM_HS_01 initial state is correct`() {
        // Phủ getter mặc định của isLoading, hospitals và hospitalDetails
        assertNotNull(viewModel.isLoading)
        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.hospitals.isEmpty())
        assertTrue(viewModel.hospitalDetails.isEmpty())
    }

    @Test
    fun `VM_HS_02 loadHospitals loading state lifecycle`() = runTest {
        coEvery { getAllHospitalsUseCase() } coAnswers {
            delay(100)
            emptyList()
        }
        
        viewModel.loadHospitals()
        // Kiểm tra isLoading = true (Đồng bộ)
        assertTrue(viewModel.isLoading.value)
        
        advanceUntilIdle()
        // Kiểm tra isLoading = false (Sau finally)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `VM_HS_03 loadHospitals success updates hospitals list`() = runTest {
        val list = listOf(testHospital)
        coEvery { getAllHospitalsUseCase() } returns list
        
        viewModel.loadHospitals()
        advanceUntilIdle()
        
        // Phủ setter và getter của hospitals
        assertEquals(list, viewModel.hospitals)
    }

    @Test
    fun `VM_HS_04 loadHospitals handles empty list result`() = runTest {
        coEvery { getAllHospitalsUseCase() } returns emptyList()
        
        viewModel.loadHospitals()
        advanceUntilIdle()
        
        assertTrue(viewModel.hospitals.isEmpty())
    }

    @Test
    fun `VM_HS_05 loadHospitalById updates details map for new ID`() = runTest {
        coEvery { getHospitalByIdUseCase("H1") } returns testHospital
        
        viewModel.loadHospitalById("H1")
        advanceUntilIdle()
        
        // Phủ setter và getter của hospitalDetails
        assertEquals(testHospital, viewModel.hospitalDetails["H1"])
    }

    @Test
    fun `VM_HS_06 loadHospitalById cache logic`() = runTest {
        coEvery { getHospitalByIdUseCase("H1") } returns testHospital
        
        viewModel.loadHospitalById("H1")
        advanceUntilIdle()
        
        // Nhánh containsKey == true -> return sớm
        viewModel.loadHospitalById("H1")
        advanceUntilIdle()
        
        coVerify(exactly = 1) { getHospitalByIdUseCase("H1") }
    }

    @Test
    fun `VM_HS_07 loadHospitalById handles null result correctly`() = runTest {
        coEvery { getHospitalByIdUseCase("NOT_FOUND") } returns null
        
        viewModel.loadHospitalById("NOT_FOUND")
        advanceUntilIdle()
        
        assertFalse(viewModel.hospitalDetails.containsKey("NOT_FOUND"))
    }

    @Test
    fun `VM_HS_08 loadHospitalById handles blank ID correctly`() = runTest {
        coEvery { getHospitalByIdUseCase("") } returns null
        
        viewModel.loadHospitalById("")
        advanceUntilIdle()
        
        assertTrue(viewModel.hospitalDetails.isEmpty())
    }

    @Test
    fun `VM_HS_09 loadHospitalById adds multiple hospitals cumulatively`() = runTest {
        val h1 = testHospital.copy(hospitalId = "H1")
        val h2 = testHospital.copy(hospitalId = "H2")
        coEvery { getHospitalByIdUseCase("H1") } returns h1
        coEvery { getHospitalByIdUseCase("H2") } returns h2
        
        viewModel.loadHospitalById("H1")
        viewModel.loadHospitalById("H2")
        advanceUntilIdle()
        
        assertEquals(2, viewModel.hospitalDetails.size)
    }

    @Test
    fun `VM_HS_10 loadHospitalById null safety check`() = runTest {
        coEvery { getHospitalByIdUseCase(any()) } returns null
        
        viewModel.loadHospitalById("ANY")
        advanceUntilIdle()
        
        // Nhánh let block không chạy
        assertTrue(viewModel.hospitalDetails.isEmpty())
    }

    @Test
    fun `VM_HS_11 loadHospitals should set loading state synchronously`() = runTest {
        coEvery { getAllHospitalsUseCase() } returns emptyList()
        
        viewModel.loadHospitals()
        // Khẳng định tính đồng bộ sau khi đã fix source code
        assertTrue("isLoading must be true synchronously", viewModel.isLoading.value)
    }

    // --- CÁC KỊCH BẢN BỔ SUNG ĐỂ ĐẠT 100% METHOD COVERAGE ---

    @Test
    fun `hospitalUseCase is wired correctly via dependency injection`() = runTest {
        coEvery { getAllHospitalsUseCase() } returns listOf(testHospital)

        viewModel.loadHospitals()
        advanceUntilIdle()

        coVerify(exactly = 1) { getAllHospitalsUseCase() }
        assertEquals(listOf(testHospital), viewModel.hospitals)
    }

    @Test
    fun `loadHospitals finally block coverage on error`() = runTest {
        // Phủ khối catch và finally khi UseCase ném lỗi
        coEvery { getAllHospitalsUseCase() } coAnswers {
            throw RuntimeException("Expected error for coverage")
        }
        
        viewModel.loadHospitals()
        advanceUntilIdle()
        
        assertFalse("isLoading should be false even after error", viewModel.isLoading.value)
    }

    @Test
    fun `loadHospitalById catch block coverage on error`() = runTest {
        // Phủ khối catch của loadHospitalById
        coEvery { getHospitalByIdUseCase(any()) } coAnswers {
            throw RuntimeException("Expected error for coverage")
        }
        
        viewModel.loadHospitalById("ERR_ID")
        advanceUntilIdle()
        
        assertTrue(viewModel.hospitalDetails.isEmpty())
    }
}
