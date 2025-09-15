package com.example.heartbeat.presentation.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.user.AuthUser
import com.example.heartbeat.domain.usecase.auth.AuthUseCases
import com.example.heartbeat.presentation.features.auth.util.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthActionType {
    LOGIN, SIGN_UP
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {
    private val _authState = MutableStateFlow<Result<AuthUser>?>(null)
    val authState: StateFlow<Result<AuthUser>?> = _authState

    private val _lastAuthAction = MutableStateFlow<AuthActionType?>(null)
    val lastAuthAction: StateFlow<AuthActionType?> = _lastAuthAction

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?> (null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError

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

    fun signUp(email: String, password: String, username: String) {
        var isValid = true
        _lastAuthAction.value = AuthActionType.SIGN_UP

        if(!Validator.isValidEmail(email)) {
            _emailError.value = "Invalid email format"
            isValid = false
        } else {
            _emailError.value = null
        }

        if(!Validator.isValidPassword(password)) {
            _passwordError.value = "Password must be at least 8 characters long"
            isValid = false
        } else {
            _passwordError.value = null
        }

        if(!Validator.isValidUsername(username)) {
            _usernameError.value = "Username cannot be empty"
            isValid = false
        } else {
            _usernameError.value = null
        }

        if (!isValid) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = authUseCases.signUp(email, password, username)
                _authState.value = result
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUpWithStaffCode(email: String, password: String, username: String, staffCode: String) {
        var isValid = true
        _lastAuthAction.value = AuthActionType.SIGN_UP

        if (!Validator.isValidEmail(email)) {
            _emailError.value = "Invalid email format"
            isValid = false
        } else {
            _emailError.value = null
        }

        if (!Validator.isValidPassword(password)) {
            _passwordError.value = "Password must be at least 8 characters long"
            isValid = false
        } else {
            _passwordError.value = null
        }

        if (!Validator.isValidUsername(username)) {
            _usernameError.value = "Username cannot be empty"
            isValid = false
        } else {
            _usernameError.value = null
        }

        if (staffCode.isBlank()) {
            _errorMessage.value = "Staff code cannot be empty"
            isValid = false
        }

        if (!isValid) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = authUseCases.signUpWithStaffCode(email, password, username, staffCode)
                _authState.value = result
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        var isValid = true
        _lastAuthAction.value = AuthActionType.LOGIN

        if(!Validator.isValidEmail(email)) {
            _emailError.value = "Invalid email format"
            isValid = false
        } else {
            _emailError.value = null
        }

        if(!Validator.isValidPassword(password)) {
            _passwordError.value = "Password must be at least 8 characters long"
            isValid = false
        } else {
            _passwordError.value = null
        }

        if (!isValid) return

        viewModelScope.launch {
            _isLoading.value = true
            _authState.value = authUseCases.login(email, password)
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            authUseCases.logout()
            _isLoading.value = false
        }
    }

    fun resetPassword(email: String) {
        var isValid = true
        if(!Validator.isValidEmail(email)) {
            _emailError.value = "Invalid email format"
            isValid = false
        } else {
            _emailError.value = null
        }

        if (!isValid) return

        viewModelScope.launch {
            _isSendEmail.value = false
            _isSendLoading.value = true
            val result = authUseCases.resetPassword(email)
            result.onSuccess {
                _isSendEmail.value = true
                _isSendLoading.value = false
            }.onFailure {
                _isSendEmail.value = false
                _errorMessage.value = it.message ?: "Failed to send email"
            }
        }
    }

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _isUserLoading.value = true
            try {
                val user = authUseCases.getCurrentUser()
                if (user != null) {
                    _authState.value = Result.success(user)
                } else {
                    _authState.value = null
                }
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
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    val isLoggedIn: StateFlow<Boolean> = authState
        .map { result -> result?.isSuccess == true && result.getOrNull() != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}