package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.domain.repository.event.EventRepository
import com.example.heartbeat.domain.repository.hospital.HospitalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchEventsUseCaseTest {

    private lateinit var eventRepository: EventRepository
    private lateinit var hospitalRepository: HospitalRepository
    private lateinit var searchEventsUseCase: SearchEventsUseCase

    private val now = LocalDateTime(2024, 1, 1, 0, 0)

    private val hospital1 = Hospital(
        hospitalId   = "h1",
        hospitalName = "Central Hospital",
        imgUrl       = "",
        address      = "Addr 1",
        phone        = "123",
        province     = "Hanoi",
        district     = "Ba Dinh"
    )

    private val event1 = Event("e1", "h1", "Big Donation", "Desc", "2024-02-01", "08:00", null, emptyList(), 100, 0, now)
    private val event2 = Event("e2", "h1", "Urgent", "Desc", "2024-02-05", "09:00", null, emptyList(), 50, 0, now)
    private val event3 = Event("e3", "h1", "Blood", "Desc", "2024-03-01", "10:00", null, emptyList(), 200, 0, now)
    private val event4 = Event("e4", "unknown", "Orphan Event", "Desc", "2024-04-01", "11:00", null, emptyList(), 10, 0, now)

    @Before
    fun setUp() {
        eventRepository = mockk()
        hospitalRepository = mockk()
        searchEventsUseCase = SearchEventsUseCase(eventRepository, hospitalRepository)

        coEvery { hospitalRepository.getAllHospitals() } returns listOf(hospital1)
        coEvery { eventRepository.getAllEvents() } returns listOf(event1, event2, event3, event4)
    }

    @Test
    fun `Match Event Name branch`() = runBlocking {
        val result = searchEventsUseCase("Big")
        assertEquals(1, result.size)
        assertEquals("e1", result[0].id)
    }

    @Test
    fun `Match Hospital Name branch`() = runBlocking {
        // e2 không khớp name nhưng khớp hospitalName (Central)
        val result = searchEventsUseCase("Central")
        assertTrue(result.any { it.id == "e2" })
    }

    @Test
    fun `Match Province branch`() = runBlocking {
        // e3 không khớp name/hospitalName nhưng khớp province (Hanoi)
        val result = searchEventsUseCase("Hanoi")
        assertTrue(result.any { it.id == "e3" })
    }

    @Test
    fun `Search No Match branch`() = runBlocking {
        assertTrue(searchEventsUseCase("Saigon").isEmpty())
    }

    @Test
    fun `Multiple tokens match different fields`() = runBlocking {
        // "Big" khớp name, "Hanoi" khớp province
        val result = searchEventsUseCase("Big Hanoi")
        assertEquals(1, result.size)
        assertEquals("e1", result[0].id)
    }

    @Test
    fun `Multiple tokens - First fails`() = runBlocking {
        assertTrue(searchEventsUseCase("Unknown Big").isEmpty())
    }

    @Test
    fun `Multiple tokens - First matches, second fails`() = runBlocking {
        // Nhánh này đảm bảo coverage cho việc all() trả về false sau khi đã có token đúng
        assertTrue(searchEventsUseCase("Big Unknown").isEmpty())
    }

    @Test
    fun `Hospital Null branch with Name match`() = runBlocking {
        // hospital null (event4), nhưng khớp qua name "Orphan" -> Phủ nhánh Elvis
        assertEquals(1, searchEventsUseCase("Orphan").size)
    }

    @Test
    fun `Hospital Null branch with No match`() = runBlocking {
        // hospital null, name sai -> các vế sau lấy "" và trả về false
        val result = searchEventsUseCase("Saigon")
        assertTrue(result.none { it.id == "e4" })
    }

    @Test
    fun `Handle messy spaces`() = runBlocking {
        // split sinh ra token rỗng, filter { it.isNotEmpty() } loại bỏ -> Phủ nhánh filter
        assertEquals(1, searchEventsUseCase("   Big    Donation   ").size)
    }

    @Test
    fun `Case Insensitive branch`() = runBlocking {
        assertEquals(1, searchEventsUseCase("bIg dOnAtIoN").size)
    }

    @Test
    fun `Empty Query branch`() = runBlocking {
        // tokens.isEmpty() trả về allEvents ngay lập tức
        assertEquals(4, searchEventsUseCase("").size)
        assertEquals(4, searchEventsUseCase("   ").size)
    }

    @Test
    fun `Empty Repository branch`() = runBlocking {
        coEvery { eventRepository.getAllEvents() } returns emptyList()
        assertTrue(searchEventsUseCase("any").isEmpty())
    }

    @Test(expected = Exception::class)
    fun `Repository Exception branch`() {
        runBlocking {
            coEvery { eventRepository.getAllEvents() } throws Exception("DB Error")
            searchEventsUseCase("error")
        }
    }

    @Test
    fun `Match multiple tokens on hospital fields only`() = runBlocking {
        // "Central" khớp hospitalName, "Hanoi" khớp province -> e1, e2, e3
        val result = searchEventsUseCase("Central Hanoi")
        assertEquals(3, result.size)
    }

    @Test
    fun `Empty Hospital Repository branch`() = runBlocking {
        // Kích hoạt nhánh associateBy trên danh sách bệnh viện rỗng (Nhánh branch cuối cùng)
        coEvery { hospitalRepository.getAllHospitals() } returns emptyList()
        val result = searchEventsUseCase("Big")
        assertEquals(1, result.size)
    }

    @Test
    fun `Match Province only when EventName and HospitalName fail`() = runBlocking {
        // Đảm bảo coverage cho vế cuối cùng của biểu thức ||
        // eventName: "Urgent", hospitalName: "Central Hospital", province: "Hanoi"
        // Tìm "Hanoi" -> eventName (false) || hName (false) || province (true)
        val result = searchEventsUseCase("Hanoi")
        assertTrue(result.any { it.id == "e2" })
    }

    @Test
    fun `Hospital Name is empty but Hospital exists`() = runBlocking {
        // Trường hợp này hiếm nếu data chuẩn, nhưng để phủ branch Elvis nếu field có thể null
        // Nếu Hospital trong thực tế các field có thể null (bạn nên kiểm tra lại Entity)
        // Giả sử ta mock một Hospital có tên trống hoặc Province trống
        val specialHospital = Hospital("h2", "", "", "", "", "Saigon", "")
        val specialEvent = Event("e5", "h2", "Blood", "Desc", "2024-03-01", "10:00", null, emptyList(), 200, 0, now)

        coEvery { hospitalRepository.getAllHospitals() } returns listOf(specialHospital)
        coEvery { eventRepository.getAllEvents() } returns listOf(specialEvent)

        val result = searchEventsUseCase("Saigon")
        assertEquals("e5", result[0].id)
    }

    @Test
    fun `Multiple tokens matching the same field`() = runBlocking {
        // Phủ nhánh tokens.all với nhiều phần tử đều thỏa mãn vế đầu tiên (eventName)
        val result = searchEventsUseCase("Big Donation")
        assertEquals(1, result.size)
        assertEquals("e1", result[0].id)
    }
}
