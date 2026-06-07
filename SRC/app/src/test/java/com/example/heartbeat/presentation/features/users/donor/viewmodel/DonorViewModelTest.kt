package com.example.heartbeat.presentation.features.users.donor.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.domain.entity.users.DonorAvatar
import com.example.heartbeat.domain.usecase.users.donor.DonorUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class DonorViewModelTest {

    private lateinit var viewModel: DonorViewModel
    private val donorUseCase: DonorUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(FirebaseAuth::class)
        mockkStatic(Uri::class)
        mockkStatic(Base64::class)
        mockkStatic(Log::class)

        every { Log.d(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>(), any<Throwable>()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0

        every { Base64.encodeToString(any<ByteArray>(), any<Int>()) } returns "base64_encoded_string"

        viewModel = DonorViewModel(donorUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun mockFirebaseAuth(userId: String?) {
        val auth = mockk<FirebaseAuth>()
        val user = if (userId != null) {
            mockk<FirebaseUser>().apply { every { uid } returns userId }
        } else null
        every { FirebaseAuth.getInstance() } returns auth
        every { auth.currentUser } returns user
    }

    private fun createSampleDonor(id: String = "uid") = Donor(
        donorId = id,
        name = "John",
        phoneNumber = "0123456789",
        bloodGroup = "O+",
        cityId = "Hanoi",
        dateOfBirth = "2000-01-01",
        age = 24,
        gender = "Male",
        willingToDonate = true,
        about = "Bio"
    )

    @Test
    fun `setLocalAvatar branches`() {
        viewModel.setLocalAvatar(null)
        assertEquals("", viewModel.formState.value.profileAvatar)

        val mockUri = mockk<Uri>()
        every { mockUri.toString() } returns "content://photo"
        viewModel.setLocalAvatar(mockUri)
        assertEquals("content://photo", viewModel.formState.value.profileAvatar)
    }

    @Test
    fun `all methods should return early if userId is null`() = runTest {
        mockFirebaseAuth(null)
        
        viewModel.submitDonor(mockk())
        advanceUntilIdle()
        coVerify(exactly = 0) { donorUseCase.addDonorUseCase(any()) }

        viewModel.getCurrentDonor()
        advanceUntilIdle()
        coVerify(exactly = 0) { donorUseCase.isDonorProfileExistUseCase(any()) }

        viewModel.updateDonor(createSampleDonor())
        advanceUntilIdle()
        coVerify(exactly = 0) { donorUseCase.updateDonorUseCase(any(), any()) }
    }

    @Test
    fun `submitDonor avatar logic branches`() = runTest {
        mockFirebaseAuth("uid")
        val context = mockk<Context>()
        
        viewModel.setLocalAvatar(null)
        coEvery { donorUseCase.addDonorUseCase(any()) } just Runs
        viewModel.submitDonor(context)
        advanceUntilIdle()
        coVerify(exactly = 0) { donorUseCase.uploadAvatarUseCase(any(), any()) }

        val mockUri = mockk<Uri>()
        every { mockUri.toString() } returns "http://already.uploaded/image.jpg"
        viewModel.setLocalAvatar(mockUri)
        viewModel.submitDonor(context)
        advanceUntilIdle()
        coVerify(exactly = 0) { donorUseCase.uploadAvatarUseCase(any(), any()) }
        
        val contentUri = mockk<Uri>()
        val contentResolver = mockk<ContentResolver>()
        every { contentUri.toString() } returns "content://local/photo"
        every { Uri.parse("content://local/photo") } returns contentUri
        every { context.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(contentUri) } returns ByteArrayInputStream(byteArrayOf(1, 2, 3))
        
        coEvery { donorUseCase.uploadAvatarUseCase("uid", any()) } returns "https://url.com/avatar.jpg"
        coEvery { donorUseCase.saveAvatarUrlUseCase(any()) } just Runs
        
        viewModel.setLocalAvatar(contentUri)
        viewModel.submitDonor(context)
        advanceUntilIdle()
        coVerify(exactly = 1) { donorUseCase.uploadAvatarUseCase("uid", "base64_encoded_string") }
    }

    @Test
    fun `submitDonor uriToBase64 throw IOException`() = runTest {
        mockFirebaseAuth("uid")
        val context = mockk<Context>()
        val contentResolver = mockk<ContentResolver>()
        val mockUri = mockk<Uri>()
        every { mockUri.toString() } returns "content://fail"
        every { Uri.parse("content://fail") } returns mockUri
        every { context.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(mockUri) } returns null // Kích hoạt ?: throw IOException

        viewModel.setLocalAvatar(mockUri)
        viewModel.submitDonor(context)
        advanceUntilIdle()

        assertNotNull(viewModel.formState.value.error)
        assertTrue(viewModel.formState.value.error!!.contains("Cannot open input stream"))
    }

    @Test
    fun `getCurrentDonor success and exception branches`() = runTest {
        mockFirebaseAuth("uid")
        
        coEvery { donorUseCase.isDonorProfileExistUseCase("uid") } returns true
        val donor = createSampleDonor("uid")
        coEvery { donorUseCase.getCurrentDonorUseCase("uid") } returns donor
        
        var profileExists: Boolean? = null
        viewModel.getCurrentDonor { profileExists = it }
        advanceUntilIdle()
        assertEquals(true, profileExists)
        assertEquals("John", viewModel.formState.value.name)

        coEvery { donorUseCase.getCurrentDonorUseCase("uid") } returns null
        viewModel.getCurrentDonor { profileExists = it }
        advanceUntilIdle()
        assertEquals(false, profileExists)
        assertNotNull(viewModel.formState.value.error)

        coEvery { donorUseCase.isDonorProfileExistUseCase("uid") } throws Exception("Database Error")
        viewModel.getCurrentDonor { profileExists = it }
        advanceUntilIdle()
        assertEquals(false, profileExists)
        assertEquals("Database Error", viewModel.formState.value.error)
    }

    @Test
    fun `getDonorById success and failure branches`() = runTest {
        val donor = createSampleDonor("id1")
        coEvery { donorUseCase.getCurrentDonorUseCase("id1") } returns donor
        viewModel.getDonorById("id1")
        advanceUntilIdle()
        assertEquals(donor, viewModel.originalDonor.value)
        
        coEvery { donorUseCase.getCurrentDonorUseCase("id2") } returns null
        viewModel.getDonorById("id2")
        advanceUntilIdle()
        assertEquals("Donor not found", viewModel.formState.value.error)
        
        coEvery { donorUseCase.getCurrentDonorUseCase("id3") } throws Exception("Failed")
        viewModel.getDonorById("id3")
        advanceUntilIdle()
        assertEquals("Failed", viewModel.formState.value.error)
    }

    @Test
    fun `updateDonor success and error paths`() = runTest {
        mockFirebaseAuth("uid")
        
        // Thành công
        coEvery { donorUseCase.updateDonorUseCase("uid", any()) } just Runs
        viewModel.updateDonor(createSampleDonor())
        advanceUntilIdle()
        assertTrue(viewModel.formState.value.isSubmitSuccess)
        verify { Log.d(any<String>(), match<String> { it.contains("updated successfully") }) }
        
        val ex = Exception("Update Failed")
        coEvery { donorUseCase.updateDonorUseCase("uid", any()) } throws ex
        viewModel.updateDonor(createSampleDonor())
        advanceUntilIdle()
        assertEquals("Update Failed", viewModel.formState.value.error)
        verify { Log.e(any<String>(), any<String>(), ex) }
    }

    @Test
    fun `fetchDonorAndCache branches`() = runTest {
        val donor = createSampleDonor("d1")
        coEvery { donorUseCase.getCurrentDonorUseCase("d1") } returns donor
        
        viewModel.fetchDonorAndCache("d1")
        advanceUntilIdle()
        assertEquals(donor, viewModel.donorCache.value["d1"])
        
        coEvery { donorUseCase.getCurrentDonorUseCase("d2") } returns null
        viewModel.fetchDonorAndCache("d2")
        advanceUntilIdle()
        verify { Log.w(any<String>(), match<String> { it.contains("No donor found") }) }
        
        val ex = Exception("Network Error")
        coEvery { donorUseCase.getCurrentDonorUseCase("d3") } throws ex
        viewModel.fetchDonorAndCache("d3")
        advanceUntilIdle()
        verify { Log.e(any<String>(), match<String> { it.contains("Error fetching donor") }, ex) }
    }

    @Test
    fun `getAvatar success and failure`() = runTest {
        val avatar = DonorAvatar("u1", "url")
        coEvery { donorUseCase.getAvatarUseCase("u1") } returns avatar
        
        viewModel.getAvatar("u1")
        advanceUntilIdle()
        assertEquals(avatar, viewModel.donorAvatar.value)
        
        coEvery { donorUseCase.getAvatarUseCase("u2") } throws Exception("No image found")
        viewModel.getAvatar("u2")
        advanceUntilIdle()
        assertEquals("No image found", viewModel.formState.value.error)
    }

    @Test
    fun `utility functions and state updates`() {
        viewModel.updatePersonalInfo("Test", "0123", "O-", "HN")
        viewModel.updateBasicInfo("2024", 0, "Other", false, "Hello")
        viewModel.setStep(1)
        viewModel.clearError()
        
        val state = viewModel.formState.value
        assertEquals("Test", state.name)
        assertEquals(0, state.age)
        assertEquals(1, state.currentStep)
        assertNull(state.error)
    }
}
