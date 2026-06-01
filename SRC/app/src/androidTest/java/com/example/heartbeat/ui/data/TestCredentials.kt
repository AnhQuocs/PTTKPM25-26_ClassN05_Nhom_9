package com.example.heartbeat.ui.data

import androidx.test.platform.app.InstrumentationRegistry

data class TestCredentials(
    val email: String,
    val password: String,
    val code: String = "",
    val loginAsEmployee: Boolean = false
) {
    companion object {
        fun fromInstrumentation(): TestCredentials {
            val args = InstrumentationRegistry.getArguments()
            return TestCredentials(
                email = args.getString("test_email")?.takeIf { it.isNotBlank() }
                    ?: "test.user.heartbeat@gmail.com",
                password = args.getString("test_password")?.takeIf { it.isNotBlank() }
                    ?: "Test@123456",
                code = args.getString("test_code").orEmpty(),
                loginAsEmployee = args.getString("test_login_as_employee")
                    ?.equals("true", ignoreCase = true)
                    ?: false
            )
        }
    }
}
