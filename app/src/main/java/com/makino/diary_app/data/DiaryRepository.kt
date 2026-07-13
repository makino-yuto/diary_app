package com.makino.diary_app.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.makino.diary_app.model.AppThemePreset
import com.makino.diary_app.model.GoogleDriveSyncMode
import com.makino.diary_app.ui.theme.DEFAULT_THEME_INTENSITY
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalTime
import java.util.Base64

class DiaryRepository(context: Context) {
    private val appContext = context.applicationContext
    private val contentResolver = appContext.contentResolver
    private val prefs = appContext.getSharedPreferences("diary_prefs", Context.MODE_PRIVATE)

    fun loadEntries(): List<DiaryEntry> {
        val raw = prefs.getString(KEY_ENTRIES, null) ?: return emptyList()
        val json = JSONArray(raw)
        return buildList {
            for (index in 0 until json.length()) {
                add(json.getJSONObject(index).toDiaryEntry())
            }
        }.sortedByDescending { it.date }
    }

    fun hasStoredLocalMediaReferences(): Boolean =
        loadEntries().any { entry -> entry.mediaItems.isNotEmpty() }

    fun loadThemePreset(): AppThemePreset =
        AppThemePreset.fromStorageValue(prefs.getString(KEY_THEME_PRESET, null))

    fun saveThemePreset(themePreset: AppThemePreset) {
        prefs.edit()
            .putString(KEY_THEME_PRESET, themePreset.storageValue)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun loadThemeIntensity(): Float = DEFAULT_THEME_INTENSITY

    fun saveThemeIntensity(themeIntensity: Float) {
        prefs.edit()
            .putFloat(KEY_THEME_INTENSITY, DEFAULT_THEME_INTENSITY)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun loadBackupTreeUri(): String? =
        prefs.getString(KEY_BACKUP_TREE_URI, null)

    fun saveBackupTreeUri(uri: String) {
        prefs.edit().putString(KEY_BACKUP_TREE_URI, uri).apply()
    }

    fun loadBackupAccountEmail(): String? =
        prefs.getString(KEY_BACKUP_ACCOUNT_EMAIL, null)

    fun saveBackupAccountEmail(email: String?) {
        prefs.edit().putString(KEY_BACKUP_ACCOUNT_EMAIL, email).apply()
    }

    fun loadGoogleDriveSyncMode(): GoogleDriveSyncMode =
        GoogleDriveSyncMode.fromStorageValue(prefs.getString(KEY_GOOGLE_DRIVE_SYNC_MODE, null))

    fun saveGoogleDriveSyncMode(mode: GoogleDriveSyncMode) {
        prefs.edit().putString(KEY_GOOGLE_DRIVE_SYNC_MODE, mode.storageValue).apply()
    }

    fun isGoogleDriveLinked(): Boolean =
        prefs.getBoolean(KEY_GOOGLE_DRIVE_LINKED, false)

    fun saveGoogleDriveLinked(linked: Boolean) {
        prefs.edit().putBoolean(KEY_GOOGLE_DRIVE_LINKED, linked).apply()
    }

    fun disconnectGoogleDrive() {
        prefs.edit()
            .remove(KEY_BACKUP_ACCOUNT_EMAIL)
            .putBoolean(KEY_GOOGLE_DRIVE_LINKED, false)
            .putLong(KEY_LAST_CLOUD_SYNC_DIARY_UPDATED_AT_MILLIS, 0L)
            .apply()
    }

    fun isNotificationsEnabled(): Boolean =
        prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)

    fun saveNotificationsEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun isChatEnabled(): Boolean =
        prefs.getBoolean(KEY_CHAT_ENABLED, true)

    fun saveChatEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_CHAT_ENABLED, enabled)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun isFingerprintAuthEnabled(): Boolean =
        prefs.getBoolean(KEY_FINGERPRINT_AUTH_ENABLED, false)

    fun saveFingerprintAuthEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_FINGERPRINT_AUTH_ENABLED, enabled)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun isPasswordAuthEnabled(): Boolean =
        prefs.getBoolean(KEY_PASSWORD_AUTH_ENABLED, false)

    fun savePasswordAuthEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_PASSWORD_AUTH_ENABLED, enabled)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun hasPasswordCredential(): Boolean =
        prefs.contains(KEY_PASSWORD_HASH) && prefs.contains(KEY_PASSWORD_SALT)

    fun savePasswordCredential(password: String) {
        val saltBytes = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val salt = Base64.getEncoder().encodeToString(saltBytes)
        val hash = hashPassword(password, saltBytes)
        prefs.edit()
            .putString(KEY_PASSWORD_SALT, salt)
            .putString(KEY_PASSWORD_HASH, hash)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun verifyPassword(password: String): Boolean {
        val salt = prefs.getString(KEY_PASSWORD_SALT, null) ?: return false
        val storedHash = prefs.getString(KEY_PASSWORD_HASH, null) ?: return false
        val saltBytes = runCatching { Base64.getDecoder().decode(salt) }.getOrNull() ?: return false
        return hashPassword(password, saltBytes) == storedHash
    }

    fun loadReminderTimes(): List<LocalTime> {
        val array = prefs.getString(KEY_REMINDER_TIMES, null)?.let(::JSONArray)
        if (array != null) {
            return buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    add(LocalTime.of(item.getInt("hour"), item.getInt("minute")))
                }
            }.distinct().sorted()
        }

        if (!prefs.contains(KEY_REMINDER_HOUR) || !prefs.contains(KEY_REMINDER_MINUTE)) {
            return emptyList()
        }
        val hour = prefs.getInt(KEY_REMINDER_HOUR, 21)
        val minute = prefs.getInt(KEY_REMINDER_MINUTE, 0)
        return listOf(LocalTime.of(hour, minute))
    }

    fun saveReminderTimes(times: List<LocalTime>) {
        val json = JSONArray()
        times.distinct().sorted().forEach { time ->
            json.put(
                JSONObject()
                    .put("hour", time.hour)
                    .put("minute", time.minute)
            )
        }
        prefs.edit()
            .putString(KEY_REMINDER_TIMES, json.toString())
            .putBoolean(KEY_REMINDER_PROMPT_SHOWN, true)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun hasSeenReminderPrompt(): Boolean =
        prefs.getBoolean(KEY_REMINDER_PROMPT_SHOWN, false)

    fun markReminderPromptSeen() {
        prefs.edit()
            .putBoolean(KEY_REMINDER_PROMPT_SHOWN, true)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun hasCompletedOnboarding(): Boolean =
        prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun markOnboardingCompleted() {
        prefs.edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, true)
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .apply()
    }

    fun clearAllData() {
        prefs.edit()
            .clear()
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
            .putLong(KEY_DIARY_UPDATED_AT_MILLIS, 0L)
            .putLong(KEY_LAST_CLOUD_SYNC_DIARY_UPDATED_AT_MILLIS, 0L)
            .apply()
    }

    fun loadDataUpdatedAtMillis(): Long =
        prefs.getLong(KEY_DATA_UPDATED_AT_MILLIS, 0L)

    fun loadDiaryUpdatedAtMillis(): Long =
        prefs.getLong(KEY_DIARY_UPDATED_AT_MILLIS, 0L)
            .takeIf { it > 0L }
            ?: loadEntries().maxOfOrNull(DiaryEntry::updatedAtMillis)
            ?: 0L

    fun loadLastCloudSyncDiaryUpdatedAtMillis(): Long =
        prefs.getLong(KEY_LAST_CLOUD_SYNC_DIARY_UPDATED_AT_MILLIS, 0L)

    fun saveLastCloudSyncDiaryUpdatedAtMillis(updatedAtMillis: Long) {
        prefs.edit().putLong(KEY_LAST_CLOUD_SYNC_DIARY_UPDATED_AT_MILLIS, updatedAtMillis).apply()
    }

    fun exportBackupJson(): String {
        val entriesArray = JSONArray()
        loadEntries()
            .sortedBy { it.date }
            .forEach { entry -> entriesArray.put(ensureMediaItems(entry).toJson()) }

        return JSONObject()
            .put("version", 3)
            .put("entries", entriesArray)
            .put("diaryUpdatedAtMillis", loadDiaryUpdatedAtMillis())
            .put("exportedAtMillis", System.currentTimeMillis())
            .toString()
    }

    fun readBackupDataUpdatedAtMillis(json: String): Long =
        JSONObject(json).let { root ->
            root.optLong("diaryUpdatedAtMillis", 0L)
                .takeIf { it > 0L }
                ?: root.optLong("dataUpdatedAtMillis", 0L)
        }

    fun backupHasEntries(json: String): Boolean {
        val root = JSONObject(json)
        val entries = when {
            root.has("entries") -> root.optJSONArray("entries")
            else -> extractEntriesArrayFromLegacyBackup(root)
        }
        return entries?.length()?.let { it > 0 } == true
    }

    fun restoreBackupJson(json: String) {
        val root = JSONObject(json)
        val entriesJson = when {
            root.has("entries") -> root.optJSONArray("entries")
            else -> extractEntriesArrayFromLegacyBackup(root)
        } ?: throw IllegalArgumentException("entries not found")
        val updatedAtMillis = readBackupDataUpdatedAtMillis(json)
            .takeIf { it > 0L }
            ?: readEntriesUpdatedAtMillis(entriesJson)

        prefs.edit()
            .putString(KEY_ENTRIES, entriesJson.toString())
            .putLong(KEY_DIARY_UPDATED_AT_MILLIS, updatedAtMillis)
            .apply()
    }

    fun ensureDraft(date: LocalDate): DiaryEntry {
        val existing = getEntry(date)
        if (existing != null) {
            if (existing.prompt == FIXED_PROMPT) return existing
            val updated = existing.copy(prompt = FIXED_PROMPT)
            saveEntry(updated, updateDiaryTimestamp = false)
            return updated
        }
        val created = DiaryEntry(
            date = date,
            prompt = FIXED_PROMPT
        )
        saveEntry(created, updateDiaryTimestamp = false)
        return created
    }

    fun getEntry(date: LocalDate): DiaryEntry? = loadEntries().firstOrNull { it.date == date }

    fun saveText(date: LocalDate, text: String, markPhotoStepCompleted: Boolean = false): DiaryEntry {
        val current = ensureDraft(date)
        val normalizedText = text.trim()
        val updated = current.copy(
            userText = normalizedText,
            photoStepCompleted = if (normalizedText.isBlank()) {
                false
            } else {
                current.photoStepCompleted || markPhotoStepCompleted
            },
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    fun savePhotos(
        date: LocalDate,
        photoUris: List<Uri>,
        markPhotoStepCompleted: Boolean = true
    ): DiaryEntry {
        val current = ensureDraft(date)
        val currentWithMedia = ensureMediaItems(current)
        val newMediaItems = photoUris.map(::buildMediaItem)
        val mergedMediaItems = (currentWithMedia.mediaItems + newMediaItems)
            .distinctBy(DiaryMediaItem::stableKey)
        val updated = current.copy(
            photoUris = mergedMediaItems.mapNotNull { resolveMediaUri(it) ?: it.originalUri },
            mediaItems = mergedMediaItems,
            photoStepCompleted = if (currentWithMedia.userText.isBlank()) {
                false
            } else {
                currentWithMedia.photoStepCompleted || markPhotoStepCompleted
            },
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    fun completePhotoStep(date: LocalDate): DiaryEntry {
        val current = ensureDraft(date)
        val updated = current.copy(
            photoStepCompleted = current.userText.isNotBlank(),
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    fun removePhoto(date: LocalDate, photoUri: String): DiaryEntry {
        val current = ensureDraft(date)
        val removeIndex = current.photoUris.indexOfFirst { it == photoUri }
        val updated = current.copy(
            photoUris = current.photoUris.filterNot { it == photoUri },
            mediaItems = if (removeIndex in current.mediaItems.indices) {
                current.mediaItems.filterIndexed { index, _ -> index != removeIndex }
            } else {
                current.mediaItems.filterNot { it.originalUri == photoUri || resolveMediaUri(it) == photoUri }
            },
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    fun markNoPhotos(date: LocalDate): DiaryEntry {
        val current = ensureDraft(date)
        val updated = current.copy(
            photoUris = emptyList(),
            mediaItems = emptyList(),
            photoStepCompleted = true,
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    private fun saveEntry(entry: DiaryEntry, updateDiaryTimestamp: Boolean = true) {
        val entries = loadEntries().toMutableList()
        val existingIndex = entries.indexOfFirst { it.date == entry.date }
        if (existingIndex >= 0) {
            entries[existingIndex] = entry
        } else {
            entries += entry
        }

        val json = JSONArray()
        entries.sortedBy { it.date }.forEach { stored ->
            json.put(stored.toJson())
        }
        val editor = prefs.edit()
            .putString(KEY_ENTRIES, json.toString())
            .putLong(KEY_DATA_UPDATED_AT_MILLIS, System.currentTimeMillis())
        if (updateDiaryTimestamp) {
            editor.putLong(KEY_DIARY_UPDATED_AT_MILLIS, System.currentTimeMillis())
        }
        editor.apply()
    }

    private fun DiaryEntry.toJson(): JSONObject = JSONObject()
        .put("date", date.toString())
        .put("prompt", prompt)
        .put("userText", userText)
        .put("photoStepCompleted", photoStepCompleted)
        .put("updatedAtMillis", updatedAtMillis)
        .put("photoUris", JSONArray(photoUris))
        .put("mediaItems", JSONArray(mediaItems.map { it.toJson() }))

    private fun JSONObject.toDiaryEntry(): DiaryEntry {
        val userText = optString("userText").trim()
        val legacyPhotoUris = optJSONArray("photoUris")?.let { array ->
            buildList {
                for (index in 0 until array.length()) {
                    add(array.getString(index))
                }
            }
        } ?: emptyList()
        val mediaItems = optJSONArray("mediaItems")?.let { array ->
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    add(item.toDiaryMediaItem())
                }
            }
        }?.takeIf { it.isNotEmpty() }
            ?: legacyPhotoUris.map { uri -> buildMediaItem(Uri.parse(uri)) }
        val resolvedPhotoUris = mediaItems.mapNotNull { resolveMediaUri(it) ?: it.originalUri?.takeIf(::canAccessUri) }
        return DiaryEntry(
            date = LocalDate.parse(getString("date")),
            prompt = getString("prompt"),
            userText = userText,
            photoUris = resolvedPhotoUris,
            mediaItems = mediaItems,
            photoStepCompleted = if (userText.isBlank()) {
                false
            } else {
                optBoolean("photoStepCompleted", false)
            },
            updatedAtMillis = optLong("updatedAtMillis", System.currentTimeMillis())
        )
    }

    private fun ensureMediaItems(entry: DiaryEntry): DiaryEntry {
        if (entry.photoUris.isEmpty()) return entry.copy(mediaItems = emptyList())
        if (entry.mediaItems.size == entry.photoUris.size && entry.mediaItems.isNotEmpty()) {
            return entry.copy(
                photoUris = entry.mediaItems.mapNotNull { resolveMediaUri(it) ?: it.originalUri?.takeIf(::canAccessUri) }
            )
        }
        val rebuiltItems = entry.photoUris.map { uriString -> buildMediaItem(Uri.parse(uriString)) }
        return entry.copy(
            photoUris = rebuiltItems.mapNotNull { resolveMediaUri(it) ?: it.originalUri?.takeIf(::canAccessUri) },
            mediaItems = rebuiltItems
        )
    }

    private fun DiaryMediaItem.toJson(): JSONObject = JSONObject()
        .put("originalUri", originalUri)
        .put("mediaStoreId", mediaStoreId)
        .put("volumeName", volumeName)
        .put("displayName", displayName)
        .put("relativePath", relativePath)
        .put("mimeType", mimeType)
        .put("sizeBytes", sizeBytes)
        .put("dateAddedSeconds", dateAddedSeconds)

    private fun JSONObject.toDiaryMediaItem(): DiaryMediaItem = DiaryMediaItem(
        originalUri = optString("originalUri").takeIf { it.isNotBlank() },
        mediaStoreId = optLong("mediaStoreId").takeIf { has("mediaStoreId") && it > 0L },
        volumeName = optString("volumeName").takeIf { it.isNotBlank() },
        displayName = optString("displayName").takeIf { it.isNotBlank() },
        relativePath = optString("relativePath").takeIf { it.isNotBlank() },
        mimeType = optString("mimeType").takeIf { it.isNotBlank() },
        sizeBytes = optLong("sizeBytes").takeIf { has("sizeBytes") && it >= 0L },
        dateAddedSeconds = optLong("dateAddedSeconds").takeIf { has("dateAddedSeconds") && it >= 0L }
    )

    private fun buildMediaItem(uri: Uri): DiaryMediaItem {
        val projection = mutableListOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATE_ADDED
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                add(MediaStore.MediaColumns.RELATIVE_PATH)
                add(MediaStore.MediaColumns.VOLUME_NAME)
            }
        }.toTypedArray()

        return runCatching {
            contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (!cursor.moveToFirst()) return@use null
                DiaryMediaItem(
                    originalUri = uri.toString(),
                    mediaStoreId = cursor.longOrNull(MediaStore.MediaColumns._ID),
                    volumeName = cursor.stringOrNull(MediaStore.MediaColumns.VOLUME_NAME),
                    displayName = cursor.stringOrNull(MediaStore.MediaColumns.DISPLAY_NAME),
                    relativePath = cursor.stringOrNull(MediaStore.MediaColumns.RELATIVE_PATH),
                    mimeType = cursor.stringOrNull(MediaStore.MediaColumns.MIME_TYPE),
                    sizeBytes = cursor.longOrNull(MediaStore.MediaColumns.SIZE),
                    dateAddedSeconds = cursor.longOrNull(MediaStore.MediaColumns.DATE_ADDED)
                )
            }
        }.getOrNull() ?: DiaryMediaItem(originalUri = uri.toString())
    }

    private fun resolveMediaUri(item: DiaryMediaItem): String? {
        val directlyAccessible = item.originalUri?.takeIf(::canAccessUri)
        if (directlyAccessible != null) return directlyAccessible

        val byId = item.mediaStoreId?.let { mediaStoreId ->
            findMediaStoreUri(
                item = item,
                selection = "${MediaStore.MediaColumns._ID} = ?",
                selectionArgs = arrayOf(mediaStoreId.toString())
            )
        }
        if (byId != null) return byId

        if (item.displayName.isNullOrBlank()) return null
        val clauses = mutableListOf("${MediaStore.MediaColumns.DISPLAY_NAME} = ?")
        val args = mutableListOf(item.displayName)
        if (!item.relativePath.isNullOrBlank() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            clauses += "${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
            args += item.relativePath
        }
        if (!item.mimeType.isNullOrBlank()) {
            clauses += "${MediaStore.MediaColumns.MIME_TYPE} = ?"
            args += item.mimeType
        }
        if (item.sizeBytes != null && item.sizeBytes >= 0L) {
            clauses += "${MediaStore.MediaColumns.SIZE} = ?"
            args += item.sizeBytes.toString()
        }
        return findMediaStoreUri(
            item = item,
            selection = clauses.joinToString(" AND "),
            selectionArgs = args.toTypedArray()
        )
    }

    private fun findMediaStoreUri(
        item: DiaryMediaItem,
        selection: String,
        selectionArgs: Array<String>
    ): String? {
        val collection = MediaStore.Files.getContentUri(
            item.volumeName?.takeIf { it.isNotBlank() } ?: DEFAULT_MEDIA_VOLUME
        )
        return runCatching {
            contentResolver.query(
                collection,
                arrayOf(MediaStore.MediaColumns._ID),
                selection,
                selectionArgs,
                "${MediaStore.MediaColumns.DATE_ADDED} DESC"
            )?.use { cursor ->
                if (!cursor.moveToFirst()) return@use null
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                ContentUris.withAppendedId(collection, id).toString()
            }
        }.getOrNull()
    }

    private fun canAccessUri(uriString: String): Boolean {
        val uri = runCatching { Uri.parse(uriString) }.getOrNull() ?: return false
        return runCatching {
            contentResolver.query(uri, arrayOf(MediaStore.MediaColumns._ID), null, null, null)
                ?.use { it.moveToFirst() } == true
        }.getOrDefault(false)
    }

    private fun android.database.Cursor.stringOrNull(column: String): String? {
        val index = getColumnIndex(column)
        return if (index >= 0 && !isNull(index)) getString(index) else null
    }

    private fun android.database.Cursor.longOrNull(column: String): Long? {
        val index = getColumnIndex(column)
        return if (index >= 0 && !isNull(index)) getLong(index) else null
    }

    private fun extractEntriesArrayFromLegacyBackup(root: JSONObject): JSONArray? {
        val preferenceArray = root.optJSONArray("preferences") ?: return null
        for (index in 0 until preferenceArray.length()) {
            val item = preferenceArray.optJSONObject(index) ?: continue
            if (item.optString("key") != KEY_ENTRIES) continue
            val rawEntries = item.optString("value")
            if (rawEntries.isBlank()) return JSONArray()
            return JSONArray(rawEntries)
        }
        return null
    }

    private fun readEntriesUpdatedAtMillis(entriesArray: JSONArray): Long =
        buildList {
            for (index in 0 until entriesArray.length()) {
                val item = entriesArray.optJSONObject(index) ?: continue
                add(item.optLong("updatedAtMillis", 0L))
            }
        }.maxOrNull() ?: 0L

    private companion object {
        const val DEFAULT_MEDIA_VOLUME = "external"
        const val FIXED_PROMPT = "\u4eca\u65e5\u306f\u3069\u3093\u306a\u4e00\u65e5\u3067\u3057\u305f\u304b\uff1f"
        const val KEY_ENTRIES = "entries"
        const val KEY_THEME_PRESET = "theme_preset"
        const val KEY_THEME_INTENSITY = "theme_intensity"
        const val KEY_BACKUP_TREE_URI = "backup_tree_uri"
        const val KEY_BACKUP_ACCOUNT_EMAIL = "backup_account_email"
        const val KEY_GOOGLE_DRIVE_LINKED = "google_drive_linked"
        const val KEY_GOOGLE_DRIVE_SYNC_MODE = "google_drive_sync_mode"
        const val KEY_DATA_UPDATED_AT_MILLIS = "data_updated_at_millis"
        const val KEY_DIARY_UPDATED_AT_MILLIS = "diary_updated_at_millis"
        const val KEY_LAST_CLOUD_SYNC_DIARY_UPDATED_AT_MILLIS = "last_cloud_sync_diary_updated_at_millis"
        const val KEY_REMINDER_TIMES = "reminder_times"
        const val KEY_REMINDER_HOUR = "reminder_hour"
        const val KEY_REMINDER_MINUTE = "reminder_minute"
        const val KEY_REMINDER_PROMPT_SHOWN = "reminder_prompt_shown"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val KEY_CHAT_ENABLED = "chat_enabled"
        const val KEY_FINGERPRINT_AUTH_ENABLED = "fingerprint_auth_enabled"
        const val KEY_PASSWORD_AUTH_ENABLED = "password_auth_enabled"
        const val KEY_PASSWORD_HASH = "password_hash"
        const val KEY_PASSWORD_SALT = "password_salt"
    }

    private fun hashPassword(password: String, saltBytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(saltBytes)
        return digest.digest(password.toByteArray(Charsets.UTF_8))
            .joinToString(separator = "") { "%02x".format(it) }
    }
}
