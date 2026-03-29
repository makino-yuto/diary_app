package com.makino.diary_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.makino.diary_app.data.DiaryRepository
import com.makino.diary_app.notifications.DiaryReminderScheduler
import com.makino.diary_app.ui.DiaryApp

class MainActivity : ComponentActivity() {
    private var forceShowOnboarding by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forceShowOnboarding = true
        DiaryReminderScheduler.scheduleReminders(this, DiaryRepository(this).loadReminderTimes())
        enableEdgeToEdge()
        setContent {
            DiaryApp(
                forceShowOnboarding = forceShowOnboarding,
                onConsumeForceShowOnboarding = { forceShowOnboarding = false }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        forceShowOnboarding = true
    }
}
