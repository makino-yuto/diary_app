package com.makino.diary_app.ui

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.biometric.BiometricManager
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makino.diary_app.data.DiaryEntry
import com.makino.diary_app.data.DiaryRepository
import com.makino.diary_app.data.GoogleDriveBackupService
import com.makino.diary_app.model.AppThemePreset
import com.makino.diary_app.model.GoogleDriveSyncMode
import com.makino.diary_app.notifications.DiaryReminderScheduler
import com.makino.diary_app.ui.theme.DEFAULT_THEME_INTENSITY
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiaryUiState(
    val isLoading: Boolean = true,
    val entries: List<DiaryEntry> = emptyList(),
    val visibleMonth: YearMonth = YearMonth.now(),
    val themePreset: AppThemePreset = AppThemePreset.White,
    val themeIntensity: Float = DEFAULT_THEME_INTENSITY,
    val reminderTimes: List<LocalTime> = emptyList(),
    val notificationsEnabled: Boolean = true,
    val fingerprintAuthEnabled: Boolean = false,
    val passwordAuthEnabled: Boolean = false,
    val hasPasswordCredential: Boolean = false,
    val hasBackupFolder: Boolean = false,
    val backupAccountEmail: String? = null,
    val isGoogleDriveLinked: Boolean = false,
    val googleDriveSyncMode: GoogleDriveSyncMode = GoogleDriveSyncMode.AutoOnSave,
    val diaryUpdatedAtMillis: Long = 0L,
    val lastCloudSyncDiaryUpdatedAtMillis: Long = 0L,
    val isSecurityUnlocked: Boolean = true,
    val hasSeenReminderPrompt: Boolean = false,
    val hasCompletedOnboarding: Boolean = false
) {
    fun entryFor(date: LocalDate): DiaryEntry? = entries.firstOrNull { it.date == date }
    fun hasCompletedEntry(date: LocalDate): Boolean = entryFor(date)?.isCompleted == true
}

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DiaryRepository(application)
    private val googleDriveBackupService = GoogleDriveBackupService()
    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()
    private var hasInitializedSecurityState = false

    init {
        refresh()
    }

    fun refresh() {
        var passwordAuthEnabled = repository.isPasswordAuthEnabled()
        val hasPasswordCredential = repository.hasPasswordCredential()
        if (passwordAuthEnabled && !hasPasswordCredential) {
            repository.savePasswordAuthEnabled(false)
            passwordAuthEnabled = false
        }

        var fingerprintAuthEnabled = repository.isFingerprintAuthEnabled()
        if (fingerprintAuthEnabled && !canUseBiometric(getApplication())) {
            repository.saveFingerprintAuthEnabled(false)
            fingerprintAuthEnabled = false
        }

        val requiresSecurity = fingerprintAuthEnabled || passwordAuthEnabled
        _uiState.update {
            it.copy(
                isLoading = false,
                entries = repository.loadEntries().sortedByDescending(DiaryEntry::date),
                themePreset = repository.loadThemePreset(),
                themeIntensity = repository.loadThemeIntensity(),
                reminderTimes = repository.loadReminderTimes(),
                notificationsEnabled = repository.isNotificationsEnabled(),
                fingerprintAuthEnabled = fingerprintAuthEnabled,
                passwordAuthEnabled = passwordAuthEnabled,
                hasPasswordCredential = hasPasswordCredential,
                hasBackupFolder = !repository.loadBackupTreeUri().isNullOrBlank(),
                backupAccountEmail = repository.loadBackupAccountEmail(),
                isGoogleDriveLinked = repository.isGoogleDriveLinked(),
                googleDriveSyncMode = repository.loadGoogleDriveSyncMode(),
                diaryUpdatedAtMillis = repository.loadDiaryUpdatedAtMillis(),
                lastCloudSyncDiaryUpdatedAtMillis = repository.loadLastCloudSyncDiaryUpdatedAtMillis(),
                isSecurityUnlocked = when {
                    !requiresSecurity -> true
                    !hasInitializedSecurityState -> false
                    else -> it.isSecurityUnlocked
                },
                hasSeenReminderPrompt = repository.hasSeenReminderPrompt(),
                hasCompletedOnboarding = repository.hasCompletedOnboarding()
            )
        }
        hasInitializedSecurityState = true
    }

    fun setThemePreset(themePreset: AppThemePreset) {
        repository.saveThemePreset(themePreset)
        _uiState.update { it.copy(themePreset = themePreset) }
    }

    fun setThemeIntensity(themeIntensity: Float) {
        repository.saveThemeIntensity(DEFAULT_THEME_INTENSITY)
        _uiState.update { it.copy(themeIntensity = DEFAULT_THEME_INTENSITY) }
    }

    fun addReminderTime(hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        val updatedTimes = (repository.loadReminderTimes() + time).distinct().sorted()
        repository.saveReminderTimes(updatedTimes)
        DiaryReminderScheduler.scheduleReminders(
            getApplication(),
            if (repository.isNotificationsEnabled()) updatedTimes else emptyList()
        )
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
        DiaryReminderScheduler.scheduleReminders(
            getApplication(),
            if (repository.isNotificationsEnabled()) updatedTimes else emptyList()
        )
        _uiState.update { it.copy(reminderTimes = updatedTimes) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        repository.saveNotificationsEnabled(enabled)
        DiaryReminderScheduler.scheduleReminders(
            getApplication(),
            if (enabled) repository.loadReminderTimes() else emptyList()
        )
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun saveBackupAccountLabel(label: String?) {
        repository.saveBackupAccountEmail(label)
        refresh()
    }

    fun setGoogleDriveSyncMode(mode: GoogleDriveSyncMode) {
        repository.saveGoogleDriveSyncMode(mode)
        refresh()
    }

    fun disconnectGoogleDrive() {
        repository.disconnectGoogleDrive()
        refresh()
    }

    fun setFingerprintAuthEnabled(enabled: Boolean) {
        repository.saveFingerprintAuthEnabled(enabled)
        _uiState.update {
            it.copy(
                fingerprintAuthEnabled = enabled,
                isSecurityUnlocked = if (enabled || it.passwordAuthEnabled) it.isSecurityUnlocked else true
            )
        }
    }

    fun setPasswordAuthEnabled(enabled: Boolean) {
        repository.savePasswordAuthEnabled(enabled)
        _uiState.update {
            it.copy(
                passwordAuthEnabled = enabled,
                isSecurityUnlocked = if (enabled || it.fingerprintAuthEnabled) it.isSecurityUnlocked else true
            )
        }
    }

    fun savePasswordCredential(password: String) {
        repository.savePasswordCredential(password)
        _uiState.update { it.copy(hasPasswordCredential = true) }
    }

    fun verifyPassword(password: String): Boolean = repository.verifyPassword(password)

    fun unlockSecurity() {
        _uiState.update { it.copy(isSecurityUnlocked = true) }
    }

    fun clearAllData() {
        repository.clearAllData()
        DiaryReminderScheduler.scheduleReminders(getApplication(), emptyList())
        refresh()
    }

    fun setBackupFolderUri(uri: Uri) {
        repository.saveBackupTreeUri(uri.toString())
        _uiState.update { it.copy(hasBackupFolder = true) }
    }

    fun exportBackup(contentResolver: ContentResolver, treeUri: Uri? = null): Result<Unit> = runCatching {
        val backupFile = resolveBackupFile(contentResolver, treeUri, createIfMissing = true)
        contentResolver.openOutputStream(backupFile.uri, "wt")?.bufferedWriter(Charsets.UTF_8).use { writer ->
            checkNotNull(writer) { "backup output is unavailable" }
            writer.write(repository.exportBackupJson())
            writer.flush()
        }
    }

    fun restoreBackup(contentResolver: ContentResolver, treeUri: Uri? = null): Result<Unit> = runCatching {
        val backupFile = resolveBackupFile(contentResolver, treeUri, createIfMissing = false)
        val json = contentResolver.openInputStream(backupFile.uri)?.bufferedReader(Charsets.UTF_8).use { reader ->
            checkNotNull(reader) { "backup input is unavailable" }
            reader.readText()
        }
        repository.restoreBackupJson(json)
        DiaryReminderScheduler.scheduleReminders(
            getApplication(),
            if (repository.isNotificationsEnabled()) repository.loadReminderTimes() else emptyList()
        )
        refresh()
    }

    fun restoreFromGoogleDrive(
        accessToken: String,
        accountEmail: String?
    ): Result<Unit> {
        val resolvedAccountLabel = resolveAccountLabel(accessToken, accountEmail)
        repository.saveBackupAccountEmail(resolvedAccountLabel)

        val result = runCatching {
            val localUpdatedAt = repository.loadDiaryUpdatedAtMillis()
            val localEntries = repository.loadEntries()
            val remoteBackupJson = googleDriveBackupService.downloadBackupOrNull(accessToken)
            val remoteUpdatedAt = remoteBackupJson?.let(repository::readBackupDataUpdatedAtMillis) ?: 0L

            if (
                remoteBackupJson != null &&
                (
                    remoteUpdatedAt > localUpdatedAt ||
                        (localEntries.isEmpty() && repository.backupHasEntries(remoteBackupJson))
                    )
            ) {
                repository.restoreBackupJson(remoteBackupJson)
            }

            repository.saveBackupAccountEmail(resolvedAccountLabel)
            repository.saveGoogleDriveLinked(true)
            repository.saveLastCloudSyncDiaryUpdatedAtMillis(remoteUpdatedAt)
            DiaryReminderScheduler.scheduleReminders(
                getApplication(),
                if (repository.isNotificationsEnabled()) repository.loadReminderTimes() else emptyList()
            )
        }
        refresh()
        return result
    }

    fun uploadDiaryToGoogleDrive(
        accessToken: String,
        accountEmail: String?
    ): Result<Unit> {
        val resolvedAccountLabel = resolveAccountLabel(accessToken, accountEmail)
        repository.saveBackupAccountEmail(resolvedAccountLabel)

        val result = runCatching {
            val backupPayload = repository.exportBackupJson()
            val uploadedDiaryUpdatedAtMillis = repository.readBackupDataUpdatedAtMillis(backupPayload)
            googleDriveBackupService.uploadBackup(accessToken, backupPayload)
            repository.saveBackupAccountEmail(resolvedAccountLabel)
            repository.saveGoogleDriveLinked(true)
            repository.saveLastCloudSyncDiaryUpdatedAtMillis(uploadedDiaryUpdatedAtMillis)
        }
        refresh()
        return result
    }

    fun manualSyncWithGoogleDrive(
        accessToken: String,
        accountEmail: String?
    ): Result<Unit> {
        val resolvedAccountLabel = resolveAccountLabel(accessToken, accountEmail)
        repository.saveBackupAccountEmail(resolvedAccountLabel)

        val result = runCatching {
            val localUpdatedAt = repository.loadDiaryUpdatedAtMillis()
            val localEntries = repository.loadEntries()
            val remoteBackupJson = googleDriveBackupService.downloadBackupOrNull(accessToken)
            val remoteUpdatedAt = remoteBackupJson?.let(repository::readBackupDataUpdatedAtMillis) ?: 0L
            val shouldRestore = remoteBackupJson != null &&
                (
                    remoteUpdatedAt > localUpdatedAt ||
                        (localEntries.isEmpty() && repository.backupHasEntries(remoteBackupJson))
                    )

            if (shouldRestore) {
                repository.restoreBackupJson(remoteBackupJson)
                repository.saveLastCloudSyncDiaryUpdatedAtMillis(remoteUpdatedAt)
            } else {
                val backupPayload = repository.exportBackupJson()
                val uploadedDiaryUpdatedAtMillis = repository.readBackupDataUpdatedAtMillis(backupPayload)
                googleDriveBackupService.uploadBackup(accessToken, backupPayload)
                repository.saveLastCloudSyncDiaryUpdatedAtMillis(uploadedDiaryUpdatedAtMillis)
            }

            repository.saveBackupAccountEmail(resolvedAccountLabel)
            repository.saveGoogleDriveLinked(true)
            DiaryReminderScheduler.scheduleReminders(
                getApplication(),
                if (repository.isNotificationsEnabled()) repository.loadReminderTimes() else emptyList()
            )
        }
        refresh()
        return result
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

    private fun resolveAccountLabel(accessToken: String, accountEmail: String?): String =
        runCatching {
            googleDriveBackupService.fetchAuthorizedUserLabelOrNull(accessToken)
        }.getOrNull()
            ?: accountEmail?.takeIf { it.isNotBlank() }
            ?: repository.loadBackupAccountEmail()
            ?: "ログイン済み"

    private fun canUseBiometric(application: Application): Boolean =
        BiometricManager.from(application).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS

    private fun resolveBackupFile(
        contentResolver: ContentResolver,
        explicitTreeUri: Uri?,
        createIfMissing: Boolean
    ): DocumentFile {
        val treeUri = explicitTreeUri
            ?: repository.loadBackupTreeUri()?.let(Uri::parse)
            ?: throw IllegalStateException("バックアップフォルダーを選択してください")
        val folder = DocumentFile.fromTreeUri(getApplication(), treeUri)
            ?: throw IllegalStateException("バックアップフォルダーを開けません")
        if (folder.name != BACKUP_FOLDER_NAME) {
            throw IllegalStateException(BACKUP_FOLDER_SELECT_MESSAGE)
        }
        if (explicitTreeUri != null) {
            runCatching {
                contentResolver.takePersistableUriPermission(
                    treeUri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            repository.saveBackupTreeUri(treeUri.toString())
        }

        val existing = folder.findFile(BACKUP_FILE_NAME)
        if (existing != null) return existing
        if (!createIfMissing) {
            throw IllegalStateException("バックアップファイルが見つかりません")
        }
        return folder.createFile("application/json", BACKUP_FILE_NAME)
            ?: throw IllegalStateException("バックアップファイルを作成できません")
    }

    private companion object {
        const val BACKUP_FOLDER_NAME = "まいにち日記"
        const val BACKUP_FILE_NAME = "mainichi_diary_backup"
        const val BACKUP_FOLDER_SELECT_MESSAGE =
            "Google Drive の マイドライブ/まいにち日記 フォルダーを選択してください"
    }
}
