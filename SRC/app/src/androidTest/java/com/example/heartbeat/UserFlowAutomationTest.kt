package com.example.heartbeat

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.heartbeat.presentation.features.main.MainActivity
import com.example.heartbeat.testing.UiTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFlowAutomationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val email: String
        get() = instrumentationArg("test_email", "ui.test@heartbeat.local")

    private val password: String
        get() = instrumentationArg("test_password", "UITest@123456")

    @Test
    fun loginFlow_entersCredentialsAndOpensHome() {
        loginToHome()

        composeRule.onNodeWithTag(UiTestTags.BottomTabHome).assertIsDisplayed()
    }

    @Test
    fun settingFlow_opensLogoutDialogAndConfirmsLogout() {
        loginToHome()

        composeRule.onNodeWithTag(UiTestTags.BottomTabSetting).performClick()
        composeRule.waitUntilExists(UiTestTags.SettingLogout)
        composeRule.onNodeWithTag(UiTestTags.SettingLogout).performClick()

        composeRule.onNodeWithTag(UiTestTags.LogoutDialog).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.LogoutConfirmButton).performClick()

        composeRule.waitUntilExists(UiTestTags.LoginEmailInput, timeoutMillis = 30_000)
        composeRule.onNodeWithTag(UiTestTags.LoginEmailInput).assertIsDisplayed()
    }

    private fun loginToHome() {
        if (composeRule.hasTag(UiTestTags.BottomTabHome)) return

        openLoginScreen()
        if (composeRule.hasTag(UiTestTags.BottomTabHome)) return

        composeRule.onNodeWithTag(UiTestTags.LoginEmailInput)
            .performTextClearance()
        composeRule.onNodeWithTag(UiTestTags.LoginEmailInput)
            .performTextInput(email)

        composeRule.onNodeWithTag(UiTestTags.LoginPasswordInput)
            .performTextClearance()
        composeRule.onNodeWithTag(UiTestTags.LoginPasswordInput)
            .performTextInput(password)

        composeRule.onNodeWithTag(UiTestTags.LoginButton).performClick()
        composeRule.waitUntilExists(UiTestTags.BottomTabHome, timeoutMillis = 45_000)
    }

    private fun openLoginScreen() {
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

        composeRule.waitUntilExists(UiTestTags.LoginEmailInput, timeoutMillis = 15_000)
    }

    private fun instrumentationArg(name: String, defaultValue: String): String {
        return InstrumentationRegistry.getArguments().getString(name)?.takeIf { it.isNotBlank() }
            ?: defaultValue
    }

    private fun ComposeTestRule.waitUntilExists(
        tag: String,
        timeoutMillis: Long = 10_000
    ) {
        waitUntil(timeoutMillis) { hasTag(tag) }
    }

    private fun ComposeTestRule.hasTag(tag: String): Boolean {
        return onAllNodesWithTag(tag, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
    }

    private fun ComposeTestRule.hasText(text: String): Boolean {
        return onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
    }
}
