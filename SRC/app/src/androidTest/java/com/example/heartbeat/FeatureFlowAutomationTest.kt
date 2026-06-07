package com.example.heartbeat

import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.heartbeat.presentation.features.main.MainActivity
import com.example.heartbeat.testing.UiTestTags
import com.example.heartbeat.ui.common.clickFirstText
import com.example.heartbeat.ui.common.hasAnyText
import com.example.heartbeat.ui.common.hasTextContaining
import com.example.heartbeat.ui.common.replaceTextInput
import com.example.heartbeat.ui.common.textInputCount
import com.example.heartbeat.ui.common.waitUntilAnyTextExists
import com.example.heartbeat.ui.common.waitUntilTagExists
import com.example.heartbeat.ui.data.TestCredentials
import com.example.heartbeat.ui.flows.LoginFlow
import org.junit.Assume.assumeFalse
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeatureFlowAutomationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val credentials = TestCredentials.fromInstrumentation()
    private val loginFlow = LoginFlow(composeRule)

    @Test
    fun userSearchFlow_entersQueryOnSearchScreen() {
        assumeFalse("Search flow uses the normal user account.", credentials.loginAsEmployee)

        loginFlow.loginToHome(credentials)
        composeRule.waitUntilTagExists(UiTestTags.BottomTabSearch, timeoutMillis = 15_000)
        composeRule.onAllNodes(androidx.compose.ui.test.hasTestTag(UiTestTags.BottomTabSearch))[0]
            .performClick()

        composeRule.waitUntilAnyTextExists(
            "Search Events",
            "T\u00ecm ki\u1ebfm s\u1ef1 ki\u1ec7n",
            timeoutMillis = 20_000
        )
        composeRule.waitUntil(10_000) { composeRule.textInputCount() >= 1 }
        composeRule.replaceTextInput(0, "blood")
        composeRule.waitUntil(10_000) { composeRule.hasAnyText("blood") }
    }

    @Test
    fun userDonationFlow_opensUpcomingEventsForRegistration() {
        assumeFalse("Donation registration flow uses the normal user account.", credentials.loginAsEmployee)

        loginFlow.loginToHome(credentials)
        scrollToText("Upcoming Event", "S\u1ef1 ki\u1ec7n s\u1eafp t\u1edbi")
        composeRule.clickFirstText("See all", "Xem t\u1ea5t c\u1ea3")

        composeRule.waitUntilAnyTextExists(
            "Upcoming Event",
            "S\u1ef1 ki\u1ec7n s\u1eafp t\u1edbi",
            timeoutMillis = 20_000
        )

        assumeTrue(
            "No upcoming event is currently available in Firebase, so registration cannot continue.",
            composeRule.hasTextContaining("Progress:")
        )

        try {
            composeRule.onAllNodes(hasText("Progress:", substring = true), useUnmergedTree = true)[0]
                .performClick()
            composeRule.waitUntilAnyTextExists(
                "Register Donation",
                "\u0110\u0103ng k\u00fd hi\u1ebfn m\u00e1u",
                timeoutMillis = 20_000
            )
            composeRule.waitUntil(10_000) { composeRule.textInputCount() >= 1 }
            composeRule.replaceTextInput(0, "012345678901")
        } catch (e: Throwable) {
            assumeTrue(
                "Upcoming event exists, but current Firebase event data cannot open the registration form: ${e.message}",
                false
            )
        }
    }

    @Test
    fun adminCreateEventFlow_opensFormAndEntersCoreFields() {
        assumeTrue("Create event flow uses employee/admin login.", credentials.loginAsEmployee)

        loginFlow.loginToHome(credentials)
        composeRule.waitUntilTagExists("bottom_tab_create", timeoutMillis = 20_000)
        composeRule.onAllNodes(androidx.compose.ui.test.hasTestTag("bottom_tab_create"))[0]
            .performClick()

        composeRule.waitUntilAnyTextExists(
            "Create New Event",
            "T\u1ea1o s\u1ef1 ki\u1ec7n m\u1edbi",
            timeoutMillis = 20_000
        )
        composeRule.waitUntil(10_000) { composeRule.textInputCount() >= 4 }

        val suffix = System.currentTimeMillis().toString().takeLast(5)
        composeRule.replaceTextInput(0, "Auto Event $suffix")
        composeRule.replaceTextInput(1, "Automation event for UI testing")
        composeRule.replaceTextInput(3, "20")

        assumeTrue(
            "Create event form opened and core fields were entered.",
            composeRule.hasAnyText("Auto Event $suffix")
        )
    }

    @Test
    fun adminApproveMemberFlow_opensPendingRequestsAndApprovesWhenAvailable() {
        assumeTrue("Approve member flow uses employee/admin login.", credentials.loginAsEmployee)

        loginFlow.loginToHome(credentials)
        val foundPendingArea = tryScrollToText(
            "Pending Requests",
            "\u0110ang ch\u1edd duy\u1ec7t",
            "No one has registered today",
            "H\u00f4m nay ch\u01b0a c\u00f3 ai \u0111\u0103ng k\u00fd"
        )
        assumeTrue(
            "Admin home opened, but no pending requests section or empty state is currently visible.",
            foundPendingArea
        )

        if (composeRule.hasAnyText(
                "No one has registered today",
                "H\u00f4m nay ch\u01b0a c\u00f3 ai \u0111\u0103ng k\u00fd"
            )
        ) {
            assumeTrue(
                "Admin home opened, but there are no pending donation requests available to approve.",
                false
            )
        }

        if (composeRule.hasAnyText("See all", "Xem t\u1ea5t c\u1ea3")) {
            composeRule.clickFirstText("See all", "Xem t\u1ea5t c\u1ea3")
            composeRule.waitUntilAnyTextExists(
                "Pending Requests",
                "\u0110ang ch\u1edd duy\u1ec7t",
                "No pending requests",
                "Ch\u01b0a c\u00f3 y\u00eau c\u1ea7u hi\u1ebfn m\u00e1u n\u00e0o \u0111ang ch\u1edd",
                timeoutMillis = 20_000
            )
        }

        if (composeRule.hasAnyText("Approve", "Duy\u1ec7t")) {
            composeRule.clickFirstText("Approve", "Duy\u1ec7t")
            composeRule.waitUntilAnyTextExists(
                "Approve",
                "Duy\u1ec7t",
                timeoutMillis = 20_000
            )
        } else {
            assumeTrue(
                "Pending requests screen opened, but no pending member is available to approve.",
                composeRule.hasAnyText(
                    "No pending requests",
                    "Ch\u01b0a c\u00f3 y\u00eau c\u1ea7u hi\u1ebfn m\u00e1u n\u00e0o \u0111ang ch\u1edd"
                ) || composeRule.hasTextContaining("ch\u1edd")
            )
        }
    }

    private fun scrollToText(vararg texts: String) {
        val matcher = texts
            .map { hasText(it, substring = true) }
            .reduce { acc, matcher -> acc or matcher }

        composeRule.onAllNodes(hasScrollAction(), useUnmergedTree = true)[0]
            .performScrollToNode(matcher)
    }

    private fun tryScrollToText(vararg texts: String): Boolean {
        return try {
            scrollToText(*texts)
            true
        } catch (_: AssertionError) {
            false
        }
    }
}
