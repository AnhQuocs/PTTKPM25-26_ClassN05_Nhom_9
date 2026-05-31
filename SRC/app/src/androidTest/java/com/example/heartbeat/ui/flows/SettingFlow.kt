package com.example.heartbeat.ui.flows

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.example.heartbeat.ui.data.TestCredentials
import com.example.heartbeat.ui.robots.MainRobot
import com.example.heartbeat.ui.robots.SettingRobot

class SettingFlow(
    composeRule: ComposeTestRule,
    private val loginFlow: LoginFlow
) {
    private val mainRobot = MainRobot(composeRule)
    private val settingRobot = SettingRobot(composeRule)

    fun logoutFromSetting(credentials: TestCredentials) {
        loginFlow.loginToHome(credentials)
        mainRobot.openSetting()
        settingRobot.openLogoutDialog()
        settingRobot.confirmLogout()
    }
}
