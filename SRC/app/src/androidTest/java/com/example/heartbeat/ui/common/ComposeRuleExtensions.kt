package com.example.heartbeat.ui.common

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput

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

fun ComposeTestRule.hasAnyText(vararg texts: String): Boolean {
    return texts.any { hasText(it) }
}

fun ComposeTestRule.waitUntilAnyTextExists(
    vararg texts: String,
    timeoutMillis: Long = 15_000
) {
    waitUntil(timeoutMillis) { hasAnyText(*texts) }
}

fun ComposeTestRule.hasTextContaining(text: String): Boolean {
    return onAllNodes(hasText(text, substring = true), useUnmergedTree = true)
        .fetchSemanticsNodes()
        .isNotEmpty()
}

fun ComposeTestRule.clickFirstText(vararg texts: String) {
    val node = texts.firstNotNullOfOrNull { text ->
        onAllNodesWithText(text, useUnmergedTree = true)
            .fetchSemanticsNodes()
            .firstOrNull()
            ?.let { onAllNodesWithText(text, useUnmergedTree = true)[0] }
    } ?: error("Could not find any of these texts: ${texts.joinToString()}")

    node.performClick()
}

fun ComposeTestRule.textInputCount(): Int {
    return onAllNodes(hasSetTextAction(), useUnmergedTree = true)
        .fetchSemanticsNodes()
        .size
}

fun ComposeTestRule.replaceTextInput(index: Int, text: String) {
    val input = onAllNodes(hasSetTextAction(), useUnmergedTree = true)[index]
    input.performTextClearance()
    input.performTextInput(text)
}
