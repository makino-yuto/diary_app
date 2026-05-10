package com.makino.diary_app.model

enum class GoogleDriveSyncMode(
    val storageValue: String,
    val label: String
) {
    AutoOnSave("auto_on_save", "自動"),
    Manual("manual", "手動");

    companion object {
        fun fromStorageValue(value: String?): GoogleDriveSyncMode =
            entries.firstOrNull { it.storageValue == value } ?: AutoOnSave
    }
}
