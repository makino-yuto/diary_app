package com.makino.diary_app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.makino.diary_app.data.DiaryRepository

class DiaryReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                DiaryReminderScheduler.scheduleReminders(
                    context,
                    DiaryRepository(context).loadReminderTimes()
                )
            }
        }
    }
}
