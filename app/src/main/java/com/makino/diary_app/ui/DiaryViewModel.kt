package com.makino.diary_app.ui

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makino.diary_app.data.DiaryEntry
import com.makino.diary_app.data.DiaryRepository
import com.makino.diary_app.model.AppThemePreset
import com.makino.diary_app.notifications.DiaryReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

data class DiaryUiState(
    val isLoading: Boolean = true,
    val entries: List<DiaryEntry> = emptyList(),
    val visibleMonth: YearMonth = YearMonth.now(),
    val themePreset: AppThemePreset = AppThemePreset.EcruBeige,
    val reminderTimes: List<LocalTime> = emptyList(),
    val hasSeenReminderPrompt: Boolean = false,
    val hasCompletedOnboarding: Boolean = false
) {
    fun entryFor(date: LocalDate): DiaryEntry? = entries.firstOrNull { it.date == date }
    fun hasCompletedEntry(date: LocalDate): Boolean = entryFor(date)?.isCompleted == true
}

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DiaryRepository(application)
    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.update {
            it.copy(
                isLoading = false,
                entries = repository.loadEntries().sortedByDescending(DiaryEntry::date),
                themePreset = repository.loadThemePreset(),
                reminderTimes = repository.loadReminderTimes(),
                hasSeenReminderPrompt = repository.hasSeenReminderPrompt(),
                hasCompletedOnboarding = repository.hasCompletedOnboarding()
            )
        }
    }

    fun setThemePreset(themePreset: AppThemePreset) {
        repository.saveThemePreset(themePreset)
        _uiState.update { it.copy(themePreset = themePreset) }
    }

    fun addReminderTime(hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        val updatedTimes = (repository.loadReminderTimes() + time).distinct().sorted()
        repository.saveReminderTimes(updatedTimes)
        DiaryReminderScheduler.scheduleReminders(getApplication(), updatedTimes)
        _uiState.update {
            it.copy(
                reminderTimes = updatedTimes,
                hasSeenReminderPrompt = true
            )
        }
    }

    fun removeReminderTime(time: LocalTime) {
        val updatedTimes = repository.loadReminderTimes().filterNot { it == time }
        repository.saveReminderTimes(updatedTimes)
        DiaryReminderScheduler.scheduleReminders(getApplication(), updatedTimes)
        _uiState.update { it.copy(reminderTimes = updatedTimes) }
    }

    fun markReminderPromptSeen() {
        repository.markReminderPromptSeen()
        _uiState.update { it.copy(hasSeenReminderPrompt = true) }
    }

    fun completeOnboarding() {
        repository.markOnboardingCompleted()
        repository.markReminderPromptSeen()
        _uiState.update {
            it.copy(
                hasCompletedOnboarding = true,
                hasSeenReminderPrompt = true
            )
        }
    }

    fun ensureTodayDraft() {
        repository.ensureDraft(LocalDate.now())
        refresh()
    }

    fun ensureDraft(date: LocalDate) {
        repository.ensureDraft(date)
        refresh()
    }

    fun saveTodayText(text: String) {
        repository.saveText(LocalDate.now(), text)
        refresh()
    }

    fun saveDiaryText(date: LocalDate, text: String) {
        repository.saveText(date, text, markPhotoStepCompleted = true)
        refresh()
    }

    fun saveTodayPhotos(
        contentResolver: ContentResolver,
        photoUris: List<Uri>,
        markPhotoStepCompleted: Boolean = true
    ) {
        savePhotos(LocalDate.now(), contentResolver, photoUris, markPhotoStepCompleted)
    }

    fun savePhotos(
        date: LocalDate,
        contentResolver: ContentResolver,
        photoUris: List<Uri>,
        markPhotoStepCompleted: Boolean = true
    ) {
        photoUris.forEach { uri ->
            runCatching {
                contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
        repository.savePhotos(date, photoUris, markPhotoStepCompleted)
        refresh()
    }

    fun completeTodayPhotoStep() {
        repository.completePhotoStep(LocalDate.now())
        refresh()
    }

    fun removePhoto(date: LocalDate, photoUri: String) {
        repository.removePhoto(date, photoUri)
        refresh()
    }

    fun markTodayNoPhotos() {
        repository.markNoPhotos(LocalDate.now())
        refresh()
    }

    fun updateMonth(month: YearMonth) {
        _uiState.update { it.copy(visibleMonth = month) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DiaryViewModel(application) as T
        }
    }
}
