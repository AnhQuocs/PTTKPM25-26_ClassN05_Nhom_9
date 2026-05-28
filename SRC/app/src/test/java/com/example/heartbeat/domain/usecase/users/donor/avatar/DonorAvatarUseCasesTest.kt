package com.example.heartbeat.domain.usecase.users.donor.avatar

import com.example.heartbeat.domain.entity.users.DonorAvatar
import com.example.heartbeat.domain.repository.users.donor.DonorRepository
import com.example.heartbeat.domain.usecase.users.donor.GetAvatarUseCase
import com.example.heartbeat.domain.usecase.users.donor.SaveAvatarUrlUseCase
import com.example.heartbeat.domain.usecase.users.donor.UpdateAvatarUseCase
import com.example.heartbeat.domain.usecase.users.donor.UploadAvatarUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DonorAvatarUseCasesTest {

    private lateinit var repository: DonorRepository
    private lateinit var uploadAvatarUseCase: UploadAvatarUseCase
    private lateinit var updateAvatarUseCase: UpdateAvatarUseCase
    private lateinit var saveAvatarUrlUseCase: SaveAvatarUrlUseCase
    private lateinit var getAvatarUseCase: GetAvatarUseCase

    private val testAvatar = DonorAvatar(donorId = "d1", avatarUrl = "http://link.com/img.jpg")

    @Before
    fun setUp() {
        repository = mockk()
        uploadAvatarUseCase = UploadAvatarUseCase(repository)
        updateAvatarUseCase = UpdateAvatarUseCase(repository)
        saveAvatarUrlUseCase = SaveAvatarUrlUseCase(repository)
        getAvatarUseCase = GetAvatarUseCase(repository)
    }

    // --- UploadAvatarUseCase ---
    @Test
    fun `UploadAvatar success should return url`() = runBlocking {
        coEvery { repository.uploadAvatarBase64("d1", "base64str") } returns "http://newurl.com"
        val result = uploadAvatarUseCase("d1", "base64str")
        assertEquals("http://newurl.com", result)
    }

    @Test(expected = Exception::class)
    fun `UploadAvatar failure should throw exception`() {
        runBlocking {
            coEvery { repository.uploadAvatarBase64(any(), any()) } throws Exception("Upload failed")
            uploadAvatarUseCase("d1", "invalid")
        }
    }

    // --- UpdateAvatarUseCase ---
    @Test
    fun `UpdateAvatar success should return url`() = runBlocking {
        coEvery { repository.updateAvatarBase64("d1", "newbase64") } returns "http://updated.com"
        val result = updateAvatarUseCase("d1", "newbase64")
        assertEquals("http://updated.com", result)
    }

    @Test(expected = Exception::class)
    fun `UpdateAvatar failure should throw exception`() {
        runBlocking {
            coEvery { repository.updateAvatarBase64(any(), any()) } throws Exception("Update failed")
            updateAvatarUseCase("d1", "base64")
        }
    }

    // --- SaveAvatarUrlUseCase ---
    @Test
    fun `SaveAvatarUrl success should call repository`() = runBlocking {
        coEvery { repository.saveAvatarUrl(any()) } returns Unit
        saveAvatarUrlUseCase(testAvatar)
        coVerify(exactly = 1) { repository.saveAvatarUrl(testAvatar) }
    }

    @Test(expected = Exception::class)
    fun `SaveAvatarUrl failure should throw exception`() = runBlocking {
        coEvery { repository.saveAvatarUrl(any()) } throws Exception("Save failed")
        saveAvatarUrlUseCase(testAvatar)
    }

    // --- GetAvatarUseCase ---
    @Test
    fun `GetAvatar success should return DonorAvatar`() = runBlocking {
        coEvery { repository.getAvatar("d1") } returns testAvatar
        val result = getAvatarUseCase("d1")
        assertEquals(testAvatar, result)
    }
}
