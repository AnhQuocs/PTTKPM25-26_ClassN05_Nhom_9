package com.example.heartbeat.ui.flows

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.example.heartbeat.testing.UiTestTags
import com.example.heartbeat.ui.common.hasTag
import com.example.heartbeat.ui.data.TestCredentials
import com.example.heartbeat.ui.robots.LoginRobot
import com.example.heartbeat.ui.robots.MainRobot

class LoginFlow(
    private val composeRule: ComposeTestRule
) {
    private val loginRobot = LoginRobot(composeRule)
    private val mainRobot = MainRobot(composeRule)

    fun loginToHome(credentials: TestCredentials) {
        if (composeRule.hasTag(UiTestTags.BottomTabHome)) return

        loginRobot.openLoginScreen()
        if (composeRule.hasTag(UiTestTags.BottomTabHome)) return

        if (credentials.loginAsEmployee) {
            println("UI automation login path: employee/admin")
            loginRobot.openEmployeeLoginScreen()
            loginRobot.enterEmployeeCredentials(credentials)
            loginRobot.submitEmployee()
        } else {
            println("UI automation login path: user")
            loginRobot.enterCredentials(credentials)
            loginRobot.submit()
        }

        mainRobot.waitForSignedInDestination()
    }
}
