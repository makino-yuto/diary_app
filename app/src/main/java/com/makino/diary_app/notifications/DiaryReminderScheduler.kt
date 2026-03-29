package com.makino.diary_app.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.makino.diary_app.MainActivity
import com.makino.diary_app.R
import com.makino.diary_app.data.DiaryRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object DiaryReminderScheduler {
    private const val CHANNEL_ID = "diary_daily_reminder"
    private const val CHANNEL_NAME = "\u65e5\u8a18\u30ea\u30de\u30a4\u30f3\u30c0\u30fc"
    private const val NOTIFICATION_ID = 1001
    private const val REQUEST_CODE_OPEN_APP = 2002
    private const val EXTRA_HOUR = "extra_hour"
    private const val EXTRA_MINUTE = "extra_minute"

    fun scheduleReminders(context: Context, times: List<LocalTime>) {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java) ?: return
        for (hour in 0..23) {
            for (minute in 0..59) {
                alarmManager.cancel(reminderPendingIntent(context, hour, minute))
            }
        }
        times.distinct().sorted().forEach { scheduleReminder(context, it) }
    }

    fun scheduleReminder(context: Context, time: LocalTime) {
        ensureNotificationChannel(context)

        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java) ?: return
        val triggerAtMillis = nextTriggerAtMillis(time)
        val pendingIntent = reminderPendingIntent(context, time.hour, time.minute)

        alarmManager.cancel(pendingIntent)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms() -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
            else -> {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        }
    }

    fun showReminderNotification(context: Context) {
        ensureNotificationChannel(context)
        if (DiaryRepository(context).getEntry(LocalDate.now())?.isCompleted == true) return
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("\u4eca\u65e5\u306f\u3069\u3093\u306a\u4e00\u65e5\u3067\u3057\u305f\u304b\uff1f")
            .setContentText("\u4eca\u65e5\u306e\u3053\u3068\u3092\u8a18\u9332\u3057\u307e\u3057\u3087\u3046")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent(context))
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    fun reminderTimeFromIntent(intent: Intent?): LocalTime? {
        val hour = intent?.getIntExtra(EXTRA_HOUR, -1) ?: -1
        val minute = intent?.getIntExtra(EXTRA_MINUTE, -1) ?: -1
        if (hour !in 0..23 || minute !in 0..59) return null
        return LocalTime.of(hour, minute)
    }

    private fun reminderPendingIntent(context: Context, hour: Int, minute: Int): PendingIntent {
        val intent = Intent(context, DiaryReminderReceiver::class.java).apply {
            putExtra(EXTRA_HOUR, hour)
            putExtra(EXTRA_MINUTE, minute)
        }
        return PendingIntent.getBroadcast(
            context,
            reminderRequestCode(hour, minute),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openAppPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_OPEN_APP,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun nextTriggerAtMillis(time: LocalTime): Long {
        val now = LocalDateTime.now()
        var next = now.withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }
        return next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun reminderRequestCode(hour: Int, minute: Int): Int = hour * 100 + minute

    private fun ensureNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = ContextCompat.getSystemService(context, NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
    }
}
