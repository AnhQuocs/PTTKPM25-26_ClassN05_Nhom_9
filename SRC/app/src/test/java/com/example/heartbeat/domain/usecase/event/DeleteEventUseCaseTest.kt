package com.example.heartbeat.domain.usecase.event

import com.example.heartbeat.domain.repository.event.EventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteEventUseCaseTest {
    private lateinit var repository: EventRepository
    private lateinit var deleteEventUseCase: DeleteEventUseCase

    @Before
    fun setUp() {
        repository = mockk()
        deleteEventUseCase = DeleteEventUseCase(repository)
    }

    @Test
    fun `DeleteEvent should call repository`() = runBlocking {
        val eventId = "e1"
        coEvery { repository.deleteEvent(any()) } returns Unit

        deleteEventUseCase(eventId)

        coVerify(exactly = 1) { repository.deleteEvent(eventId) }
    }

    /**
     * Kịch bản TEST CHẮC CHẮN FAIL để làm báo cáo:
     * Mong đợi: Nếu ID trống thì KHÔNG được gọi repository.
     * Kết quả hiện tại: Code chưa check nên vẫn gọi -> Test sẽ báo Fail ở dòng coVerify.
     */
    @Test
    fun `DeleteEvent with blank ID should NOT call repository`() = runBlocking {
        val eventId = ""
        coEvery { repository.deleteEvent(any()) } returns Unit

        deleteEventUseCase(eventId)

        // Mong đợi số lần gọi là 0 (exactly = 0)
        // Lệnh này sẽ ném ra lỗi vì thực tế code đang gọi 1 lần
        coVerify(exactly = 0) { repository.deleteEvent(any()) }
    }
}
