package com.makino.diary_app.data

import android.content.Context
import android.net.Uri
import com.makino.diary_app.model.AppThemePreset
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

class DiaryRepository(context: Context) {
    private val appContext = context.applicationContext
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

    fun loadThemePreset(): AppThemePreset =
        AppThemePreset.fromStorageValue(prefs.getString(KEY_THEME_PRESET, null))

    fun saveThemePreset(themePreset: AppThemePreset) {
        prefs.edit().putString(KEY_THEME_PRESET, themePreset.storageValue).apply()
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
            .apply()
    }

    fun hasSeenReminderPrompt(): Boolean =
        prefs.getBoolean(KEY_REMINDER_PROMPT_SHOWN, false)

    fun markReminderPromptSeen() {
        prefs.edit().putBoolean(KEY_REMINDER_PROMPT_SHOWN, true).apply()
    }

    fun hasCompletedOnboarding(): Boolean =
        prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun markOnboardingCompleted() {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
    }

    fun ensureDraft(date: LocalDate): DiaryEntry {
        val existing = getEntry(date)
        if (existing != null) {
            if (existing.prompt == FIXED_PROMPT) return existing
            val updated = existing.copy(prompt = FIXED_PROMPT)
            saveEntry(updated)
            return updated
        }
        val created = DiaryEntry(
            date = date,
            prompt = FIXED_PROMPT
        )
        saveEntry(created)
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
        val updated = current.copy(
            photoUris = (current.photoUris + photoUris.map(Uri::toString)).distinct(),
            photoStepCompleted = if (current.userText.isBlank()) {
                false
            } else {
                current.photoStepCompleted || markPhotoStepCompleted
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
        val updated = current.copy(
            photoUris = current.photoUris.filterNot { it == photoUri },
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    fun markNoPhotos(date: LocalDate): DiaryEntry {
        val current = ensureDraft(date)
        val updated = current.copy(
            photoUris = emptyList(),
            photoStepCompleted = true,
            updatedAtMillis = System.currentTimeMillis()
        )
        saveEntry(updated)
        return updated
    }

    private fun saveEntry(entry: DiaryEntry) {
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
        prefs.edit().putString(KEY_ENTRIES, json.toString()).apply()
    }

    private fun DiaryEntry.toJson(): JSONObject = JSONObject()
        .put("date", date.toString())
        .put("prompt", prompt)
        .put("userText", userText)
        .put("photoStepCompleted", photoStepCompleted)
        .put("updatedAtMillis", updatedAtMillis)
        .put("photoUris", JSONArray(photoUris))

    private fun JSONObject.toDiaryEntry(): DiaryEntry {
        val userText = optString("userText").trim()
        return DiaryEntry(
            date = LocalDate.parse(getString("date")),
            prompt = getString("prompt"),
            userText = userText,
            photoUris = optJSONArray("photoUris")?.let { array ->
                buildList {
                    for (index in 0 until array.length()) {
                        add(array.getString(index))
                    }
                }
            } ?: emptyList(),
            photoStepCompleted = if (userText.isBlank()) {
                false
            } else {
                optBoolean("photoStepCompleted", false)
            },
            updatedAtMillis = optLong("updatedAtMillis", System.currentTimeMillis())
        )
    }

    private companion object {
        const val FIXED_PROMPT = "\u4eca\u65e5\u306f\u3069\u3093\u306a\u4e00\u65e5\u3067\u3057\u305f\u304b\uff1f"
        const val KEY_ENTRIES = "entries"
        const val KEY_THEME_PRESET = "theme_preset"
        const val KEY_REMINDER_TIMES = "reminder_times"
        const val KEY_REMINDER_HOUR = "reminder_hour"
        const val KEY_REMINDER_MINUTE = "reminder_minute"
        const val KEY_REMINDER_PROMPT_SHOWN = "reminder_prompt_shown"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
