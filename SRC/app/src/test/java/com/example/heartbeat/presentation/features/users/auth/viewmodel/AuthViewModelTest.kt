package com.example.heartbeat.presentation.features.users.auth.viewmodel

import android.util.Log
import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.usecase.users.auth.*
import com.example.heartbeat.presentation.features.users.auth.util.AuthValidator
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private lateinit var authUseCases: AuthUseCases

    private val signUpUseCase: SignUpUseCase = mockk()
    private val signUpWithCodeUseCase: SignUpWithCodeUseCase = mockk()
    private val loginUseCase: LoginUseCase = mockk()
    private val loginWithCodeUseCase: LoginWithCodeUseCase = mockk()
    private val logoutUseCase: LogOutUseCase = mockk()
    private val resetPasswordUseCase: ResetPasswordUseCase = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase = mockk()
    private val updatePasswordUseCase: UpdatePasswordUseCase = mockk()
    private val updateUserNameUseCase: UpdateUserNameUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(AuthValidator)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        coEvery { getCurrentUserUseCase() } returns null

        authUseCases = AuthUseCases(
            signUp = signUpUseCase,
            signUpWithCode = signUpWithCodeUseCase,
            login = loginUseCase,
            loginWithCode = loginWithCodeUseCase,
            logout = logoutUseCase,
            resetPassword = resetPasswordUseCase,
            getCurrentUser = getCurrentUserUseCase,
            checkUserLoggedInUseCase = checkUserLoggedInUseCase,
            updatePasswordUseCase = updatePasswordUseCase,
            updateUserNameUseCase = updateUserNameUseCase
        )

        viewModel = AuthViewModel(authUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun mockAllValid() {
        every { AuthValidator.isValidEmail(any()) } returns true
        every { AuthValidator.isValidPassword(any()) } returns true
        every { AuthValidator.isValidUsername(any()) } returns true
    }

    private fun createSuccessUser() = AuthUser("1", "test@test.com", "Tester", "user")

    // ========== 1. VALIDATION EXHAUSTIVE PERMUTATIONS (Yellow Lines) ==========

    @Test
    fun `signUp validation exhaustive permutations`() = runTest {
        // Case 1: Email fail
        every { AuthValidator.isValidEmail(any()) } returns false
        every { AuthValidator.isValidPassword(any()) } returns true
        every { AuthValidator.isValidUsername(any()) } returns true
        viewModel.signUp("e", "p", "u")
        assertEquals("Invalid email format", viewModel.emailError.value)
        coVerify(exactly = 0) { signUpUseCase(any(), any(), any()) }

        // Case 2: Email ok, Pass fail
        every { AuthValidator.isValidEmail(any()) } returns true
        every { AuthValidator.isValidPassword(any()) } returns false
        viewModel.signUp("e", "p", "u")
        assertNull(viewModel.emailError.value) // Cover true branch (null)
        assertEquals("Password must be at least 8 characters long", viewModel.passwordError.value)

        // Case 3: E/P ok, User fail
        every { AuthValidator.isValidPassword(any()) } returns true
        every { AuthValidator.isValidUsername(any()) } returns false
        viewModel.signUp("e", "p", "u")
        assertNull(viewModel.passwordError.value) // Cover true branch (null)
        assertEquals("Username cannot be empty", viewModel.usernameError.value)

        // Case 4: All ok
        every { AuthValidator.isValidUsername(any()) } returns true
        coEvery { signUpUseCase(any(), any(), any()) } returns Result.success(createSuccessUser())
        viewModel.signUp("e", "p", "u")
        advanceUntilIdle()
        assertNull(viewModel.usernameError.value) // Cover true branch (null)
    }

    @Test
    fun `signUpWithStaffCode validation exhaustive permutations`() = runTest {
        mockAllValid()
        // Code fail
        viewModel.signUpWithStaffCode("e", "p", "u", " ")
        assertEquals("Staff code cannot be empty", viewModel.codeError.value)

        // Username fail
        every { AuthValidator.isValidUsername(any()) } returns false
        viewModel.signUpWithStaffCode("e", "p", "u", "CODE")
        assertEquals("Username cannot be empty", viewModel.usernameError.value)

        // Password fail
        every { AuthValidator.isValidUsername(any()) } returns true
        every { AuthValidator.isValidPassword(any()) } returns false
        viewModel.signUpWithStaffCode("e", "p", "u", "CODE")
        assertEquals("Password must be at least 8 characters long", viewModel.passwordError.value)

        // Email fail
        every { AuthValidator.isValidPassword(any()) } returns true
        every { AuthValidator.isValidEmail(any()) } returns false
        viewModel.signUpWithStaffCode("e", "p", "u", "CODE")
        assertEquals("Invalid email format", viewModel.emailError.value)

        // All ok
        every { AuthValidator.isValidEmail(any()) } returns true
        coEvery { signUpWithCodeUseCase(any(), any(), any(), any()) } returns Result.success(createSuccessUser())
        viewModel.signUpWithStaffCode("e", "p", "u", "CODE")
        advanceUntilIdle()
        assertNull(viewModel.codeError.value)
    }

    @Test
    fun `login and loginWithCode validation exhaustive permutations`() = runTest {
        // Login permutations
        every { AuthValidator.isValidEmail(any()) } returns false
        every { AuthValidator.isValidPassword(any()) } returns true
        viewModel.login("e", "p")
        assertEquals("Invalid email format", viewModel.emailError.value)

        every { AuthValidator.isValidEmail(any()) } returns true
        every { AuthValidator.isValidPassword(any()) } returns false
        viewModel.login("e", "p")
        assertEquals("Password must be at least 8 characters long", viewModel.passwordError.value)

        // LoginWithCode permutations
        every { AuthValidator.isValidPassword(any()) } returns true
        viewModel.loginWithCode("e", "p", "")
        assertEquals("Staff code cannot be empty", viewModel.codeError.value)

        every { AuthValidator.isValidPassword(any()) } returns false
        viewModel.loginWithCode("e", "p", "c")
        assertEquals("Password must be at least 8 characters long", viewModel.passwordError.value)

        every { AuthValidator.isValidPassword(any()) } returns true
        every { AuthValidator.isValidEmail(any()) } returns false
        viewModel.loginWithCode("e", "p", "c")
        assertEquals("Invalid email format", viewModel.emailError.value)
    }

    // ========== 2. SUCCESS PATHS & MAPPING (Red Lines) ==========

    @Test
    fun `loginWithCode result mapping exhaustive coverage`() = runTest {
        mockAllValid()
        // Case: Result.failure -> getOrNull() is null
        coEvery { loginWithCodeUseCase(any(), any(), any()) } returns Result.failure(Exception("Fail"))
        viewModel.loginWithCode("e", "p", "c")
        advanceUntilIdle()
        assertTrue(viewModel.authState.value?.isFailure == true)
        assertEquals("Login failed", viewModel.authState.value?.exceptionOrNull()?.message)

        // Case: Success(user) -> else branch
        val user = createSuccessUser()
        coEvery { loginWithCodeUseCase(any(), any(), any()) } returns Result.success(user)
        viewModel.loginWithCode("e", "p", "c")
        advanceUntilIdle()
        assertEquals(user, viewModel.authState.value?.getOrNull())
    }

    @Test
    fun `updateUsername success path exhaustive`() = runTest {
        every { AuthValidator.isValidUsername(any()) } returns true
        coEvery { updateUserNameUseCase(any()) } returns Result.success(Unit)
        val updatedUser = createSuccessUser()
        coEvery { getCurrentUserUseCase() } returns updatedUser

        viewModel.updateUsername("NewName")
        advanceUntilIdle()

        assertEquals("Username updated successfully", viewModel.errorMessage.value)
        assertEquals(updatedUser, viewModel.authState.value?.getOrNull())
    }

    @Test
    fun `updatePassword success path exhaustive`() = runTest {
        every { AuthValidator.isValidPassword(any()) } returns true
        coEvery { updatePasswordUseCase(any()) } returns Result.success(Unit)

        viewModel.updatePassword("newPassword123")
        advanceUntilIdle()

        assertEquals("Password updated successfully", viewModel.errorMessage.value)
    }

    @Test
    fun `resetPassword success path`() = runTest {
        every { AuthValidator.isValidEmail(any()) } returns true
        coEvery { resetPasswordUseCase(any()) } returns Result.success(Unit)

        viewModel.resetPassword("test@test.com")
        advanceUntilIdle()
        assertTrue(viewModel.isSendEmail.value)
        assertNull(viewModel.emailError.value) // Coverage for _emailError.value = null
    }

    // ========== 3. ELVIS & EXCEPTION BRANCHES ==========

    @Test
    fun `exhaustive error elvis operator coverage`() = runTest {
        mockAllValid()

        // updateUsername onFailure null message
        coEvery { updateUserNameUseCase(any()) } returns Result.failure(Exception())
        viewModel.updateUsername("name")
        advanceUntilIdle()
        assertEquals("Failed to update username", viewModel.errorMessage.value)

        // updatePassword catch null message
        coEvery { updatePasswordUseCase(any()) } throws Exception()
        viewModel.updatePassword("pass")
        advanceUntilIdle()
        assertEquals("An error occurred", viewModel.errorMessage.value)

        // resetPassword onFailure null message
        coEvery { resetPasswordUseCase(any()) } returns Result.failure(Exception())
        viewModel.resetPassword("e@e.com")
        advanceUntilIdle()
        assertEquals("Failed to send email", viewModel.errorMessage.value)

        // resetPassword catch null message
        coEvery { resetPasswordUseCase(any()) } throws Exception()
        viewModel.resetPassword("e@e.com")
        advanceUntilIdle()
        assertEquals("An error occurred", viewModel.errorMessage.value)
    }

    @Test
    fun `loadCurrentUser and logout exception logs`() = runTest {
        val ex = Exception("Fail")
        coEvery { getCurrentUserUseCase() } throws ex
        viewModel.loadCurrentUser()
        advanceUntilIdle()
        verify { Log.e("AuthViewModel", "failed to load current user", ex) }

        coEvery { logoutUseCase() } throws ex
        viewModel.logout()
        advanceUntilIdle()
        verify { Log.e("AuthViewModel", "logout failed", ex) }
    }

    @Test
    fun `auth actions catch blocks`() = runTest {
        mockAllValid()
        val ex = Exception("Crash")

        coEvery { signUpUseCase(any(), any(), any()) } throws ex
        viewModel.signUp("e", "p", "u")
        advanceUntilIdle()
        assertEquals(ex, viewModel.authState.value?.exceptionOrNull())

        coEvery { loginUseCase(any(), any()) } throws ex
        viewModel.login("e", "p")
        advanceUntilIdle()
        assertEquals(ex, viewModel.authState.value?.exceptionOrNull())
    }

    // ========== 4. STATE FLOWS ==========

    @Test
    fun `isLoggedIn state flow exhaustive branches`() = runTest {
        // null state
        assertFalse(viewModel.isLoggedIn.value)

        // isFailure
        mockAllValid()
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception())
        viewModel.login("e", "p")
        advanceUntilIdle()
        assertFalse(viewModel.isLoggedIn.value)

        // isSuccess but null user (using mock)
        val mockResult = mockk<Result<AuthUser>>()
        every { mockResult.isSuccess } returns true
        every { mockResult.getOrNull() } returns null
        coEvery { loginUseCase(any(), any()) } returns mockResult
        viewModel.login("e", "p")
        advanceUntilIdle()
        assertFalse(viewModel.isLoggedIn.value)

        // isSuccess with user
        coEvery { loginUseCase(any(), any()) } returns Result.success(createSuccessUser())
        viewModel.login("e", "p")
        advanceUntilIdle()
        assertTrue(viewModel.isLoggedIn.value)
    }

    @Test
    fun `resetPassword validation fail`() = runTest {
        every { AuthValidator.isValidEmail(any()) } returns false
        viewModel.resetPassword("bad")
        assertEquals("Invalid email format", viewModel.emailError.value)
    }

    @Test
    fun `clear methods execution`() = runTest {
        viewModel.clearEmailError()
        viewModel.clearPasswordError()
        viewModel.clearUsernameError()
        viewModel.clearErrorMessage()
        viewModel.clearAuthState()
        assertNull(viewModel.emailError.value)
        assertNull(viewModel.authState.value)
    }
}