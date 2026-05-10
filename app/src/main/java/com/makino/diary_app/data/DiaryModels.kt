package com.makino.diary_app.data

import java.time.LocalDate

data class DiaryEntry(
    val date: LocalDate,
    val prompt: String,
    val userText: String = "",
    val photoUris: List<String> = emptyList(),
    val photoStepCompleted: Boolean = false,
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    val isCompleted: Boolean
        get() = userText.isNotBlank() && photoStepCompleted
}

fun defaultPrompts(): List<String> = listOf(
    "今日のできごとを教えてください。",
)
