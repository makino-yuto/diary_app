package com.makino.diary_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.makino.diary_app.data.DiaryRepository
import com.makino.diary_app.notifications.DiaryReminderScheduler
import com.makino.diary_app.ui.DiaryApp

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DiaryReminderScheduler.scheduleReminders(this, DiaryRepository(this).loadReminderTimes())
        enableEdgeToEdge()
        setContent {
            DiaryApp()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
