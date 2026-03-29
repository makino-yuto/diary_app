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
    private var forceShowOnboarding by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forceShowOnboarding = shouldForceShowOnboarding(intent)
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
        forceShowOnboarding = shouldForceShowOnboarding(intent)
    }

    private fun shouldForceShowOnboarding(intent: Intent?): Boolean =
        intent?.getBooleanExtra(EXTRA_FORCE_SHOW_ONBOARDING, false) == true

    companion object {
        const val EXTRA_FORCE_SHOW_ONBOARDING = "force_show_onboarding"
    }
}
