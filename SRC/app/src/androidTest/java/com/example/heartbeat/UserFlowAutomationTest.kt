package com.example.heartbeat

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.heartbeat.presentation.features.main.MainActivity
import com.example.heartbeat.testing.UiTestTags
import com.example.heartbeat.ui.data.TestCredentials
import com.example.heartbeat.ui.flows.LoginFlow
import com.example.heartbeat.ui.flows.SettingFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFlowAutomationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val credentials = TestCredentials.fromInstrumentation()
    private val loginFlow = LoginFlow(composeRule)
    private val settingFlow = SettingFlow(composeRule, loginFlow)

    @Test
    fun loginFlow_entersCredentialsAndOpensHome() {
        loginFlow.loginToHome(credentials)

        composeRule.onNodeWithTag(UiTestTags.BottomTabHome).assertIsDisplayed()
    }

    @Test
    fun settingFlow_opensLogoutDialogAndConfirmsLogout() {
        settingFlow.logoutFromSetting(credentials)
    }
}
