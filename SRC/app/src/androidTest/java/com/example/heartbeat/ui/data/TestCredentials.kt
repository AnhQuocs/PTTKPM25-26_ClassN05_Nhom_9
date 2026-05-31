package com.example.heartbeat.ui.data

import androidx.test.platform.app.InstrumentationRegistry

data class TestCredentials(
    val email: String,
    val password: String
) {
    companion object {
        fun fromInstrumentation(): TestCredentials {
            val args = InstrumentationRegistry.getArguments()
            return TestCredentials(
                email = args.getString("test_email")?.takeIf { it.isNotBlank() }
                    ?: "test.user.heartbeat@gmail.com",
                password = args.getString("test_password")?.takeIf { it.isNotBlank() }
                    ?: "Test@123456"
            )
        }
    }
}
