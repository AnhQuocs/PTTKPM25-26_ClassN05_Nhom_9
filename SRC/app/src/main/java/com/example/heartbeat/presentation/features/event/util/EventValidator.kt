package com.example.heartbeat.presentation.features.event.util

import com.example.heartbeat.R

object EventValidator {

    fun isValidEventText(
        text: String,
        maxWords: Int,
        emptyError: Int,
        tooLongError: Int
    ): Int? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return emptyError
        }
        val wordCount = trimmed.split("\\s+".toRegex()).size
        if (wordCount > maxWords) {
            return tooLongError
        }
        return null
    }

    fun validateCapacity(capacity: String): Int? {
        val trimmed = capacity.trim()
        if (trimmed.isEmpty()) {
            return R.string.validate_capacity_empty
        }
        val number = trimmed.toIntOrNull() ?: return R.string.validate_capacity_empty
        if (number > 300) {
            return R.string.validate_capacity_exceed
        }
        return null
    }
}