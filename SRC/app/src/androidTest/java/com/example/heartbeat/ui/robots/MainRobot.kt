package com.example.heartbeat.ui.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.heartbeat.testing.UiTestTags
import com.example.heartbeat.ui.common.waitUntilTagExists

class MainRobot(
    private val composeRule: ComposeTestRule
) {
    fun waitForHome() {
        composeRule.waitUntilTagExists(UiTestTags.BottomTabHome, timeoutMillis = 45_000)
    }

    fun openSetting() {
        composeRule.onNodeWithTag(UiTestTags.BottomTabSetting).performClick()
        composeRule.waitUntilTagExists(UiTestTags.SettingLogout, timeoutMillis = 15_000)
    }
}
