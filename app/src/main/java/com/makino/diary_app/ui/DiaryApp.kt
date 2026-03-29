package com.makino.diary_app.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.makino.diary_app.R
import com.makino.diary_app.data.DiaryEntry
import com.makino.diary_app.model.AppThemePreset
import com.makino.diary_app.ui.theme.AccentGreen
import com.makino.diary_app.ui.theme.AccentPeach
import com.makino.diary_app.ui.theme.GoldSand
import com.makino.diary_app.ui.theme.Ink
import com.makino.diary_app.ui.theme.JetBrainsMsGothicFontFamily
import com.makino.diary_app.ui.theme.LineBlue
import com.makino.diary_app.ui.theme.MistBlue
import com.makino.diary_app.ui.theme.MutedInk
import com.makino.diary_app.ui.theme.Paper
import com.makino.diary_app.ui.theme.PaperDark
import com.makino.diary_app.ui.theme.SurfaceBorder
import com.makino.diary_app.ui.theme.WarmBrown
import com.makino.diary_app.ui.theme.DiaryappTheme
import com.makino.diary_app.ui.theme.paletteForTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val ROUTE_SPLASH = "splash"
private const val ROUTE_ONBOARDING = "onboarding"
private const val ROUTE_CALENDAR = "calendar"
private const val ROUTE_DIARY_MENU = "diary_menu"
private const val ROUTE_SETTINGS = "settings"
private const val ROUTE_REMINDER_SETTINGS = "reminder_settings"
private const val ROUTE_THEME_PICKER = "theme_picker"
private const val ROUTE_CHAT = "chat"
private const val ROUTE_DIARY_PREFIX = "diary"
private const val ARG_EDIT = "edit"
private val DIARY_LINE_HEIGHT = 36.dp
private val ScreenShape = RoundedCornerShape(30.dp)

private const val LABEL_CALENDAR_TITLE = "\u30ab\u30ec\u30f3\u30c0\u30fc"
private const val LABEL_DIARY_MENU = "\u65e5\u8a18"
private const val LABEL_DIARY_MENU_TITLE = "\u65e5\u8a18"
private const val LABEL_CALENDAR_TAB = "\u30ab\u30ec\u30f3\u30c0\u30fc"
private const val LABEL_SETTINGS = "\u8a2d\u5b9a"
private const val LABEL_SETTINGS_TITLE = "\u8a2d\u5b9a"
private const val LABEL_THEME = "\u30c6\u30fc\u30de"
private const val LABEL_NOTIFICATION_TIME = "\u901a\u77e5\u6642\u523b"
private const val LABEL_SELECT_TIME = "\u6642\u9593\u3092\u9078\u3076"
private const val LABEL_LATER = "\u3042\u3068\u3067"
private const val LABEL_CHANGE = "\u5909\u66f4"
private const val LABEL_UNSET = "\u672a\u8a2d\u5b9a"
private const val LABEL_ADD_TIME = "\u8ffd\u52a0"
private const val LABEL_REMOVE = "\u524a\u9664"
private const val LABEL_SELECTED = "\u9078\u629e\u4e2d"
private const val LABEL_PREVIOUS_MONTH = "\u524d\u6708"
private const val LABEL_NEXT_MONTH = "\u6b21\u6708"
private const val LABEL_PICK_PHOTO = "\u5199\u771f\u3092\u9078\u629e"
private const val LABEL_ADD_PHOTO = "\u5199\u771f\u3092\u8ffd\u52a0"
private const val LABEL_NO_PHOTO = "\u306a\u3044"
private const val LABEL_FINISH = "\u5b8c\u4e86"
private const val LABEL_BACK = "\u623b\u308b"
private const val LABEL_VIEW = "\u95b2\u89a7"
private const val LABEL_EDIT = "\u7de8\u96c6"
private const val LABEL_SAVE = "\u4fdd\u5b58"
private const val LABEL_START = "\u306f\u3058\u3081\u308b"
private const val PROMPT_FALLBACK = "\u4eca\u65e5\u306f\u3069\u3093\u306a\u4e00\u65e5\u3067\u3057\u305f\u304b\uff1f"
private const val PROMPT_PHOTO_QUESTION = "\u6b8b\u3057\u3066\u304a\u304d\u305f\u3044\u5199\u771f\u306f\u3042\u308a\u307e\u3059\u304b\uff1f\u4f55\u679a\u3067\u3082\u9078\u629e\u53ef\u80fd\u3067\u3059"
private const val PROMPT_NO_PHOTO_SAVED = "\u4eca\u65e5\u306f\u5199\u771f\u306a\u3057\u3067\u307e\u3068\u3081\u3066\u304a\u304d\u307e\u3057\u305f\u3002"
private const val PROMPT_PHOTO_SAVED = "\u3053\u306e\u5199\u771f\u305f\u3061\u3092\u4eca\u65e5\u306e\u65e5\u8a18\u306b\u8cbc\u3063\u3066\u304a\u304d\u307e\u3059\u306d\u3002"
private const val PROMPT_PLACEHOLDER = "\u4eca\u65e5\u306e\u3053\u3068\u3092\u307e\u3068\u3081\u3066\u307f\u307e\u3057\u3087\u3046"
private const val PROMPT_EDIT_NOTE = "\u4f1a\u8a71\u3092\u4fdd\u5b58\u3057\u305f\u3042\u3068\u3082\u3001\u30ab\u30ec\u30f3\u30c0\u30fc\u304b\u3089\u3044\u3064\u3067\u3082\u66f8\u304d\u76f4\u305b\u307e\u3059\u3002"
private const val PROMPT_CALENDAR_STYLE_NOTE = "\u66f8\u304d\u305f\u3044\u65e5\u3092\u9078\u3093\u3067\u3001\u305d\u306e\u307e\u307e\u7de8\u96c6\u3067\u304d\u307e\u3059\u3002"
private const val PROMPT_EMPTY_DIARY = "\u3053\u306e\u65e5\u306e\u65e5\u8a18\u306f\u307e\u3060\u3042\u308a\u307e\u305b\u3093\u3002"
private const val PROMPT_DIARY_MENU_NOTE = "\u3053\u3053\u304b\u3089\u4fdd\u5b58\u6e08\u307f\u306e\u65e5\u8a18\u3092\u958b\u3051\u307e\u3059\u3002"
private const val PROMPT_DIARY_MENU_EMPTY = "\u307e\u3060\u95b2\u89a7\u3067\u304d\u308b\u65e5\u8a18\u304c\u3042\u308a\u307e\u305b\u3093\u3002"
private const val PROMPT_THEME_NOTE = "\u8868\u793a\u30c6\u30fc\u30de\u3092\u9078\u3079\u307e\u3059\u3002"
private const val PROMPT_NOTIFICATION_TIME_NOTE = "\u305d\u306e\u65e5\u306e\u65e5\u8a18\u304c\u672a\u8a18\u5165\u306e\u3068\u304d\u3060\u3051\u3001\u8a2d\u5b9a\u3057\u305f\u6642\u9593\u306b\u901a\u77e5\u3057\u307e\u3059\u3002\n\u8907\u6570\u8ffd\u52a0\u3067\u304d\u307e\u3059\u3002"
private const val PROMPT_THEME_SETTINGS_NOTE = "\u30c6\u30fc\u30de\u30ab\u30e9\u30fc\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u3059"
private const val PROMPT_NOTIFICATION_SETTINGS_NOTE = "\u901a\u77e5\u6642\u523b\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u3059"
private const val PROMPT_ONBOARDING_REMINDER = "\u306f\u3058\u3081\u306b\u3001\u6bce\u65e5\u3069\u306e\u6642\u9593\u306b\u65e5\u8a18\u3092\u66f8\u304f\u304b\u6c7a\u3081\u307e\u3057\u3087\u3046\u3002\u8907\u6570\u8ffd\u52a0\u3067\u304d\u307e\u3059\u3002"
private const val PROMPT_ONBOARDING_THEME = "\u6b21\u306b\u3001\u30c6\u30fc\u30de\u30ab\u30e9\u30fc\u3092\u9078\u3073\u307e\u3057\u3087\u3046\u3002\u30bf\u30c3\u30d7\u3059\u308b\u3068\u305d\u306e\u5834\u3067\u53cd\u6620\u3055\u308c\u307e\u3059\u3002"
private const val PROMPT_ONBOARDING_FINISH = "\u8a2d\u5b9a\u3067\u304d\u305f\u3089\u3001\u4e0b\u306e\u300c\u306f\u3058\u3081\u308b\u300d\u304b\u3089\u9032\u307f\u307e\u3057\u3087\u3046\u3002"
private val CHAT_TEXT_SIZE = 18.sp
private val CHAT_LINE_HEIGHT = 28.sp

private enum class BottomSection {
    DiaryMenu,
    Calendar,
    Settings
}

private fun diaryRoute(date: LocalDate, edit: Boolean = false): String =
    "$ROUTE_DIARY_PREFIX/$date?$ARG_EDIT=$edit"

@Composable
private fun BottomNavigationBar(
    selectedSection: BottomSection,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
        ) {
            BottomNavigationButton(
                modifier = Modifier.weight(1f),
                label = LABEL_DIARY_MENU,
                iconRes = R.drawable.ic_nav_diary,
                selected = selectedSection == BottomSection.DiaryMenu,
                onClick = onOpenDiaryMenu
            )
            BottomNavigationDivider()
            BottomNavigationButton(
                modifier = Modifier.weight(1f),
                label = LABEL_CALENDAR_TAB,
                iconRes = R.drawable.ic_nav_calendar,
                selected = selectedSection == BottomSection.Calendar,
                onClick = onOpenCalendar
            )
            BottomNavigationDivider()
            BottomNavigationButton(
                modifier = Modifier.weight(1f),
                label = LABEL_SETTINGS,
                iconRes = R.drawable.ic_nav_settings,
                selected = selectedSection == BottomSection.Settings,
                onClick = onOpenSettings
            )
        }
    }
}

@Composable
private fun BottomNavigationButton(
    modifier: Modifier = Modifier,
    label: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val contentColor = if (selected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.72f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (selected) colorScheme.background else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(22.dp),
            tint = contentColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontFamily = JetBrainsMsGothicFontFamily),
            color = contentColor
        )
    }
}

@Composable
private fun BottomNavigationDivider() {
    Box(
        modifier = Modifier
            .padding(vertical = 14.dp)
            .width(1.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    )
}

@Composable
private fun ScreenBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFBF6EF),
                        Paper,
                        Color(0xFFF1ECE4)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-64).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AccentPeach.copy(alpha = 0.34f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-96).dp, y = 96.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MistBlue.copy(alpha = 0.75f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        content()
    }
}

@Composable
private fun GlassPanel(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = ScreenShape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.88f)),
        border = BorderStroke(1.dp, SurfaceBorder.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun SectionPill(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        GoldSand.copy(alpha = 0.9f),
                        AccentPeach.copy(alpha = 0.65f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceBorder.copy(alpha = 0.75f),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Ink
        )
    }
}

@Composable
fun DiaryApp(
    forceShowOnboarding: Boolean = false,
    onConsumeForceShowOnboarding: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: DiaryViewModel = viewModel(factory = DiaryViewModel.Factory(application))
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }
    var hasRequestedNotificationPermissionThisLaunch by rememberSaveable { mutableStateOf(false) }
    val requestNotificationPermission = {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    val openReminderTimePicker = {
        val initialTime = uiState.reminderTimes.lastOrNull() ?: LocalTime.of(21, 0)
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                viewModel.addReminderTime(hourOfDay, minute)
                requestNotificationPermission()
            },
            initialTime.hour,
            initialTime.minute,
            true
        ).show()
    }

    LaunchedEffect(uiState.reminderTimes) {
        com.makino.diary_app.notifications.DiaryReminderScheduler.scheduleReminders(
            context,
            uiState.reminderTimes
        )
        if (
            uiState.reminderTimes.isNotEmpty() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !hasRequestedNotificationPermissionThisLaunch &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            hasRequestedNotificationPermissionThisLaunch = true
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    DiaryappTheme(themePreset = uiState.themePreset) {
        SystemBarAppearance(useDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DiaryNavigation(
                navController = navController,
                uiState = uiState,
                forceShowOnboarding = forceShowOnboarding,
                onConsumeForceShowOnboarding = onConsumeForceShowOnboarding,
                onEnsureTodayDraft = viewModel::ensureTodayDraft,
                onEnsureDraft = viewModel::ensureDraft,
                onSaveTodayText = viewModel::saveTodayText,
                onSaveDiaryText = viewModel::saveDiaryText,
                onSaveTodayPhotos = { uris ->
                    viewModel.saveTodayPhotos(
                        context.contentResolver,
                        uris,
                        markPhotoStepCompleted = false
                    )
                },
                onSavePhotos = { date, uris -> viewModel.savePhotos(date, context.contentResolver, uris) },
                onCompleteTodayPhotoStep = viewModel::completeTodayPhotoStep,
                onRemovePhoto = viewModel::removePhoto,
                onMarkTodayNoPhotos = viewModel::markTodayNoPhotos,
                onUpdateMonth = viewModel::updateMonth,
                onSetThemePreset = viewModel::setThemePreset,
                onOpenReminderTimePicker = openReminderTimePicker,
                onRemoveReminderTime = viewModel::removeReminderTime,
                onCompleteOnboarding = viewModel::completeOnboarding
            )
        }
    }
}

@Composable
private fun SystemBarAppearance(useDarkIcons: Boolean) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = useDarkIcons
            insetsController.isAppearanceLightNavigationBars = useDarkIcons
        }
    }
}

@Composable
private fun DiaryNavigation(
    navController: NavHostController,
    uiState: DiaryUiState,
    forceShowOnboarding: Boolean,
    onConsumeForceShowOnboarding: () -> Unit,
    onEnsureTodayDraft: () -> Unit,
    onEnsureDraft: (LocalDate) -> Unit,
    onSaveTodayText: (String) -> Unit,
    onSaveDiaryText: (LocalDate, String) -> Unit,
    onSaveTodayPhotos: (List<Uri>) -> Unit,
    onSavePhotos: (LocalDate, List<Uri>) -> Unit,
    onCompleteTodayPhotoStep: () -> Unit,
    onRemovePhoto: (LocalDate, String) -> Unit,
    onMarkTodayNoPhotos: () -> Unit,
    onUpdateMonth: (YearMonth) -> Unit,
    onSetThemePreset: (AppThemePreset) -> Unit,
    onOpenReminderTimePicker: () -> Unit,
    onRemoveReminderTime: (LocalTime) -> Unit,
    onCompleteOnboarding: () -> Unit
) {
    val today = LocalDate.now()
    var launchRouteResolved by rememberSaveable { mutableStateOf(false) }
    val openCalendar = {
        navController.navigate(ROUTE_CALENDAR) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
    val openDiaryMenu = {
        navController.navigate(ROUTE_DIARY_MENU) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
    val openSettings = {
        navController.navigate(ROUTE_SETTINGS) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
    val openThemePicker = {
        navController.navigate(ROUTE_THEME_PICKER) {
            launchSingleTop = true
        }
    }
    val openReminderSettings = {
        navController.navigate(ROUTE_REMINDER_SETTINGS) {
            launchSingleTop = true
        }
    }
    val finishTodayChat = {
        navController.navigate(diaryRoute(today)) {
            popUpTo(ROUTE_CHAT) { inclusive = true }
            launchSingleTop = true
        }
    }
    val finishOnboarding = {
        onCompleteOnboarding()
        val target = if (uiState.hasCompletedEntry(today)) {
            ROUTE_CALENDAR
        } else {
            if (uiState.entryFor(today) == null) {
                onEnsureTodayDraft()
            }
            ROUTE_CHAT
        }
        navController.navigate(target) {
            popUpTo(ROUTE_ONBOARDING) { inclusive = true }
            launchSingleTop = true
        }
    }

    LaunchedEffect(
        uiState.isLoading,
        uiState.entries,
        uiState.hasCompletedOnboarding,
        forceShowOnboarding
    ) {
        if (uiState.isLoading) return@LaunchedEffect
        if (forceShowOnboarding) {
            navController.navigate(ROUTE_ONBOARDING) {
                launchSingleTop = true
            }
            onConsumeForceShowOnboarding()
            return@LaunchedEffect
        }
        if (launchRouteResolved) return@LaunchedEffect
        if (!uiState.hasCompletedOnboarding) {
            navController.navigate(ROUTE_ONBOARDING) {
                popUpTo(ROUTE_SPLASH) { inclusive = true }
                launchSingleTop = true
            }
            launchRouteResolved = true
            return@LaunchedEffect
        }
        if (uiState.entryFor(today) == null) {
            onEnsureTodayDraft()
            navController.navigate(ROUTE_CHAT) {
                popUpTo(ROUTE_SPLASH) { inclusive = true }
                launchSingleTop = true
            }
            launchRouteResolved = true
            return@LaunchedEffect
        }
        val target = if (uiState.hasCompletedEntry(today)) ROUTE_CALENDAR else ROUTE_CHAT
        navController.navigate(target) {
            popUpTo(ROUTE_SPLASH) { inclusive = true }
            launchSingleTop = true
        }
        launchRouteResolved = true
    }

    NavHost(navController = navController, startDestination = ROUTE_SPLASH) {
        composable(ROUTE_SPLASH) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        composable(ROUTE_ONBOARDING) {
            OnboardingChatScreen(
                reminderTimes = uiState.reminderTimes,
                themePreset = uiState.themePreset,
                onAddReminderTime = onOpenReminderTimePicker,
                onRemoveReminderTime = onRemoveReminderTime,
                onSelectThemePreset = onSetThemePreset,
                onFinish = finishOnboarding
            )
        }
        composable(ROUTE_CHAT) {
            ChatScreen(
                entry = uiState.entryFor(today),
                onDoneText = { text ->
                    onSaveTodayText(text)
                    if (uiState.entryFor(today)?.photoStepCompleted == true) {
                        finishTodayChat()
                    }
                },
                onSavePhotos = {
                    onSaveTodayPhotos(it)
                },
                onCompletePhotos = {
                    onCompleteTodayPhotoStep()
                    finishTodayChat()
                },
                onNoPhotos = {
                    onMarkTodayNoPhotos()
                    finishTodayChat()
                }
            )
        }
        composable(ROUTE_CALENDAR) {
            CalendarScreen(
                uiState = uiState,
                onPreviousMonth = { onUpdateMonth(uiState.visibleMonth.minusMonths(1)) },
                onNextMonth = { onUpdateMonth(uiState.visibleMonth.plusMonths(1)) },
                onSelectMonth = onUpdateMonth,
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings,
                onOpenDate = { date ->
                    onEnsureDraft(date)
                    navController.navigate(diaryRoute(date))
                }
            )
        }
        composable(ROUTE_DIARY_MENU) {
            DiaryMenuScreen(
                uiState = uiState,
                onOpenEntry = { date -> navController.navigate(diaryRoute(date)) },
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings
            )
        }
        composable(ROUTE_SETTINGS) {
            SettingsScreen(
                themePreset = uiState.themePreset,
                reminderTimes = uiState.reminderTimes,
                onOpenThemeSelection = openThemePicker,
                onOpenReminderSettings = openReminderSettings,
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings
            )
        }
        composable(ROUTE_REMINDER_SETTINGS) {
            ReminderSelectionScreen(
                reminderTimes = uiState.reminderTimes,
                onAddReminderTime = onOpenReminderTimePicker,
                onRemoveReminderTime = onRemoveReminderTime,
                onBack = { navController.popBackStack() },
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings
            )
        }
        composable(ROUTE_THEME_PICKER) {
            ThemeSelectionScreen(
                selectedThemePreset = uiState.themePreset,
                onSelectThemePreset = onSetThemePreset,
                onBack = { navController.popBackStack() },
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings
            )
        }
        composable(
            route = "$ROUTE_DIARY_PREFIX/{date}?$ARG_EDIT={$ARG_EDIT}",
            arguments = listOf(
                navArgument(ARG_EDIT) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("date")
            val date = dateString?.let(LocalDate::parse)
            val startInEditMode = backStackEntry.arguments?.getBoolean(ARG_EDIT) ?: false
            DiaryScreen(
                date = date,
                entry = date?.let(uiState::entryFor),
                onBack = { navController.popBackStack() },
                startInEditMode = startInEditMode,
                onEnsureDraft = onEnsureDraft,
                onSaveText = onSaveDiaryText,
                onSavePhotos = onSavePhotos,
                onRemovePhoto = onRemovePhoto,
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings
            )
        }
    }
}

@Composable
private fun OnboardingChatScreen(
    reminderTimes: List<LocalTime>,
    themePreset: AppThemePreset,
    onAddReminderTime: () -> Unit,
    onRemoveReminderTime: (LocalTime) -> Unit,
    onSelectThemePreset: (AppThemePreset) -> Unit,
    onFinish: () -> Unit
) {
    val today = LocalDate.now()
    var hasSkippedReminderSelection by rememberSaveable { mutableStateOf(false) }
    val hasAdvancedToThemeStep = reminderTimes.isNotEmpty() || hasSkippedReminderSelection

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (hasAdvancedToThemeStep) {
                Surface(
                    tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onFinish,
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text(LABEL_START)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = today.format(DateTimeFormatter.ofPattern("M\u6708d\u65e5 EEEE", Locale.JAPAN)),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                item {
                    MessageBubble(
                        text = PROMPT_ONBOARDING_REMINDER,
                        isBot = true
                    )
                }
                item {
                    OnboardingReminderCard(
                        reminderTimes = reminderTimes,
                        onPickTime = onAddReminderTime,
                        onRemoveReminderTime = onRemoveReminderTime,
                        onSkip = { hasSkippedReminderSelection = true }
                    )
                }
                if (reminderTimes.isNotEmpty()) {
                    item {
                        MessageBubble(
                            text = reminderTimes.joinToString(" / ") { formatReminderTime(it) },
                            isBot = false
                        )
                    }
                }
                if (hasAdvancedToThemeStep) {
                    item {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_THEME,
                            isBot = true
                        )
                    }
                    item {
                        MessageBubble(
                            text = themePreset.label,
                            isBot = false
                        )
                    }
                    item {
                        ThemeSelectionInlineCard(
                            selectedThemePreset = themePreset,
                            onSelectThemePreset = onSelectThemePreset
                        )
                    }
                    item {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_FINISH,
                            isBot = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatScreen(
    entry: DiaryEntry?,
    onDoneText: (String) -> Unit,
    onSavePhotos: (List<Uri>) -> Unit,
    onCompletePhotos: () -> Unit,
    onNoPhotos: () -> Unit
) {
    val today = LocalDate.now()
    var input by rememberSaveable(entry?.date, entry?.userText) { mutableStateOf(entry?.userText.orEmpty()) }
    var isInputFocused by rememberSaveable { mutableStateOf(false) }
    val canFinishText by remember(input) {
        derivedStateOf { input.isNotBlank() }
    }
    val focusManager = LocalFocusManager.current
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            onSavePhotos(uris)
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (entry?.userText.isNullOrBlank()) {
                ChatComposerBar(
                    input = input,
                    isInputFocused = isInputFocused,
                    onInputChange = { input = it },
                    onFocusChanged = { isInputFocused = it },
                    canSend = canFinishText,
                    onSend = {
                        focusManager.clearFocus()
                        onDoneText(input)
                    }
                )
            } else if (entry.photoStepCompleted != true) {
                PhotoActionBar(
                    hasPhotos = entry.photoUris.isNotEmpty(),
                    onPickPhotos = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onFinishPhotos = onCompletePhotos,
                    onNoPhotos = onNoPhotos
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = today.format(DateTimeFormatter.ofPattern("M\u6708d\u65e5 EEEE", Locale.JAPAN)),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                item {
                    MessageBubble(
                        text = PROMPT_FALLBACK,
                        isBot = true
                    )
                }
                if (!entry?.userText.isNullOrBlank()) {
                    item {
                        MessageBubble(
                            text = entry.userText,
                            isBot = false
                        )
                    }
                    item {
                        MessageBubble(
                            text = PROMPT_PHOTO_QUESTION,
                            isBot = true
                        )
                    }
                    if (entry.photoUris.isNotEmpty() && entry.photoStepCompleted != true) {
                        item {
                            PhotoRow(photoUris = entry.photoUris)
                        }
                    }
                }
                if (entry?.photoStepCompleted == true) {
                    item {
                        if (entry.photoUris.isEmpty()) {
                            MessageBubble(
                                text = PROMPT_NO_PHOTO_SAVED,
                                isBot = true
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                MessageBubble(
                                    text = PROMPT_PHOTO_SAVED,
                                    isBot = true
                                )
                                PhotoRow(photoUris = entry.photoUris)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatComposerBar(
    input: String,
    isInputFocused: Boolean,
    onInputChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    canSend: Boolean,
    onSend: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .imePadding()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = input,
                onValueChange = onInputChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { onFocusChanged(it.isFocused) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = CHAT_TEXT_SIZE,
                    lineHeight = CHAT_LINE_HEIGHT,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                minLines = 1,
                maxLines = if (isInputFocused) 5 else 1,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (input.isBlank()) {
                            Text(
                                text = PROMPT_PLACEHOLDER,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = CHAT_TEXT_SIZE,
                                    lineHeight = CHAT_LINE_HEIGHT,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.46f)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Button(
                onClick = onSend,
                enabled = canSend,
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_send),
                    contentDescription = "\u9001\u4fe1",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun PhotoActionBar(
    hasPhotos: Boolean,
    onPickPhotos: () -> Unit,
    onFinishPhotos: () -> Unit,
    onNoPhotos: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilledTonalButton(
                onClick = onPickPhotos,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(if (hasPhotos) LABEL_ADD_PHOTO else LABEL_PICK_PHOTO)
            }
            OutlinedButton(
                onClick = if (hasPhotos) onFinishPhotos else onNoPhotos,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(if (hasPhotos) LABEL_FINISH else LABEL_NO_PHOTO)
            }
        }
    }
}

@Composable
private fun MessageBubble(text: String, isBot: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isBot) Arrangement.Start else Arrangement.End
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isBot) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                }
            ),
            shape = RoundedCornerShape(
                topStart = 22.dp,
                topEnd = 22.dp,
                bottomStart = if (isBot) 6.dp else 22.dp,
                bottomEnd = if (isBot) 22.dp else 6.dp
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = CHAT_TEXT_SIZE,
                    lineHeight = CHAT_LINE_HEIGHT
                )
            )
        }
    }
}

@Composable
private fun CalendarScreen(
    uiState: DiaryUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectMonth: (YearMonth) -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenDate: (LocalDate) -> Unit
) {
    val days = remember(uiState.visibleMonth) { calendarDays(uiState.visibleMonth) }
    var selectedDate by rememberSaveable(uiState.visibleMonth) { mutableStateOf<LocalDate?>(null) }
    var isMonthPickerVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomNavigationBar(
                selectedSection = BottomSection.Calendar,
                onOpenDiaryMenu = onOpenDiaryMenu,
                onOpenCalendar = onOpenCalendar,
                onOpenSettings = onOpenSettings
            )
        },
        floatingActionButton = {
            Button(
                onClick = { selectedDate?.let(onOpenDate) },
                enabled = selectedDate != null,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(LABEL_VIEW)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = LABEL_CALENDAR_TITLE,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onPreviousMonth) { Text(LABEL_PREVIOUS_MONTH) }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { isMonthPickerVisible = true }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.visibleMonth.format(
                                    DateTimeFormatter.ofPattern("yyyy\u5e74M\u6708", Locale.JAPAN)
                                ),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = JetBrainsMsGothicFontFamily
                                )
                            )
                            Text(
                                text = "\u25be",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = JetBrainsMsGothicFontFamily
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(onClick = onNextMonth) { Text(LABEL_NEXT_MONTH) }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf(
                            "\u6708",
                            "\u706b",
                            "\u6c34",
                            "\u6728",
                            "\u91d1",
                            "\u571f",
                            "\u65e5"
                        ).forEach { label ->
                            Text(
                                text = label,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    days.chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            week.forEach { day ->
                                DayCell(
                                    modifier = Modifier.weight(1f),
                                    date = day,
                                    hasEntry = day != null && uiState.entryFor(day)?.isCompleted == true,
                                    isSelected = day != null && day == selectedDate,
                                    isToday = day == LocalDate.now(),
                                    onClick = {
                                        if (day != null) {
                                            if (selectedDate == day) {
                                                onOpenDate(day)
                                            } else {
                                                selectedDate = day
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        if (isMonthPickerVisible) {
            CalendarMonthPickerDialog(
                visibleMonth = uiState.visibleMonth,
                onDismiss = { isMonthPickerVisible = false },
                onSelectMonth = { selectedMonth ->
                    onSelectMonth(selectedMonth)
                    isMonthPickerVisible = false
                }
            )
        }
    }
}

@Composable
private fun CalendarMonthPickerDialog(
    visibleMonth: YearMonth,
    onDismiss: () -> Unit,
    onSelectMonth: (YearMonth) -> Unit
) {
    var selectedYear by rememberSaveable(visibleMonth) { mutableStateOf(visibleMonth.year) }
    val months = remember { (1..12).toList() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "\u5e74\u6708\u3092\u9078\u629e",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { selectedYear -= 1 }) { Text("\u524d\u306e\u5e74") }
                    Text(
                        text = "$selectedYear\u5e74",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = JetBrainsMsGothicFontFamily
                        )
                    )
                    TextButton(onClick = { selectedYear += 1 }) { Text("\u6b21\u306e\u5e74") }
                }
                months.chunked(4).forEach { monthRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        monthRow.forEach { month ->
                            val isSelected =
                                selectedYear == visibleMonth.year && month == visibleMonth.monthValue
                            FilledTonalButton(
                                onClick = { onSelectMonth(YearMonth.of(selectedYear, month)) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (isSelected) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                    contentColor = if (isSelected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            ) {
                                Text("$month\u6708")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("\u9589\u3058\u308b")
            }
        }
    )
}

@Composable
private fun DiaryMenuScreen(
    uiState: DiaryUiState,
    onOpenEntry: (LocalDate) -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val completedEntries = remember(uiState.entries) {
        uiState.entries.filter { it.isCompleted }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomNavigationBar(
                selectedSection = BottomSection.DiaryMenu,
                onOpenDiaryMenu = onOpenDiaryMenu,
                onOpenCalendar = onOpenCalendar,
                onOpenSettings = onOpenSettings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = LABEL_DIARY_MENU_TITLE,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = PROMPT_DIARY_MENU_NOTE,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (completedEntries.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = PROMPT_DIARY_MENU_EMPTY,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(completedEntries.size) { index ->
                        val entry = completedEntries[index]
                        val previewPhoto = entry.photoUris.firstOrNull()
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenEntry(entry.date) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = entry.date.format(DateTimeFormatter.ofPattern("yy/MM/dd", Locale.JAPAN)),
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = JetBrainsMsGothicFontFamily
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = entry.userText.ifBlank { PROMPT_EMPTY_DIARY },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (previewPhoto != null) {
                                    AsyncImage(
                                        model = previewPhoto,
                                        contentDescription = "\u65e5\u8a18\u30d7\u30ec\u30d3\u30e5\u30fc",
                                        modifier = Modifier
                                            .size(width = 88.dp, height = 88.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    themePreset: AppThemePreset,
    reminderTimes: List<LocalTime>,
    onOpenThemeSelection: () -> Unit,
    onOpenReminderSettings: () -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomNavigationBar(
                selectedSection = BottomSection.Settings,
                onOpenDiaryMenu = onOpenDiaryMenu,
                onOpenCalendar = onOpenCalendar,
                onOpenSettings = onOpenSettings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = LABEL_SETTINGS_TITLE,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            SettingsItemRow(
                title = LABEL_NOTIFICATION_TIME,
                supportingText = PROMPT_NOTIFICATION_SETTINGS_NOTE,
                value = formatReminderSummary(reminderTimes),
                onClick = onOpenReminderSettings
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsItemRow(
                title = LABEL_THEME,
                supportingText = PROMPT_THEME_SETTINGS_NOTE,
                value = themePreset.label,
                onClick = onOpenThemeSelection
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OnboardingReminderCard(
    reminderTimes: List<LocalTime>,
    onPickTime: () -> Unit,
    onRemoveReminderTime: (LocalTime) -> Unit,
    onSkip: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = PROMPT_NOTIFICATION_TIME_NOTE,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface.copy(alpha = 0.78f)
            )
            if (reminderTimes.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    reminderTimes.forEach { time ->
                        ReminderTimeChip(
                            time = time,
                            onRemove = { onRemoveReminderTime(time) }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = onPickTime,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (reminderTimes.isEmpty()) LABEL_SELECT_TIME else LABEL_ADD_TIME)
                }
                OutlinedButton(
                    onClick = onSkip,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(LABEL_LATER)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReminderTimesCard(
    reminderTimes: List<LocalTime>,
    onAddReminderTime: () -> Unit,
    onRemoveReminderTime: (LocalTime) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = LABEL_NOTIFICATION_TIME,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = JetBrainsMsGothicFontFamily
                        ),
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = PROMPT_NOTIFICATION_TIME_NOTE,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                FilledTonalButton(onClick = onAddReminderTime) {
                    Text(LABEL_ADD_TIME)
                }
            }

            if (reminderTimes.isEmpty()) {
                Text(
                    text = LABEL_UNSET,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurface.copy(alpha = 0.58f)
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    reminderTimes.forEach { time ->
                        ReminderTimeChip(
                            time = time,
                            onRemove = { onRemoveReminderTime(time) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderTimeChip(
    time: LocalTime,
    onRemove: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(colorScheme.primary.copy(alpha = 0.12f))
            .border(1.dp, colorScheme.primary.copy(alpha = 0.22f), RoundedCornerShape(999.dp))
            .padding(start = 12.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatReminderTime(time),
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMsGothicFontFamily
            ),
            color = colorScheme.primary
        )
        TextButton(
            onClick = onRemove,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
        ) {
            Text(LABEL_REMOVE)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemeSelectionInlineCard(
    selectedThemePreset: AppThemePreset,
    onSelectThemePreset: (AppThemePreset) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val inlineThemeOrder = listOf(
        AppThemePreset.Bougainvillea,
        AppThemePreset.CyclamenPink,
        AppThemePreset.Apricot,
        AppThemePreset.CreamYellow,
        AppThemePreset.SpringGreen,
        AppThemePreset.HorizonBlue,
        AppThemePreset.EcruBeige,
        AppThemePreset.Lilac,
        AppThemePreset.BlancDeZinc,
        AppThemePreset.IvoryBlack
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            inlineThemeOrder.forEach { theme ->
                ThemeInlineChip(
                    themePreset = theme,
                    isSelected = theme == selectedThemePreset,
                    onClick = { onSelectThemePreset(theme) }
                )
            }
        }
    }
}

@Composable
private fun ThemeInlineChip(
    themePreset: AppThemePreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val previewColors = paletteForTheme(themePreset).previewColors

    Row(
        modifier = Modifier
            .then(
                if (themePreset == AppThemePreset.HorizonBlue || themePreset == AppThemePreset.Lilac) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) colorScheme.primary.copy(alpha = 0.12f)
                else colorScheme.background
            )
            .border(
                width = 1.dp,
                color = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemePreviewRow(previewColors = previewColors)
        Text(
            text = themePreset.label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = JetBrainsMsGothicFontFamily
            ),
            color = if (isSelected) colorScheme.primary else colorScheme.onSurface
        )
    }
}

@Composable
private fun SettingsItemRow(
    title: String,
    supportingText: String,
    value: String,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = JetBrainsMsGothicFontFamily
                        ),
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = JetBrainsMsGothicFontFamily
                        ),
                        color = colorScheme.primary
                    )
                    Text(
                        text = LABEL_CHANGE,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.56f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderSelectionScreen(
    reminderTimes: List<LocalTime>,
    onAddReminderTime: () -> Unit,
    onRemoveReminderTime: (LocalTime) -> Unit,
    onBack: () -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomNavigationBar(
                selectedSection = BottomSection.Settings,
                onOpenDiaryMenu = onOpenDiaryMenu,
                onOpenCalendar = onOpenCalendar,
                onOpenSettings = onOpenSettings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) { Text(LABEL_BACK) }
                Text(
                    text = LABEL_NOTIFICATION_TIME,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    )
                )
                Spacer(modifier = Modifier.width(52.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            ReminderTimesCard(
                reminderTimes = reminderTimes,
                onAddReminderTime = onAddReminderTime,
                onRemoveReminderTime = onRemoveReminderTime
            )
        }
    }
}

@Composable
private fun ThemeSelectionScreen(
    selectedThemePreset: AppThemePreset,
    onSelectThemePreset: (AppThemePreset) -> Unit,
    onBack: () -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomNavigationBar(
                selectedSection = BottomSection.Settings,
                onOpenDiaryMenu = onOpenDiaryMenu,
                onOpenCalendar = onOpenCalendar,
                onOpenSettings = onOpenSettings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) { Text(LABEL_BACK) }
                Text(
                    text = LABEL_THEME,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    )
                )
                Spacer(modifier = Modifier.width(52.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(AppThemePreset.entries.size) { index ->
                    val themePreset = AppThemePreset.entries[index]
                    ThemePresetCard(
                        themePreset = themePreset,
                        isSelected = themePreset == selectedThemePreset,
                        onClick = { onSelectThemePreset(themePreset) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemePresetCard(
    themePreset: AppThemePreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val previewColors = paletteForTheme(themePreset).previewColors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                colorScheme.primary.copy(alpha = 0.12f)
            } else {
                colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = themePreset.label,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    ),
                    color = colorScheme.onSurface
                )
                ThemePreviewRow(previewColors = previewColors)
            }
            Text(
                text = if (isSelected) LABEL_SELECTED else LABEL_CHANGE,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                ),
                color = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.56f)
            )
        }
    }
}

@Composable
private fun ThemePreviewRow(previewColors: List<Color>) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        previewColors.forEach { previewColor ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(previewColor)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
            )
        }
    }
}

private fun formatReminderTime(time: LocalTime): String =
    String.format(Locale.JAPAN, "%02d:%02d", time.hour, time.minute)

private fun formatReminderSummary(reminderTimes: List<LocalTime>): String =
    when {
        reminderTimes.isEmpty() -> LABEL_UNSET
        reminderTimes.size == 1 -> formatReminderTime(reminderTimes.first())
        else -> "${formatReminderTime(reminderTimes.first())} ほか${reminderTimes.size - 1}件"
    }

@Composable
private fun DayCell(
    modifier: Modifier = Modifier,
    date: LocalDate?,
    hasEntry: Boolean,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    isSelected -> colorScheme.primary.copy(alpha = 0.18f)
                    isToday -> colorScheme.secondary.copy(alpha = 0.55f)
                    hasEntry -> colorScheme.surface
                    else -> colorScheme.background
                }
            )
            .border(
                width = 1.dp,
                color = if (isSelected || isToday) colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = date != null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Text(
                text = date.dayOfMonth.toString(),
                color = colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            if (hasEntry) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 7.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(colorScheme.tertiary)
                )
            }
        }
    }
}

@Composable
private fun DiaryScreen(
    date: LocalDate?,
    entry: DiaryEntry?,
    startInEditMode: Boolean,
    onBack: () -> Unit,
    onEnsureDraft: (LocalDate) -> Unit,
    onSaveText: (LocalDate, String) -> Unit,
    onSavePhotos: (LocalDate, List<Uri>) -> Unit,
    onRemovePhoto: (LocalDate, String) -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    LaunchedEffect(date, entry) {
        if (date != null && entry == null) {
            onEnsureDraft(date)
        }
    }

    var isEditing by rememberSaveable(date, startInEditMode) { mutableStateOf(startInEditMode) }
    var editText by rememberSaveable(entry?.date, entry?.userText) { mutableStateOf(entry?.userText.orEmpty()) }

    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val navigationBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val diaryLift by animateDpAsState(
        targetValue = if (isEditing) (imeBottom - navigationBottom).coerceAtLeast(0.dp) else 0.dp,
        label = "diaryLift"
    )

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        val currentDate = entry?.date ?: date
        if (uris.isNotEmpty() && currentDate != null) {
            onSavePhotos(currentDate, uris)
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomNavigationBar(
                selectedSection = BottomSection.DiaryMenu,
                onOpenDiaryMenu = onOpenDiaryMenu,
                onOpenCalendar = onOpenCalendar,
                onOpenSettings = onOpenSettings
            )
        }
    ) { innerPadding ->
        if (entry == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = diaryLift)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) { Text(LABEL_BACK) }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isEditing) {
                        FilledTonalButton(
                            onClick = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        ) {
                            Text(LABEL_ADD_PHOTO)
                        }
                        FilledTonalButton(
                            onClick = {
                                onSaveText(entry.date, editText)
                                isEditing = false
                            }
                        ) {
                            Text(LABEL_SAVE)
                        }
                    } else {
                        FilledTonalButton(onClick = { isEditing = true }) {
                            Text(LABEL_EDIT)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinedDiaryPaper(
                modifier = Modifier.weight(1f),
                entry = entry,
                isEditing = isEditing,
                editText = editText,
                onEditTextChange = { editText = it },
                onRemovePhoto = { photoUri -> onRemovePhoto(entry.date, photoUri) }
            )
        }
    }
}

@Composable
private fun LinedDiaryPaper(
    modifier: Modifier = Modifier,
    entry: DiaryEntry,
    isEditing: Boolean,
    editText: String,
    onEditTextChange: (String) -> Unit,
    onRemovePhoto: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = entry.date.format(DateTimeFormatter.ofPattern("yy/MM/dd", Locale.JAPAN)),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinedTextSection(
                modifier = Modifier.fillMaxWidth(),
                text = if (isEditing) editText else entry.userText,
                isEditing = isEditing,
                onTextChange = onEditTextChange
            )
            if (entry.photoUris.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                PhotoColumn(
                    photoUris = entry.photoUris,
                    isEditing = isEditing,
                    onRemovePhoto = onRemovePhoto
                )
            }
        }
    }
}

@Composable
private fun LinedTextSection(
    modifier: Modifier = Modifier,
    text: String,
    isEditing: Boolean,
    onTextChange: (String) -> Unit
) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val lineColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
    }

    Box(
        modifier = modifier
            .heightIn(min = DIARY_LINE_HEIGHT)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val lineSpacing = DIARY_LINE_HEIGHT.toPx()
            var currentY = lineSpacing
            while (currentY <= size.height + 1f) {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, currentY),
                    end = Offset(size.width, currentY),
                    strokeWidth = 2f
                )
                currentY += lineSpacing
            }
        }
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = DIARY_LINE_HEIGHT),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            readOnly = !isEditing,
            cursorBrush = SolidColor(
                if (isEditing) MaterialTheme.colorScheme.onSurface else Color.Transparent
            ),
            minLines = 1
        )
    }
}

@Composable
private fun PhotoColumn(
    photoUris: List<String>,
    isEditing: Boolean,
    onRemovePhoto: (String) -> Unit
) {
    var fullscreenPhotoUri by remember(photoUris) { mutableStateOf<String?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        photoUris.forEach { uri ->
            val painter = rememberAsyncImagePainter(model = uri)
            val intrinsicSize = painter.intrinsicSize

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                val imageAspectRatio = if (
                    intrinsicSize.width.isFinite() &&
                    intrinsicSize.height.isFinite() &&
                    intrinsicSize.width > 0f &&
                    intrinsicSize.height > 0f
                ) {
                    intrinsicSize.width / intrinsicSize.height
                } else {
                    1f
                }
                val containerAspectRatio = maxWidth.value / 220.dp.value
                val imageModifier = if (imageAspectRatio >= containerAspectRatio) {
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(imageAspectRatio)
                } else {
                    Modifier
                        .fillMaxHeight()
                        .aspectRatio(imageAspectRatio)
                }

                Box(
                    modifier = imageModifier.clickable { fullscreenPhotoUri = uri },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "\u65e5\u8a18\u306e\u5199\u771f",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    if (isEditing) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 10.dp, y = (-10).dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.92f))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f),
                                    shape = CircleShape
                                )
                                .clickable { onRemovePhoto(uri) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "\u00d7",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    fullscreenPhotoUri?.let { photoUri ->
        FullscreenPhotoDialog(
            photoUri = photoUri,
            onDismiss = { fullscreenPhotoUri = null }
        )
    }
}

@Composable
private fun PhotoRow(photoUris: List<String>) {
    PhotoColumn(
        photoUris = photoUris,
        isEditing = false,
        onRemovePhoto = {}
    )
}

@Composable
private fun FullscreenPhotoDialog(
    photoUri: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = photoUri,
                contentDescription = "\u62e1\u5927\u8868\u793a\u4e2d\u306e\u5199\u771f",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

private fun calendarDays(month: YearMonth): List<LocalDate?> {
    val first = month.atDay(1)
    val leadingSpaces = (first.dayOfWeek.value - DayOfWeek.MONDAY.value).let { if (it < 0) it + 7 else it }
    val days = MutableList<LocalDate?>(leadingSpaces) { null }
    for (day in 1..month.lengthOfMonth()) {
        days += month.atDay(day)
    }
    while (days.size % 7 != 0) {
        days += null
    }
    return days
}







