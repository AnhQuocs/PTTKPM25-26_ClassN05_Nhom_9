package com.example.heartbeat.presentation.features.users.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.usecase.users.auth.AuthUseCases
import com.example.heartbeat.presentation.features.users.auth.util.AuthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthActionType {
    LOGIN, SIGN_UP
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {
    private val TAG = "AuthViewModel"

    private val _authState = MutableStateFlow<Result<AuthUser>?>(null)
    val authState: StateFlow<Result<AuthUser>?> = _authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _lastAuthAction = MutableStateFlow<AuthActionType?>(null)
    val lastAuthAction: StateFlow<AuthActionType?> = _lastAuthAction

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?> (null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError

    private val _codeError = MutableStateFlow<String?>(null)
    val codeError: StateFlow<String?> = _codeError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isUserLoading = MutableStateFlow(false)
    val isUserLoading: StateFlow<Boolean> = _isUserLoading

    private val _isSendEmail = MutableStateFlow(false)
    val isSendEmail: StateFlow<Boolean> = _isSendEmail

    private val _isSendLoading = MutableStateFlow(false)
    val isSendLoading: StateFlow<Boolean> = _isSendLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        // Reactively update isLoggedIn when authState changes
        // Using manual collection in init is more robust for Unit Testing and reactive state consistency
        viewModelScope.launch {
            _authState.collect { result ->
                _isLoggedIn.value = result?.getOrNull() != null
            }
        }
        loadCurrentUser()
    }

    fun signUp(email: String, password: String, username: String) {
        _lastAuthAction.value = AuthActionType.SIGN_UP
        val emailValid = AuthValidator.isValidEmail(email)
        val passwordValid = AuthValidator.isValidPassword(password)
        val usernameValid = AuthValidator.isValidUsername(username)

        _emailError.value = if (emailValid) null else "Invalid email format"
        _passwordError.value = if (passwordValid) null else "Password must be at least 8 characters long"
        _usernameError.value = if (usernameValid) null else "Username cannot be empty"

        if (!emailValid || !passwordValid || !usernameValid) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _authState.value = authUseCases.signUp(email, password, username)
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUpWithStaffCode(email: String, password: String, username: String, code: String) {
        _lastAuthAction.value = AuthActionType.SIGN_UP
        val emailValid = AuthValidator.isValidEmail(email)
        val passwordValid = AuthValidator.isValidPassword(password)
        val usernameValid = AuthValidator.isValidUsername(username)
        val codeValid = code.isNotBlank()

        _emailError.value = if (emailValid) null else "Invalid email format"
        _passwordError.value = if (passwordValid) null else "Password must be at least 8 characters long"
        _usernameError.value = if (usernameValid) null else "Username cannot be empty"
        _codeError.value = if (codeValid) null else "Staff code cannot be empty"

        if (!emailValid || !passwordValid || !usernameValid || !codeValid) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _authState.value = authUseCases.signUpWithCode(email, password, username, code)
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        _lastAuthAction.value = AuthActionType.LOGIN
        val emailValid = AuthValidator.isValidEmail(email)
        val passwordValid = AuthValidator.isValidPassword(password)

        _emailError.value = if (emailValid) null else "Invalid email format"
        _passwordError.value = if (passwordValid) null else "Password must be at least 8 characters long"

        if (!emailValid || !passwordValid) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _authState.value = authUseCases.login(email, password)
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginWithCode(email: String, password: String, code: String) {
        _lastAuthAction.value = AuthActionType.LOGIN
        val emailValid = AuthValidator.isValidEmail(email)
        val passwordValid = AuthValidator.isValidPassword(password)
        val codeValid = code.isNotBlank()

        _emailError.value = if (emailValid) null else "Invalid email format"
        _passwordError.value = if (passwordValid) null else "Password must be at least 8 characters long"
        _codeError.value = if (codeValid) null else "Staff code cannot be empty"

        if (!emailValid || !passwordValid || !codeValid) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authUseCases.loginWithCode(email, password, code)
                _authState.value = if (result.getOrNull() == null) {
                    Result.failure(Exception("Login failed"))
                } else {
                    result
                }
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authUseCases.logout()
            } catch (e: Exception) {
                Log.e(TAG, "logout failed", e)
            } finally {
                clearAuthState()
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(email: String) {
        if (!AuthValidator.isValidEmail(email)) {
            _emailError.value = "Invalid email format"
            return
        }
        _emailError.value = null

        viewModelScope.launch {
            _isSendEmail.value = false
            _isSendLoading.value = true
            try {
                val result = authUseCases.resetPassword(email)
                result.onSuccess {
                    _isSendEmail.value = true
                }.onFailure {
                    _errorMessage.value = it.message ?: "Failed to send email"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isSendLoading.value = false
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _isUserLoading.value = true
            try {
                val user = authUseCases.getCurrentUser()
                _authState.value = if (user != null) Result.success(user) else null
            } catch (e: Exception) {
                Log.e(TAG, "failed to load current user", e)
                _authState.value = Result.failure(e)
            } finally {
                _isUserLoading.value = false
            }
        }
    }

    fun clearAuthState() {
        _authState.value = null
        _lastAuthAction.value = null
    }

    fun clearEmailError() { _emailError.value = null }
    fun clearPasswordError() { _passwordError.value = null }
    fun clearUsernameError() { _usernameError.value = null }
    fun clearErrorMessage() { _errorMessage.value = null }

    fun updateUsername(newUsername: String) {
        if (!AuthValidator.isValidUsername(newUsername)) {
            _usernameError.value = "Username cannot be empty"
            return
        }
        _usernameError.value = null

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authUseCases.updateUserNameUseCase(newUsername)
                result.onSuccess {
                    _errorMessage.value = "Username updated successfully"
                    loadCurrentUser()
                }.onFailure {
                    _errorMessage.value = it.message ?: "Failed to update username"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePassword(newPassword: String) {
        if (!AuthValidator.isValidPassword(newPassword)) {
            _passwordError.value = "Password must be at least 8 characters long"
            return
        }
        _passwordError.value = null

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authUseCases.updatePasswordUseCase(newPassword)
                result.onSuccess {
                    _errorMessage.value = "Password updated successfully"
                }.onFailure {
                    _errorMessage.value = it.message ?: "Failed to update password"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
