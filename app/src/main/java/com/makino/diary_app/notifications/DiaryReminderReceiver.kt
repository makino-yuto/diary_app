package com.makino.diary_app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DiaryReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        DiaryReminderScheduler.showReminderNotification(context)
        DiaryReminderScheduler.reminderTimeFromIntent(intent)?.let { time ->
            DiaryReminderScheduler.scheduleReminder(context, time)
        }
    }
}
