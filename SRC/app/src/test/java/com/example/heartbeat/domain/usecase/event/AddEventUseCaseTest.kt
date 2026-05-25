package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.repository.event.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test

class AddEventUseCaseTest {
    private lateinit var repository: EventRepository
    private lateinit var addEventUseCase: AddEventUseCase

    @Before
    fun setUp() {
        repository = mockk()
        addEventUseCase = AddEventUseCase(repository)
    }

    private fun createDummyEvent(name: String = "Event Name", capacity: Int = 100) = Event(
        id = "e1",
        locationId = "l1",
        name = name,
        description = "Description",
        date = "2023-10-10",
        time = "10:00",
        deadline = null,
        donorList = emptyList(),
        capacity = capacity,
        donorCount = 0,
        createdAt = LocalDateTime(2023, 10, 10, 0, 0)
    )

    @Test
    fun `AddEvent with valid data should call repository`() = runBlocking {
        val event = createDummyEvent()
        coEvery { repository.addEvent(any()) } returns Unit

        addEventUseCase(event)

        coVerify(exactly = 1) { repository.addEvent(event) }
    }

    /**
     * KỊCH BẢN TEST CHẮC CHẮN FAIL ĐỂ LÀM BÁO CÁO (Trường hợp 1)
     * Mong đợi: Nếu tên sự kiện trống, KHÔNG được gọi repository để tránh lưu dữ liệu bẩn.
     * Hiện tại: FAIL vì code UseCase chưa có validation, vẫn gọi repo 1 lần.
     */
    @Test
    fun `AddEvent with blank name should NOT call repository`() = runBlocking {
        val invalidEvent = createDummyEvent(name = "")
        coEvery { repository.addEvent(any()) } returns Unit

        addEventUseCase(invalidEvent)

        // Mong đợi số lần gọi là 0 (exactly = 0)
        coVerify(exactly = 0) { repository.addEvent(any()) }
    }

    /**
     * KỊCH BẢN TEST CHẮC CHẮN FAIL ĐỂ LÀM BÁO CÁO (Trường hợp 2)
     * Mong đợi: Nếu sức chứa không hợp lệ (<= 0), KHÔNG được gọi repository.
     * Hiện tại: FAIL vì code chưa có bước kiểm tra sức chứa.
     */
    @Test
    fun `AddEvent with zero capacity should NOT call repository`() = runBlocking {
        val invalidEvent = createDummyEvent(capacity = 0)
        coEvery { repository.addEvent(any()) } returns Unit

        addEventUseCase(invalidEvent)

        coVerify(exactly = 0) { repository.addEvent(any()) }
    }
}
