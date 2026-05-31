package com.example.heartbeat.ui.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.example.heartbeat.testing.UiTestTags
import com.example.heartbeat.ui.common.waitUntilTagExists

class SettingRobot(
    private val composeRule: ComposeTestRule
) {
    fun openLogoutDialog() {
        composeRule.waitUntilTagExists(UiTestTags.SettingLogout, timeoutMillis = 15_000)

        val logoutRowBounds = composeRule
            .onNodeWithTag(UiTestTags.SettingLogout, useUnmergedTree = true)
            .fetchSemanticsNode()
            .boundsInRoot

        composeRule.onRoot().performTouchInput {
            click(androidx.compose.ui.geometry.Offset(width - 48f, logoutRowBounds.center.y))
        }

        composeRule.waitUntilTagExists(UiTestTags.LogoutDialog, timeoutMillis = 15_000)
        composeRule.onNodeWithTag(UiTestTags.LogoutDialog).assertIsDisplayed()
    }

    fun confirmLogout() {
        composeRule.onNodeWithTag(UiTestTags.LogoutConfirmButton).performClick()
        composeRule.waitUntilTagExists(UiTestTags.LoginEmailInput, timeoutMillis = 30_000)
        composeRule.onNodeWithTag(UiTestTags.LoginEmailInput).assertIsDisplayed()
    }
}
