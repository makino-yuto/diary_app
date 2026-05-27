package com.makino.diary_app.data

import java.time.LocalDate

data class DiaryEntry(
    val date: LocalDate,
    val prompt: String,
    val userText: String = "",
    val photoUris: List<String> = emptyList(),
    val mediaItems: List<DiaryMediaItem> = emptyList(),
    val photoStepCompleted: Boolean = false,
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    val isCompleted: Boolean
        get() = userText.isNotBlank() && photoStepCompleted
}

data class DiaryMediaItem(
    val originalUri: String? = null,
    val mediaStoreId: Long? = null,
    val volumeName: String? = null,
    val displayName: String? = null,
    val relativePath: String? = null,
    val mimeType: String? = null,
    val sizeBytes: Long? = null,
    val dateAddedSeconds: Long? = null
) {
    val stableKey: String
        get() = buildString {
            append(mediaStoreId ?: "no-id")
            append('|')
            append(volumeName.orEmpty())
            append('|')
            append(relativePath.orEmpty())
            append('|')
            append(displayName.orEmpty())
            append('|')
            append(sizeBytes ?: -1L)
        }
}

fun defaultPrompts(): List<String> = listOf(
    "今日のできごとを教えてください。",
)
