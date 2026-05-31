package com.example.heartbeat.ui.common

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText

fun ComposeTestRule.waitUntilTagExists(
    tag: String,
    timeoutMillis: Long = 10_000
) {
    waitUntil(timeoutMillis) { hasTag(tag) }
}

fun ComposeTestRule.hasTag(tag: String): Boolean {
    return onAllNodesWithTag(tag, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
}

fun ComposeTestRule.hasText(text: String): Boolean {
    return onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
}
