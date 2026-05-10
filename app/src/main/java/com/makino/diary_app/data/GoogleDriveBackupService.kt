package com.makino.diary_app.data

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class GoogleDriveBackupService {
    fun fetchAuthorizedUserLabelOrNull(accessToken: String): String? {
        val response = request(
            accessToken = accessToken,
            method = "GET",
            url = "$DRIVE_API_BASE/about?fields=user(emailAddress,displayName)"
        )
        val user = JSONObject(response).optJSONObject("user") ?: return null
        return user.optString("emailAddress").takeIf { it.isNotBlank() }
            ?: user.optString("displayName").takeIf { it.isNotBlank() }
    }

    fun uploadBackup(accessToken: String, backupJson: String) {
        val folderId = findOrCreateBackupFolderId(accessToken)
        val existingFileId = findBackupFileId(accessToken, folderId)
        val fileId = existingFileId ?: createBackupFile(accessToken, folderId)
        updateBackupFile(accessToken, fileId, backupJson)
    }

    fun downloadBackupOrNull(accessToken: String): String? {
        val folderId = findBackupFolderId(accessToken) ?: return null
        val fileId = findBackupFileId(accessToken, folderId) ?: return null
        return request(
            accessToken = accessToken,
            method = "GET",
            url = "$DRIVE_API_BASE/files/$fileId?alt=media"
        )
    }

    private fun findBackupFolderId(accessToken: String): String? {
        val query = "name = '$BACKUP_FOLDER_NAME' and mimeType = '$FOLDER_MIME_TYPE' and 'root' in parents and trashed = false"
        val response = request(
            accessToken = accessToken,
            method = "GET",
            url = "$DRIVE_API_BASE/files?q=${encodeQuery(query)}&spaces=drive&fields=files(id,name)&orderBy=modifiedTime desc"
        )
        return JSONObject(response)
            .optJSONArray("files")
            ?.optJSONObject(0)
            ?.optString("id")
            ?.takeIf { it.isNotBlank() }
    }

    private fun findOrCreateBackupFolderId(accessToken: String): String =
        findBackupFolderId(accessToken) ?: createBackupFolder(accessToken)

    private fun createBackupFolder(accessToken: String): String {
        val body = JSONObject()
            .put("name", BACKUP_FOLDER_NAME)
            .put("mimeType", FOLDER_MIME_TYPE)
            .put("parents", JSONArray().put("root"))

        val response = request(
            accessToken = accessToken,
            method = "POST",
            url = "$DRIVE_API_BASE/files?fields=id,name",
            contentType = JSON_CONTENT_TYPE,
            body = body.toString()
        )

        return JSONObject(response).getString("id")
    }

    private fun findBackupFileId(accessToken: String, folderId: String): String? {
        val query = "name = '$BACKUP_FILE_NAME' and '$folderId' in parents and trashed = false"
        val response = request(
            accessToken = accessToken,
            method = "GET",
            url = "$DRIVE_API_BASE/files?q=${encodeQuery(query)}&spaces=drive&fields=files(id,name)&orderBy=modifiedTime desc"
        )
        return JSONObject(response)
            .optJSONArray("files")
            ?.optJSONObject(0)
            ?.optString("id")
            ?.takeIf { it.isNotBlank() }
    }

    private fun createBackupFile(accessToken: String, folderId: String): String {
        val body = JSONObject()
            .put("name", BACKUP_FILE_NAME)
            .put("mimeType", "application/json")
            .put("parents", JSONArray().put(folderId))

        val response = request(
            accessToken = accessToken,
            method = "POST",
            url = "$DRIVE_API_BASE/files?fields=id,name",
            contentType = JSON_CONTENT_TYPE,
            body = body.toString()
        )

        return JSONObject(response).getString("id")
    }

    private fun updateBackupFile(
        accessToken: String,
        fileId: String,
        backupJson: String
    ) {
        request(
            accessToken = accessToken,
            method = "PATCH",
            url = "$UPLOAD_API_BASE/files/$fileId?uploadType=media",
            contentType = JSON_CONTENT_TYPE,
            body = backupJson
        )
    }

    private fun request(
        accessToken: String,
        method: String,
        url: String,
        contentType: String? = null,
        body: String? = null
    ): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            setRequestProperty("Authorization", "Bearer $accessToken")
            setRequestProperty("Accept", "application/json")
            contentType?.let { setRequestProperty("Content-Type", it) }
            if (body != null) {
                doOutput = true
            }
        }

        try {
            if (body != null) {
                BufferedOutputStream(connection.outputStream).bufferedWriter(Charsets.UTF_8).use { writer ->
                    writer.write(body)
                    writer.flush()
                }
            }

            val statusCode = connection.responseCode
            val responseText = runCatching {
                val stream = if (statusCode in 200..299) connection.inputStream else connection.errorStream
                if (stream == null) {
                    ""
                } else {
                    BufferedInputStream(stream).bufferedReader(Charsets.UTF_8).use { it.readText() }
                }
            }.getOrDefault("")

            if (statusCode !in 200..299) {
                val message = runCatching {
                    JSONObject(responseText).optJSONObject("error")?.optString("message")
                }.getOrNull()
                throw IllegalStateException(
                    message?.takeIf { it.isNotBlank() }
                        ?: "Google Drive との通信に失敗しました"
                )
            }

            return responseText
        } finally {
            connection.disconnect()
        }
    }

    private fun encodeQuery(query: String): String =
        URLEncoder.encode(query, Charsets.UTF_8.name())

    companion object {
        const val BACKUP_FOLDER_NAME = "\u307e\u3044\u306b\u3061\u65e5\u8a18"
        const val BACKUP_FILE_NAME = "mainichi_diary_backup.json"

        private const val DRIVE_API_BASE = "https://www.googleapis.com/drive/v3"
        private const val UPLOAD_API_BASE = "https://www.googleapis.com/upload/drive/v3"
        private const val JSON_CONTENT_TYPE = "application/json; charset=UTF-8"
        private const val FOLDER_MIME_TYPE = "application/vnd.google-apps.folder"
    }
}
