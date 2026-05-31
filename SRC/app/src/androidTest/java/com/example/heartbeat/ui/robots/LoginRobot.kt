package com.example.heartbeat.ui.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.heartbeat.testing.UiTestTags
import com.example.heartbeat.ui.common.hasTag
import com.example.heartbeat.ui.common.hasText
import com.example.heartbeat.ui.common.waitUntilTagExists
import com.example.heartbeat.ui.data.TestCredentials

class LoginRobot(
    private val composeRule: ComposeTestRule
) {
    fun openLoginScreen() {
        repeat(4) {
            composeRule.waitUntil(15_000) {
                composeRule.hasTag(UiTestTags.LoginEmailInput) ||
                    composeRule.hasTag(UiTestTags.BottomTabHome) ||
                    composeRule.hasText("Get Started") ||
                    composeRule.hasText("Next")
            }

            if (
                composeRule.hasTag(UiTestTags.LoginEmailInput) ||
                composeRule.hasTag(UiTestTags.BottomTabHome)
            ) {
                return
            }

            when {
                composeRule.hasText("Get Started") -> {
                    composeRule.onAllNodesWithText("Get Started")[0].performClick()
                }
                composeRule.hasText("Next") -> {
                    composeRule.onAllNodesWithText("Next")[0].performClick()
                }
                else -> {
                    composeRule.waitForIdle()
                }
            }
        }

        composeRule.waitUntilTagExists(UiTestTags.LoginEmailInput, timeoutMillis = 15_000)
    }

    fun enterCredentials(credentials: TestCredentials) {
        composeRule.onNodeWithTag(UiTestTags.LoginEmailInput).performTextClearance()
        composeRule.onNodeWithTag(UiTestTags.LoginEmailInput).performTextInput(credentials.email)

        composeRule.onNodeWithTag(UiTestTags.LoginPasswordInput).performTextClearance()
        composeRule.onNodeWithTag(UiTestTags.LoginPasswordInput).performTextInput(credentials.password)
    }

    fun submit() {
        composeRule.onNodeWithTag(UiTestTags.LoginButton).performClick()
    }
}
