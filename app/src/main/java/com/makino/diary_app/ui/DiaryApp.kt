package com.makino.diary_app.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.fragment.app.FragmentActivity
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.makino.diary_app.R
import com.makino.diary_app.data.DiaryEntry
import com.makino.diary_app.model.AppThemePreset
import com.makino.diary_app.model.GoogleDriveSyncMode
import com.makino.diary_app.ui.theme.AccentGreen
import com.makino.diary_app.ui.theme.AccentPeach
import com.makino.diary_app.ui.theme.DEFAULT_THEME_INTENSITY
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
import coil3.request.ImageRequest
import coil3.video.videoFrameMillis
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

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
private val PanelShape = RoundedCornerShape(22.dp)
private val ControlShape = RoundedCornerShape(18.dp)
private val InlineShape = RoundedCornerShape(999.dp)

private const val LABEL_CALENDAR_TITLE = "\u30ab\u30ec\u30f3\u30c0\u30fc"
private const val LABEL_DIARY_MENU = "\u65e5\u8a18"
private const val LABEL_DIARY_MENU_TITLE = "\u65e5\u8a18"
private const val LABEL_CALENDAR_TAB = "\u30ab\u30ec\u30f3\u30c0\u30fc"
private const val LABEL_SETTINGS = "\u8a2d\u5b9a"
private const val LABEL_SETTINGS_TITLE = "\u8a2d\u5b9a"
private const val LABEL_SETTINGS_SECTION_NOTIFICATION_THEME = "\u901a\u77e5/\u30c6\u30fc\u30de"
private const val LABEL_SETTINGS_SECTION_IMPORTANT = "\u91cd\u8981"
private const val LABEL_SETTINGS_SECTION_SECURITY = "\u30bb\u30ad\u30e5\u30ea\u30c6\u30a3"
private const val LABEL_SETTINGS_SECTION_BACKUP = "\u30d0\u30c3\u30af\u30a2\u30c3\u30d7"
private const val LABEL_THEME = "\u30c6\u30fc\u30de"
private const val LABEL_NOTIFICATION_TIME = "\u901a\u77e5\u6642\u523b"
private const val LABEL_NOTIFICATION_TOGGLE = "\u901a\u77e5\u306eON/OFF"
private const val LABEL_DELETE_ALL_DATA = "\u5168\u30c7\u30fc\u30bf\u3092\u524a\u9664\u3059\u308b"
private const val LABEL_TERMS_OF_USE = "\u5229\u7528\u898f\u7d04"
private const val LABEL_PRIVACY_POLICY = "\u30d7\u30e9\u30a4\u30d0\u30b7\u30fc\u30dd\u30ea\u30b7\u30fc"
private const val LABEL_COMMERCIAL_DISCLOSURE = "\u7279\u5b9a\u5546\u53d6\u5f15\u6cd5\u306b\u57fa\u3065\u304f\u8868\u8a18"
private const val LABEL_FINGERPRINT_AUTH = "\u6307\u7d0b\u8a8d\u8a3c"
private const val LABEL_PASSWORD_AUTH = "\u30d1\u30b9\u30ef\u30fc\u30c9\u8a8d\u8a3c"
private const val LABEL_BACKUP = "\u30d0\u30c3\u30af\u30a2\u30c3\u30d7"
private const val LABEL_RESTORE_DATA = "\u30c7\u30fc\u30bf\u306e\u5fa9\u5143"
private const val LABEL_GOOGLE_ACCOUNT = "Google\u30a2\u30ab\u30a6\u30f3\u30c8"
private const val LABEL_SYNC_METHOD = "\u540c\u671f\u65b9\u6cd5"
private const val LABEL_SYNC_NOW = "\u4eca\u3059\u3050\u540c\u671f"
private const val LABEL_SYNC_MODE_AUTO = "\u81ea\u52d5"
private const val LABEL_SYNC_MODE_MANUAL = "\u624b\u52d5"
private const val LABEL_LOGIN_WITH_GOOGLE = "Google\u3067\u30ed\u30b0\u30a4\u30f3"
private const val LABEL_LOGOUT = "\u30ed\u30b0\u30a2\u30a6\u30c8"
private const val LABEL_SET_PASSWORD = "\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u8a2d\u5b9a"
private const val LABEL_CHANGE_PASSWORD = "\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5909\u66f4"
private const val LABEL_CURRENT_PASSWORD = "\u73fe\u5728\u306e\u30d1\u30b9\u30ef\u30fc\u30c9"
private const val LABEL_NEW_PASSWORD = "\u65b0\u3057\u3044\u30d1\u30b9\u30ef\u30fc\u30c9"
private const val LABEL_CONFIRM_PASSWORD = "\u78ba\u8a8d\u7528\u30d1\u30b9\u30ef\u30fc\u30c9"
private const val LABEL_UNLOCK = "\u30ed\u30c3\u30af\u3092\u89e3\u9664"
private const val LABEL_USE_FINGERPRINT = "\u6307\u7d0b\u3067\u8a8d\u8a3c"
private const val LABEL_DELETE = "\u524a\u9664"
private const val LABEL_CANCEL = "\u30ad\u30e3\u30f3\u30bb\u30eb"
private const val LABEL_CLOSE = "\u9589\u3058\u308b"
private const val LABEL_SETTING_VERSION = "\u00a9\u307e\u3044\u306b\u3061\u65e5\u8a18 version1.0.0"
private const val LABEL_SELECT_TIME = "\u6642\u9593\u3092\u9078\u3076"
private const val LABEL_LATER = "\u3042\u3068\u3067"
private const val LABEL_CHANGE = "\u5909\u66f4"
private const val LABEL_UNSET = "\u672a\u8a2d\u5b9a"
private const val LABEL_ADD_TIME = "\u8ffd\u52a0"
private const val LABEL_REMOVE = "\u524a\u9664"
private const val LABEL_SELECTED = "\u9078\u629e\u4e2d"
private const val LABEL_CONNECTED = "\u30ed\u30b0\u30a4\u30f3\u6e08\u307f"
private const val LABEL_PREVIOUS_MONTH = "\u524d\u6708"
private const val LABEL_NEXT_MONTH = "\u6b21\u6708"
private const val LABEL_PICK_MEDIA = "\u5199\u771f\u3084\u52d5\u753b\u3092\u9078\u629e"
private const val LABEL_ADD_MEDIA = "\u5199\u771f\u3084\u52d5\u753b\u3092\u8ffd\u52a0"
private const val LABEL_NO_PHOTO = "\u306a\u3044"
private const val LABEL_FINISH = "\u5b8c\u4e86"
private const val LABEL_BACK = "\u623b\u308b"
private const val LABEL_VIEW = "\u95b2\u89a7"
private const val LABEL_EDIT = "\u7de8\u96c6"
private const val LABEL_SAVE = "\u4fdd\u5b58"
private const val LABEL_START = "\u306f\u3058\u3081\u308b"
private const val PROMPT_FALLBACK = "\u4eca\u65e5\u306f\u3069\u3093\u306a\u4e00\u65e5\u3067\u3057\u305f\u304b\uff1f"
private const val PROMPT_PHOTO_QUESTION = "\u6b8b\u3057\u3066\u304a\u304d\u305f\u3044\u5199\u771f\u3084\u52d5\u753b\u306f\u3042\u308a\u307e\u3059\u304b\uff1f\u4f55\u679a\u3067\u3082\u9078\u629e\u53ef\u80fd\u3067\u3059"
private const val PROMPT_NO_PHOTO_SAVED = "\u4eca\u65e5\u306f\u5199\u771f\u3084\u52d5\u753b\u306a\u3057\u3067\u307e\u3068\u3081\u3066\u304a\u304d\u307e\u3057\u305f"
private const val PROMPT_PHOTO_SAVED = "\u3053\u306e\u5199\u771f\u3084\u52d5\u753b\u3092\u4eca\u65e5\u306e\u65e5\u8a18\u306b\u8cbc\u3063\u3066\u304a\u304d\u307e\u3059\u306d"
private const val PROMPT_PLACEHOLDER = "\u4eca\u65e5\u306e\u3053\u3068\u3092\u307e\u3068\u3081\u3066\u307f\u307e\u3057\u3087\u3046"
private const val PROMPT_EDIT_NOTE = "\u4f1a\u8a71\u3092\u4fdd\u5b58\u3057\u305f\u3042\u3068\u3082\u3001\u30ab\u30ec\u30f3\u30c0\u30fc\u304b\u3089\u3044\u3064\u3067\u3082\u66f8\u304d\u76f4\u305b\u307e\u3059\u3002"
private const val PROMPT_CALENDAR_STYLE_NOTE = "\u66f8\u304d\u305f\u3044\u65e5\u3092\u9078\u3093\u3067\u3001\u305d\u306e\u307e\u307e\u7de8\u96c6\u3067\u304d\u307e\u3059\u3002"
private const val PROMPT_EMPTY_DIARY = "\u3053\u306e\u65e5\u306e\u65e5\u8a18\u306f\u307e\u3060\u3042\u308a\u307e\u305b\u3093\u3002"
private const val PROMPT_DIARY_MENU_EMPTY = "\u307e\u3060\u95b2\u89a7\u3067\u304d\u308b\u65e5\u8a18\u304c\u3042\u308a\u307e\u305b\u3093\u3002"
private const val PROMPT_THEME_NOTE = "\u8868\u793a\u30c6\u30fc\u30de\u3092\u9078\u3079\u307e\u3059\u3002"
private const val PROMPT_NOTIFICATION_TIME_NOTE = "\u305d\u306e\u65e5\u306e\u65e5\u8a18\u304c\u672a\u8a18\u5165\u306e\u3068\u304d\u3060\u3051\u3001\u8a2d\u5b9a\u3057\u305f\u6642\u9593\u306b\u901a\u77e5\u3057\u307e\u3059\n\u8907\u6570\u8ffd\u52a0\u3067\u304d\u307e\u3059"
private const val PROMPT_THEME_SETTINGS_NOTE = "\u30c6\u30fc\u30de\u30ab\u30e9\u30fc\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u3059"
private const val PROMPT_NOTIFICATION_SETTINGS_NOTE = "\u901a\u77e5\u6642\u523b\u3092\u8a2d\u5b9a\u3067\u304d\u307e\u3059"
private const val PROMPT_SECURITY_PREPARING = "\u3053\u306e\u6a5f\u80fd\u306f\u3053\u308c\u304b\u3089\u4f5c\u308a\u8fbc\u3093\u3067\u3044\u304d\u307e\u3059"
private const val PROMPT_LEGAL_PREPARING = "\u3053\u306e\u5185\u5bb9\u306f\u6e96\u5099\u4e2d\u3067\u3059"
private const val PROMPT_DELETE_ALL_DATA = "\u4fdd\u5b58\u6e08\u307f\u306e\u65e5\u8a18\u3001\u901a\u77e5\u8a2d\u5b9a\u3001\u30bb\u30ad\u30e5\u30ea\u30c6\u30a3\u8a2d\u5b9a\u3092\u3059\u3079\u3066\u524a\u9664\u3057\u307e\u3059\u304b"
private const val PROMPT_SECURITY_LOCK = "\u30a2\u30d7\u30ea\u3092\u958b\u304f\u306b\u306f\u8a8d\u8a3c\u304c\u5fc5\u8981\u3067\u3059"
private const val PROMPT_PASSWORD_MINIMUM = "\u30d1\u30b9\u30ef\u30fc\u30c9\u306f4\u6587\u5b57\u4ee5\u4e0a\u3067\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044"
private const val PROMPT_PASSWORD_MISMATCH = "\u78ba\u8a8d\u7528\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093"
private const val PROMPT_PASSWORD_INCORRECT = "\u73fe\u5728\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u9055\u3044\u307e\u3059"
private const val PROMPT_PASSWORD_LOCK_FAILED = "\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u9055\u3044\u307e\u3059"
private const val PROMPT_BIOMETRIC_FAILED = "\u8a8d\u8a3c\u306b\u5931\u6557\u3057\u307e\u3057\u305f"
private const val PROMPT_PASSWORD_ROW_NOTE = "\u30bf\u30c3\u30d7\u3067\u8a2d\u5b9a\u307e\u305f\u306f\u5909\u66f4\u3067\u304d\u307e\u3059"
private const val PROMPT_BIOMETRIC_UNAVAILABLE = "\u3053\u306e\u7aef\u672b\u3067\u306f\u6307\u7d0b\u8a8d\u8a3c\u3092\u5229\u7528\u3067\u304d\u307e\u305b\u3093"
private const val PROMPT_BIOMETRIC_NOT_ENROLLED = "\u7aef\u672b\u306b\u6307\u7d0b\u307e\u305f\u306f\u7aef\u672b\u8a8d\u8a3c\u304c\u767b\u9332\u3055\u308c\u3066\u3044\u307e\u305b\u3093"
private const val PROMPT_BIOMETRIC_TEMPORARY_ERROR = "\u3044\u307e\u306f\u6307\u7d0b\u8a8d\u8a3c\u3092\u5229\u7528\u3067\u304d\u307e\u305b\u3093"
private const val PROMPT_BACKUP_SUCCESS = "\u30c7\u30fc\u30bf\u3092\u30d0\u30c3\u30af\u30a2\u30c3\u30d7\u3057\u307e\u3057\u305f"
private const val PROMPT_BACKUP_FAILED = "\u30d0\u30c3\u30af\u30a2\u30c3\u30d7\u306b\u5931\u6557\u3057\u307e\u3057\u305f"
private const val PROMPT_RESTORE_SUCCESS = "\u30c7\u30fc\u30bf\u3092\u5fa9\u5143\u3057\u307e\u3057\u305f"
private const val PROMPT_RESTORE_FAILED = "\u30c7\u30fc\u30bf\u306e\u5fa9\u5143\u306b\u5931\u6557\u3057\u307e\u3057\u305f"
private const val PROMPT_BACKUP_DIRECTORY_NOTE = "Google Drive \u306e \u30de\u30a4\u30c9\u30e9\u30a4\u30d6/\u307e\u3044\u306b\u3061\u65e5\u8a18 \u3068\u9023\u643a\u3067\u304d\u307e\u3059"
private const val PROMPT_SYNC_METHOD_NOTE = "\u4fdd\u5b58\u6642\u3060\u3051\u81ea\u52d5\u3067\u540c\u671f\u3059\u308b\u304b \u624b\u52d5\u3067\u540c\u671f\u3059\u308b\u304b\u9078\u3079\u307e\u3059"
private const val PROMPT_SYNC_NOW_NOTE = "\u73fe\u5728\u306e\u65e5\u8a18\u30c7\u30fc\u30bf\u3092 Google Drive \u3068\u3059\u3050\u306b\u540c\u671f\u3057\u307e\u3059"
private const val PROMPT_GOOGLE_DRIVE_CONNECT_FAILED = "Google アカウントへの接続に失敗しました"
private const val PROMPT_GOOGLE_DRIVE_SYNCING = "同期中です"
private const val PROMPT_GOOGLE_DRIVE_SYNCING_NOTE = "Google Drive と日記データを確認しています"
private const val PROMPT_GOOGLE_DRIVE_SYNCING_COMPACT = "同期中"
private const val PROMPT_GOOGLE_DRIVE_SYNCED_COMPACT = "同期済み"
private const val PROMPT_GOOGLE_DRIVE_SYNC_FAILED_COMPACT = "同期失敗"
private const val PROMPT_GOOGLE_DRIVE_LOGOUT_SUCCESS = "Google アカウントからログアウトしました"
private const val PROMPT_GOOGLE_DRIVE_LOGOUT_FAILED = "Google アカウントからのログアウトに失敗しました"
private const val PROMPT_GOOGLE_DRIVE_RESTORED = "Google Drive と連携しました"
private const val PROMPT_GOOGLE_DRIVE_MANUAL_SYNCED = "Google Drive と同期しました"
private const val PROMPT_ONBOARDING_REMINDER = "\u306f\u3058\u3081\u306b\u3001\u6bce\u65e5\u3069\u306e\u6642\u9593\u306b\u65e5\u8a18\u3092\u66f8\u304f\u304b\u6c7a\u3081\u307e\u3057\u3087\u3046"
private const val PROMPT_ONBOARDING_GOOGLE_DRIVE = "Google Drive\u306b\u30ed\u30b0\u30a4\u30f3\u3059\u308b\u3068\u3001\u8907\u6570\u306e\u7aef\u672b\u9593\u3067\u30c7\u30fc\u30bf\u3092\u5171\u6709\u3067\u304d\u3001\u6a5f\u7a2e\u5909\u66f4\u6642\u3082\u30c7\u30fc\u30bf\u3092\u5f15\u304d\u7d99\u3052\u307e\u3059"
private const val PROMPT_ONBOARDING_GOOGLE_DRIVE_PERMISSION = "Google Drive \u306e \u30de\u30a4\u30c9\u30e9\u30a4\u30d6/\u307e\u3044\u306b\u3061\u65e5\u8a18 \u306b\u4fdd\u5b58\u3057\u307e\u3059\n\u3053\u306e\u30a2\u30d7\u30ea\u306f\u8a31\u53ef\u3055\u308c\u305f\u3053\u306e\u30d5\u30a9\u30eb\u30c0\u3068\u30a2\u30d7\u30ea\u304c\u4f5c\u6210\u3057\u305f\u30c7\u30fc\u30bf\u4ee5\u5916\u306b\u306f\u30a2\u30af\u30bb\u30b9\u3057\u307e\u305b\u3093"
private const val PROMPT_ONBOARDING_SYNC_MODE = "Google Drive \u3068\u306e\u540c\u671f\u65b9\u6cd5\u3092\u9078\u3073\u307e\u3057\u3087\u3046"
private const val PROMPT_ONBOARDING_SYNC_MODE_NOTE = "\u81ea\u52d5\u3092\u9078\u3076\u3068\u3001\u30c1\u30e3\u30c3\u30c8\u3092\u5b8c\u4e86\u3057\u305f\u3068\u304d\u3084\u65e5\u8a18\u753b\u9762\u3067\u4fdd\u5b58\u3092\u62bc\u3057\u305f\u3068\u304d\u306b Google Drive \u3078\u540c\u671f\u3055\u308c\u307e\u3059\n\u624b\u52d5\u3092\u9078\u3076\u3068\u3001\u8a2d\u5b9a\u304b\u3089\u597d\u304d\u306a\u30bf\u30a4\u30df\u30f3\u30b0\u3067\u540c\u671f\u3067\u304d\u307e\u3059"
private const val PROMPT_ONBOARDING_THEME = "\u6b21\u306b\u30c6\u30fc\u30de\u30ab\u30e9\u30fc\u3092\u9078\u3073\u307e\u3057\u3087\u3046"
private const val PROMPT_ONBOARDING_FINISH = "\u8a2d\u5b9a\u3067\u304d\u305f\u3089\u3001\u4e0b\u306e\u300c\u306f\u3058\u3081\u308b\u300d\u304b\u3089\u9032\u307f\u307e\u3057\u3087\u3046"
private val PRIVACY_POLICY_TEXT = """
プライバシーポリシー
最終更新日: 2026-05-10

1. 運営者
本アプリ「まいにち日記」は Makino Yuto によって運営されています
連絡先: contact@jinsei-makino.com

2. 取得する情報
本アプリでは、以下の情報を取り扱います

・ユーザーが入力した日記本文
・ユーザーが選択した写真や動画の参照情報
・通知時刻、テーマ、認証設定などのアプリ設定情報
・Google Drive 連携を有効にした場合の、連携に必要な Google アカウント情報

本アプリは、氏名、住所、電話番号などの個人を直接特定できる情報を、入力させる目的で取得しません
また、アクセス解析や広告配信のためのトラッキングは行っていません

3. 利用目的
取得した情報は、以下の目的で利用します

・日記データの保存、表示、編集
・写真や動画を日記に添付して表示するため
・通知、テーマ、認証などの設定を反映するため
・ユーザーが希望した場合に Google Drive へバックアップまたは同期するため

4. 保存先
日記本文、設定情報、認証設定などのデータは、主にユーザーの端末内に保存されます
Google Drive 連携を有効にした場合は、日記データのみを Google Drive の マイドライブ/まいにち日記 に保存します
テーマ、通知設定、認証設定などは Google Drive へ同期されず、端末内に保持されます

5. 第三者サービスの利用
本アプリは、必要に応じて以下の機能を利用します

・Google Drive
  ユーザーが連携を許可した場合に限り、日記データの保存、読み込み、同期のために利用します
  本アプリは、ユーザーに許可された保存先と、アプリが扱う日記データ以外へアクセスしません

・Android フォトピッカー等の端末機能
  ユーザーが選択した写真や動画を日記に添付するために利用します

・端末の生体認証または画面ロック機能
  指紋認証などの本人確認機能を利用するために使われます

6. データの削除
ユーザーは、本アプリ内の操作により日記データを削除できます
また、設定画面から全データ削除を実行することで、端末内に保存された日記データや設定を削除できます
Google Drive に保存された日記データは、連携状態や保存内容に応じて別途削除される場合があります

7. ユーザーの権利
ユーザーは、自身のデータについて、確認、修正、削除を求めることができます
本ポリシーやデータの取り扱いについてのお問い合わせは、上記連絡先までご連絡ください

8. ポリシーの変更
本ポリシーは、法令改正やサービス内容の変更に応じて改定される場合があります
重要な変更がある場合は、アプリ内表示などの適切な方法でお知らせします
""".trimIndent()
private val TERMS_OF_USE_TEXT = """
利用規約
最終更新日: 2026-05-10

この利用規約（以下「本規約」）は、Makino Yuto（以下「運営者」）が提供するアプリ「まいにち日記」（以下「本アプリ」）の利用条件を定めるものです
本アプリを利用するユーザーは、本規約に同意したうえで本アプリを利用するものとします

1. 本アプリの内容
本アプリは、日記本文、写真、動画等を記録し、閲覧、編集、保存できる日記アプリです
また、ユーザーが希望した場合に限り、Google Drive 連携によるバックアップまたは同期機能を利用できます

2. 利用条件
ユーザーは、自己の責任において本アプリを利用するものとします
ユーザーは、法令または公序良俗に反する目的で本アプリを利用してはなりません
ユーザーは、本アプリの運営を妨害する行為、第三者の権利を侵害する行為、不正アクセスその他これに類する行為をしてはなりません

3. ユーザーデータ
本アプリに入力または添付された日記本文、写真、動画その他のデータは、原則としてユーザー自身に帰属します
ユーザーは、自らの判断と責任でデータを保存、編集、削除するものとします
Google Drive 連携を有効にした場合、日記データはユーザーが指定または許可した保存先へ同期されます

4. バックアップおよび同期
本アプリは、端末の状態、通信環境、Google の提供状況その他の事情により、バックアップまたは同期が正常に完了しない場合があります
運営者は、バックアップまたは同期の失敗、遅延、消失によって生じた損害について、故意または重過失がある場合を除き責任を負いません
重要なデータについては、ユーザー自身の責任で管理してください

5. 認証機能
本アプリは、指紋認証やパスワード認証などのロック機能を提供する場合があります
これらは端末の利用環境や設定に依存するため、すべての端末で同一の動作を保証するものではありません

6. 禁止事項
ユーザーは、以下の行為を行ってはなりません

・法令または公序良俗に違反する行為
・他人の権利、利益、名誉、プライバシー等を侵害する行為
・本アプリまたは関連サービスの運営を妨害する行為
・不正な手段で本アプリを利用する行為
・本アプリの不具合や脆弱性を悪用する行為
・その他、運営者が不適切と判断する行為

7. 免責
運営者は、本アプリの完全性、正確性、継続性、有用性、特定目的への適合性を保証するものではありません
運営者は、本アプリの利用または利用不能によりユーザーに生じた損害について、運営者に故意または重過失がある場合を除き責任を負いません

8. 本アプリの変更・停止
運営者は、ユーザーへの事前通知なく、本アプリの全部または一部を変更、停止または終了することがあります

9. 本規約の変更
運営者は、必要と判断した場合、本規約を変更できるものとします
重要な変更がある場合は、アプリ内表示などの適切な方法でお知らせします

10. 準拠法および管轄
本規約は日本法に準拠します
本アプリに関して紛争が生じた場合は、運営者の所在地を管轄する裁判所を第一審の専属的合意管轄裁判所とします
""".trimIndent()
private val COMMERCIAL_DISCLOSURE_TEXT = """
特定商取引法に基づく表記
最終更新日: 2026-05-10

販売事業者名
Makino Yuto

運営責任者
Makino Yuto

所在地
請求があれば遅滞なく開示いたします

電話番号
請求があれば遅滞なく開示いたします

お問い合わせ先
contact@jinsei-makino.com

販売価格
現在、本アプリに有料プラン、アプリ内課金、追加購入機能はありません

商品代金以外の必要料金
本アプリの利用やダウンロードに伴う通信料は、ユーザーの負担となります

代金の支払時期および方法
現在、有料商品の提供はありません

商品の引渡時期
本アプリは、ユーザーの端末へインストールされた時点で利用可能となります

返品・キャンセルについて
現在、有料商品の提供はありません

動作環境
Android 端末での利用を想定しています
ただし、すべての端末での完全な動作を保証するものではありません

特記事項
将来、本アプリに有料機能や課金機能を追加する場合は、本表記を更新します
""".trimIndent()
private val CHAT_TEXT_SIZE = 18.sp
private val CHAT_LINE_HEIGHT = 28.sp
private val UtilityActionButtonGray = Color(0xFFD9D9D9)
private val UtilityActionButtonGrayDisabled = Color(0xFFBEBEBE)
private const val ONBOARDING_MESSAGE_DELAY_MS = 1500L
private const val ONBOARDING_THEME_TRANSITION_DELAY_MS = 1500L

private enum class BottomSection {
    DiaryMenu,
    Calendar,
    Settings
}

private enum class BackupAction {
    Export,
    Restore
}

private enum class GoogleDriveAction {
    RestoreFromDrive,
    UploadDiary,
    ManualSync
}

private enum class DiaryMediaKind {
    Image,
    Video
}

private fun androidx.compose.material3.ColorScheme.isPureWhiteTheme(): Boolean =
    background == Color.White &&
        surface == Color.White &&
        primary == Color.Black &&
        onSurface == Color.Black

private fun androidx.compose.material3.ColorScheme.isPureBlackTheme(): Boolean =
    background == Color(0xFF3C3C3C) &&
        surface == Color(0xFF3C3C3C) &&
        primary == Color(0xFFB0B0B0) &&
        onSurface == Color.White

private fun androidx.compose.material3.ColorScheme.isPureMonochromeTheme(): Boolean =
    isPureWhiteTheme() || isPureBlackTheme()

private fun androidx.compose.material3.ColorScheme.exactBorderColor(alpha: Float = 0.12f): Color =
    if (isPureMonochromeTheme()) outline else onSurface.copy(alpha = alpha)

private fun androidx.compose.material3.ColorScheme.secondaryTextColor(alpha: Float = 0.66f): Color =
    if (isPureMonochromeTheme()) onSurface else onSurface.copy(alpha = alpha)

private fun androidx.compose.material3.ColorScheme.calendarShellColor(): Color =
    if (isPureMonochromeTheme()) surfaceVariant else surface

private fun androidx.compose.material3.ColorScheme.calendarShellContentColor(): Color =
    if (isPureMonochromeTheme()) onSurface else onSurface

private fun legalBodyForTitle(title: String): String =
    when (title) {
        LABEL_TERMS_OF_USE -> TERMS_OF_USE_TEXT
        LABEL_PRIVACY_POLICY -> PRIVACY_POLICY_TEXT
        LABEL_COMMERCIAL_DISCLOSURE -> COMMERCIAL_DISCLOSURE_TEXT
        else -> PROMPT_LEGAL_PREPARING
    }

private data class FullscreenDiaryMedia(
    val index: Int,
    val uri: String,
    val kind: DiaryMediaKind
)

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
            .windowInsetsPadding(WindowInsets.navigationBars),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.exactBorderColor())
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
    val contentColor = if (selected || colorScheme.isPureMonochromeTheme()) {
        if (selected) colorScheme.primary else colorScheme.onSurface
    } else {
        colorScheme.onSurface.copy(alpha = 0.72f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(onClick = onClick)
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 7.dp)
                    .width(38.dp)
                    .height(3.dp)
                    .clip(InlineShape)
                    .background(if (colorScheme.isPureMonochromeTheme()) colorScheme.primary else colorScheme.primary.copy(alpha = 0.9f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
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
}

@Composable
private fun BottomNavigationDivider() {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .padding(vertical = 14.dp)
            .width(1.dp)
            .fillMaxHeight()
            .background(colorScheme.exactBorderColor())
    )
}

@Composable
private fun UtilityActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ControlShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = UtilityActionButtonGray,
            contentColor = Color.Black,
            disabledContainerColor = UtilityActionButtonGrayDisabled,
            disabledContentColor = Color.Black.copy(alpha = 0.56f)
        )
    ) {
        Text(text)
    }
}

@Composable
private fun ScreenBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (colorScheme.isPureMonochromeTheme()) {
                    SolidColor(colorScheme.background)
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFBF6EF),
                            Paper,
                            Color(0xFFF1ECE4)
                        )
                    )
                }
            )
    ) {
        if (!colorScheme.isPureMonochromeTheme()) {
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
        }
        content()
    }
}

@Composable
private fun GlassPanel(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = ScreenShape,
        colors = CardDefaults.cardColors(
            containerColor = if (colorScheme.isPureMonochromeTheme()) colorScheme.surface else Color.White.copy(alpha = 0.88f)
        ),
        border = BorderStroke(1.dp, if (colorScheme.isPureMonochromeTheme()) colorScheme.exactBorderColor() else SurfaceBorder.copy(alpha = 0.8f)),
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
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (colorScheme.isPureMonochromeTheme()) {
                    SolidColor(colorScheme.surface)
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            GoldSand.copy(alpha = 0.9f),
                            AccentPeach.copy(alpha = 0.65f)
                        )
                    )
                }
            )
            .border(
                width = 1.dp,
                color = if (colorScheme.isPureMonochromeTheme()) colorScheme.exactBorderColor() else SurfaceBorder.copy(alpha = 0.75f),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (colorScheme.isPureMonochromeTheme()) colorScheme.onSurface else Ink
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
    val coroutineScope = rememberCoroutineScope()
    val googleSignInClient = remember(context) {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(Scopes.DRIVE_FILE))
                .build()
        )
    }
    val googleDriveAuthorizationAvailable = remember(context) {
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
    }
    var backupStatusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var isDriveSyncInFlight by rememberSaveable { mutableStateOf(false) }
    var isDriveRequestInFlight by rememberSaveable { mutableStateOf(false) }
    var compactDriveStatus by rememberSaveable { mutableStateOf<String?>(null) }
    var compactDriveStatusToken by rememberSaveable { mutableStateOf(0) }
    var recoverableAuthAccount by remember { mutableStateOf<GoogleSignInAccount?>(null) }
    var recoverableAuthAction by remember { mutableStateOf<GoogleDriveAction?>(null) }
    var pendingRecoverableAuthIntent by remember { mutableStateOf<Intent?>(null) }
    var pendingGoogleDriveAction by remember { mutableStateOf(GoogleDriveAction.RestoreFromDrive) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }
    fun showCompactDriveStatus(text: String, autoHideMillis: Long? = null) {
        compactDriveStatus = text
        compactDriveStatusToken += 1
        val currentToken = compactDriveStatusToken
        if (autoHideMillis != null) {
            coroutineScope.launch {
                delay(autoHideMillis)
                if (compactDriveStatusToken == currentToken) {
                    compactDriveStatus = null
                }
            }
        }
    }
    fun resetGoogleDriveRequestState() {
        recoverableAuthAccount = null
        recoverableAuthAction = null
        pendingRecoverableAuthIntent = null
        pendingGoogleDriveAction = GoogleDriveAction.RestoreFromDrive
        isDriveRequestInFlight = false
        isDriveSyncInFlight = false
    }
    fun syncSignedInGoogleAccount(
        account: GoogleSignInAccount,
        action: GoogleDriveAction,
        interactive: Boolean
    ) {
        val resolvedAccountLabel = account.email
            ?: account.displayName
            ?: LABEL_CONNECTED
        viewModel.saveBackupAccountLabel(resolvedAccountLabel)
        val shouldBlockUi = interactive || action == GoogleDriveAction.RestoreFromDrive
        val shouldShowCompactStatus = action == GoogleDriveAction.UploadDiary && !interactive
        if (shouldBlockUi) {
            isDriveSyncInFlight = true
        }
        if (shouldShowCompactStatus) {
            showCompactDriveStatus(PROMPT_GOOGLE_DRIVE_SYNCING_COMPACT)
        }
        isDriveRequestInFlight = true
        coroutineScope.launch {
            val tokenResult = withContext(Dispatchers.IO) {
                runCatching {
                    val androidAccount = checkNotNull(account.account) {
                        "Google アカウント情報を取得できませんでした"
                    }
                    GoogleAuthUtil.getToken(
                        context,
                        androidAccount,
                        "oauth2:${Scopes.DRIVE_FILE} ${Scopes.EMAIL}"
                    )
                }
            }
            if (tokenResult.isFailure) {
                val error = tokenResult.exceptionOrNull()
                if (interactive && error is UserRecoverableAuthException) {
                    recoverableAuthAccount = account
                    recoverableAuthAction = action
                    pendingRecoverableAuthIntent = error.intent
                    if (pendingRecoverableAuthIntent == null) {
                        backupStatusMessage = PROMPT_GOOGLE_DRIVE_CONNECT_FAILED
                        resetGoogleDriveRequestState()
                        if (shouldShowCompactStatus) {
                            showCompactDriveStatus(PROMPT_GOOGLE_DRIVE_SYNC_FAILED_COMPACT, 1500)
                        }
                    }
                    return@launch
                }
                if (interactive) {
                    backupStatusMessage = error?.message ?: PROMPT_GOOGLE_DRIVE_CONNECT_FAILED
                }
                if (shouldShowCompactStatus) {
                    showCompactDriveStatus(PROMPT_GOOGLE_DRIVE_SYNC_FAILED_COMPACT, 1500)
                }
                resetGoogleDriveRequestState()
                return@launch
            }
            val accessToken = tokenResult.getOrThrow()
            val syncResult = withContext(Dispatchers.IO) {
                when (action) {
                    GoogleDriveAction.RestoreFromDrive ->
                        viewModel.restoreFromGoogleDrive(accessToken, resolvedAccountLabel)
                    GoogleDriveAction.UploadDiary ->
                        viewModel.uploadDiaryToGoogleDrive(accessToken, resolvedAccountLabel)
                    GoogleDriveAction.ManualSync ->
                        viewModel.manualSyncWithGoogleDrive(accessToken, resolvedAccountLabel)
                }
            }
            val syncMessage = syncResult.fold(
                onSuccess = {
                    if (!interactive) {
                        null
                    } else {
                        when (action) {
                            GoogleDriveAction.RestoreFromDrive -> PROMPT_GOOGLE_DRIVE_RESTORED
                            GoogleDriveAction.UploadDiary,
                            GoogleDriveAction.ManualSync -> PROMPT_GOOGLE_DRIVE_MANUAL_SYNCED
                        }
                    }
                },
                onFailure = {
                    if (interactive) it.message ?: PROMPT_BACKUP_FAILED else null
                }
            )
            if (interactive) {
                backupStatusMessage = syncMessage
            }
            if (shouldShowCompactStatus) {
                if (syncResult.isSuccess) {
                    showCompactDriveStatus(PROMPT_GOOGLE_DRIVE_SYNCED_COMPACT, 1500)
                } else {
                    showCompactDriveStatus(PROMPT_GOOGLE_DRIVE_SYNC_FAILED_COMPACT, 1800)
                }
            }
            isDriveRequestInFlight = false
            if (shouldBlockUi) {
                isDriveSyncInFlight = false
            }
        }
    }
    val recoverableAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val account = recoverableAuthAccount
        val action = recoverableAuthAction ?: GoogleDriveAction.RestoreFromDrive
        if (result.resultCode != Activity.RESULT_OK || account == null) {
            backupStatusMessage = PROMPT_GOOGLE_DRIVE_CONNECT_FAILED
            resetGoogleDriveRequestState()
            return@rememberLauncherForActivityResult
        }
        recoverableAuthAccount = null
        recoverableAuthAction = null
        pendingRecoverableAuthIntent = null
        syncSignedInGoogleAccount(account, action = action, interactive = true)
    }
    LaunchedEffect(pendingRecoverableAuthIntent) {
        val intent = pendingRecoverableAuthIntent ?: return@LaunchedEffect
        recoverableAuthLauncher.launch(intent)
    }
    val googleDriveSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            backupStatusMessage = PROMPT_GOOGLE_DRIVE_CONNECT_FAILED
            resetGoogleDriveRequestState()
            return@rememberLauncherForActivityResult
        }
        val account = runCatching {
            GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .getResult(ApiException::class.java)
        }.getOrElse {
            backupStatusMessage = it.message ?: PROMPT_GOOGLE_DRIVE_CONNECT_FAILED
            resetGoogleDriveRequestState()
            return@rememberLauncherForActivityResult
        }
        syncSignedInGoogleAccount(account, action = pendingGoogleDriveAction, interactive = true)
    }
    fun runGoogleDriveAction(action: GoogleDriveAction, interactive: Boolean) {
        if (isDriveRequestInFlight) return
        if (!googleDriveAuthorizationAvailable) {
            if (interactive) {
                backupStatusMessage = PROMPT_GOOGLE_DRIVE_CONNECT_FAILED
            }
            return
        }
        val existingAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (existingAccount != null) {
            syncSignedInGoogleAccount(existingAccount, action = action, interactive = interactive)
            return
        }
        if (!interactive) {
            return
        }
        pendingGoogleDriveAction = action
        isDriveRequestInFlight = true
        isDriveSyncInFlight = true
        googleDriveSignInLauncher.launch(googleSignInClient.signInIntent)
    }
    fun maybeRunAutoDiarySync() {
        if (uiState.googleDriveSyncMode != GoogleDriveSyncMode.AutoOnSave) return
        if (!uiState.isGoogleDriveLinked) return
        runGoogleDriveAction(
            action = GoogleDriveAction.UploadDiary,
            interactive = false
        )
    }
    fun disconnectGoogleDrive() {
        if (isDriveRequestInFlight) return
        isDriveRequestInFlight = true
        isDriveSyncInFlight = true
        googleSignInClient.signOut().addOnCompleteListener { task ->
            resetGoogleDriveRequestState()
            if (task.isSuccessful) {
                viewModel.disconnectGoogleDrive()
                backupStatusMessage = PROMPT_GOOGLE_DRIVE_LOGOUT_SUCCESS
            } else {
                backupStatusMessage = task.exception?.message ?: PROMPT_GOOGLE_DRIVE_LOGOUT_FAILED
            }
        }
    }
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

    DiaryappTheme(
        themePreset = uiState.themePreset,
        themeIntensity = uiState.themeIntensity
    ) {
        SystemBarAppearance(useDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (!uiState.isLoading && !uiState.isSecurityUnlocked) {
                AppLockScreen(
                    isFingerprintEnabled = uiState.fingerprintAuthEnabled,
                    isPasswordEnabled = uiState.passwordAuthEnabled,
                    onVerifyPassword = viewModel::verifyPassword,
                    onUnlock = viewModel::unlockSecurity
                )
            } else {
                DiaryNavigation(
                    navController = navController,
                    uiState = uiState,
                    isDriveSyncInFlight = isDriveSyncInFlight,
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
                    onSetThemeIntensity = viewModel::setThemeIntensity,
                    onSetNotificationsEnabled = viewModel::setNotificationsEnabled,
                    onSetGoogleDriveSyncMode = viewModel::setGoogleDriveSyncMode,
                    onSetFingerprintAuthEnabled = viewModel::setFingerprintAuthEnabled,
                    onSetPasswordAuthEnabled = viewModel::setPasswordAuthEnabled,
                    onSavePasswordCredential = viewModel::savePasswordCredential,
                    onVerifyPassword = viewModel::verifyPassword,
                    onConnectGoogleDrive = {
                        runGoogleDriveAction(
                            action = GoogleDriveAction.RestoreFromDrive,
                            interactive = true
                        )
                    },
                    onManualGoogleDriveSync = {
                        runGoogleDriveAction(
                            action = GoogleDriveAction.ManualSync,
                            interactive = true
                        )
                    },
                    onAutoSyncDiarySave = ::maybeRunAutoDiarySync,
                    onDisconnectGoogleDrive = ::disconnectGoogleDrive,
                    onClearAllData = viewModel::clearAllData,
                    onOpenReminderTimePicker = openReminderTimePicker,
                    onRemoveReminderTime = viewModel::removeReminderTime,
                    onCompleteOnboarding = viewModel::completeOnboarding
                )
            }
        }

        backupStatusMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { backupStatusMessage = null },
                confirmButton = {
                    TextButton(onClick = { backupStatusMessage = null }) {
                        Text(LABEL_CLOSE)
                    }
                },
                text = {
                    Text(message)
                }
            )
        }

        AnimatedVisibility(
            visible = compactDriveStatus != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 160)),
            exit = fadeOut(animationSpec = tween(durationMillis = 220)),
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    shape = ControlShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 6.dp,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.exactBorderColor(0.08f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (compactDriveStatus == PROMPT_GOOGLE_DRIVE_SYNCING_COMPACT) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = compactDriveStatus.orEmpty(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = JetBrainsMsGothicFontFamily
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        if (isDriveSyncInFlight) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    shape = PanelShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.exactBorderColor(0.08f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = PROMPT_GOOGLE_DRIVE_SYNCING,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = JetBrainsMsGothicFontFamily
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = PROMPT_GOOGLE_DRIVE_SYNCING_NOTE,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = JetBrainsMsGothicFontFamily
                            ),
                            color = MaterialTheme.colorScheme.secondaryTextColor(0.68f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
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
private fun AppLockScreen(
    isFingerprintEnabled: Boolean,
    isPasswordEnabled: Boolean,
    onVerifyPassword: (String) -> Boolean,
    onUnlock: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findComponentActivity()
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasAutoPrompted by remember { mutableStateOf(false) }

    val launchBiometricAuth = {
        when {
            activity == null -> {
                errorMessage = PROMPT_BIOMETRIC_UNAVAILABLE
            }
            biometricAvailabilityMessage(context) != null -> {
                errorMessage = biometricAvailabilityMessage(context)
            }
            else -> {
                errorMessage = null
                showBiometricPrompt(
                    activity = activity,
                    title = LABEL_UNLOCK,
                    subtitle = PROMPT_SECURITY_LOCK,
                    onSuccess = {
                        errorMessage = null
                        onUnlock()
                    },
                    onError = { promptError ->
                        errorMessage = promptError.ifBlank { null }
                    }
                )
            }
        }
    }

    LaunchedEffect(isFingerprintEnabled) {
        if (isFingerprintEnabled && !hasAutoPrompted) {
            hasAutoPrompted = true
            launchBiometricAuth()
        }
    }

    ScreenBackdrop {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = ScreenShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.exactBorderColor(0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = LABEL_DIARY_MENU_TITLE,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = JetBrainsMsGothicFontFamily
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = PROMPT_SECURITY_LOCK,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondaryTextColor(0.72f)
                    )
                    if (isFingerprintEnabled) {
                        FilledTonalButton(
                            onClick = launchBiometricAuth,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(LABEL_USE_FINGERPRINT)
                        }
                    }
                    if (isPasswordEnabled) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                if (errorMessage == PROMPT_PASSWORD_LOCK_FAILED) {
                                    errorMessage = null
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text(LABEL_PASSWORD_AUTH) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            )
                        )
                        Button(
                            onClick = {
                                if (onVerifyPassword(password)) {
                                    errorMessage = null
                                    password = ""
                                    onUnlock()
                                } else {
                                    errorMessage = PROMPT_PASSWORD_LOCK_FAILED
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = password.isNotBlank()
                        ) {
                            Text(LABEL_UNLOCK)
                        }
                    }
                    errorMessage?.let { message ->
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiaryNavigation(
    navController: NavHostController,
    uiState: DiaryUiState,
    isDriveSyncInFlight: Boolean,
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
    onSetThemeIntensity: (Float) -> Unit,
    onSetNotificationsEnabled: (Boolean) -> Unit,
    onSetGoogleDriveSyncMode: (GoogleDriveSyncMode) -> Unit,
    onSetFingerprintAuthEnabled: (Boolean) -> Unit,
    onSetPasswordAuthEnabled: (Boolean) -> Unit,
    onSavePasswordCredential: (String) -> Unit,
    onVerifyPassword: (String) -> Boolean,
    onConnectGoogleDrive: () -> Unit,
    onManualGoogleDriveSync: () -> Unit,
    onAutoSyncDiarySave: () -> Unit,
    onDisconnectGoogleDrive: () -> Unit,
    onClearAllData: () -> Unit,
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
    val openDateInViewMode: (LocalDate) -> Unit = { date ->
        onEnsureDraft(date)
        navController.navigate(diaryRoute(date))
    }
    val openDateInEditMode: (LocalDate) -> Unit = { date ->
        onEnsureDraft(date)
        navController.navigate(diaryRoute(date, edit = true))
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
            launchRouteResolved = true
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
                backupAccountEmail = uiState.backupAccountEmail,
                googleDriveSyncMode = uiState.googleDriveSyncMode,
                themePreset = uiState.themePreset,
                themeIntensity = uiState.themeIntensity,
                isInteractionLocked = isDriveSyncInFlight,
                onAddReminderTime = onOpenReminderTimePicker,
                onRemoveReminderTime = onRemoveReminderTime,
                onConnectGoogleDrive = onConnectGoogleDrive,
                onSetGoogleDriveSyncMode = onSetGoogleDriveSyncMode,
                onSelectThemePreset = onSetThemePreset,
                onThemeIntensityChange = onSetThemeIntensity,
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
                    onAutoSyncDiarySave()
                    finishTodayChat()
                },
                onNoPhotos = {
                    onMarkTodayNoPhotos()
                    onAutoSyncDiarySave()
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
                onOpenDate = openDateInViewMode,
                onEditDate = openDateInEditMode
            )
        }
        composable(ROUTE_DIARY_MENU) {
            DiaryMenuScreen(
                uiState = uiState,
                onOpenEntry = openDateInViewMode,
                onEditToday = {
                    onEnsureTodayDraft()
                    navController.navigate(diaryRoute(today, edit = true))
                },
                onOpenDiaryMenu = openDiaryMenu,
                onOpenCalendar = openCalendar,
                onOpenSettings = openSettings
            )
        }
        composable(ROUTE_SETTINGS) {
            SettingsScreen(
                themePreset = uiState.themePreset,
                reminderTimes = uiState.reminderTimes,
                notificationsEnabled = uiState.notificationsEnabled,
                fingerprintAuthEnabled = uiState.fingerprintAuthEnabled,
                passwordAuthEnabled = uiState.passwordAuthEnabled,
                hasPasswordCredential = uiState.hasPasswordCredential,
                backupAccountEmail = uiState.backupAccountEmail,
                isGoogleDriveLinked = uiState.isGoogleDriveLinked,
                googleDriveSyncMode = uiState.googleDriveSyncMode,
                onOpenThemeSelection = openThemePicker,
                onOpenReminderSettings = openReminderSettings,
                onSetNotificationsEnabled = onSetNotificationsEnabled,
                onSetGoogleDriveSyncMode = onSetGoogleDriveSyncMode,
                onSetFingerprintAuthEnabled = onSetFingerprintAuthEnabled,
                onSetPasswordAuthEnabled = onSetPasswordAuthEnabled,
                onSavePasswordCredential = onSavePasswordCredential,
                onVerifyPassword = onVerifyPassword,
                onConnectGoogleDrive = onConnectGoogleDrive,
                onManualGoogleDriveSync = onManualGoogleDriveSync,
                onDisconnectGoogleDrive = onDisconnectGoogleDrive,
                onClearAllData = onClearAllData,
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
                themeIntensity = uiState.themeIntensity,
                onSelectThemePreset = onSetThemePreset,
                onThemeIntensityChange = onSetThemeIntensity,
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
                onOpenDate = { selectedDate, editMode ->
                    onEnsureDraft(selectedDate)
                    navController.navigate(diaryRoute(selectedDate, edit = editMode)) {
                        popUpTo(backStackEntry.destination.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSaveText = { currentDate, text ->
                    onSaveDiaryText(currentDate, text)
                    onAutoSyncDiarySave()
                },
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
    backupAccountEmail: String?,
    googleDriveSyncMode: GoogleDriveSyncMode,
    themePreset: AppThemePreset,
    themeIntensity: Float,
    isInteractionLocked: Boolean,
    onAddReminderTime: () -> Unit,
    onRemoveReminderTime: (LocalTime) -> Unit,
    onConnectGoogleDrive: () -> Unit,
    onSetGoogleDriveSyncMode: (GoogleDriveSyncMode) -> Unit,
    onSelectThemePreset: (AppThemePreset) -> Unit,
    onThemeIntensityChange: (Float) -> Unit,
    onFinish: () -> Unit
) {
    val today = LocalDate.now()
    val listState = rememberLazyListState()
    var hasSkippedReminderSelection by rememberSaveable { mutableStateOf(false) }
    var hasChosenGoogleDriveOption by rememberSaveable { mutableStateOf(false) }
    var hasSkippedGoogleDriveLinking by rememberSaveable { mutableStateOf(false) }
    var hasChosenSyncModeOption by rememberSaveable { mutableStateOf(false) }
    val hasAdvancedToDriveStep = reminderTimes.isNotEmpty() || hasSkippedReminderSelection
    val hasAdvancedToSyncModeStep =
        hasAdvancedToDriveStep && hasChosenGoogleDriveOption && !hasSkippedGoogleDriveLinking
    val hasAdvancedToThemeStep =
        hasAdvancedToDriveStep && hasChosenGoogleDriveOption &&
            (hasSkippedGoogleDriveLinking || hasChosenSyncModeOption)
    var showReminderIntro by rememberSaveable { mutableStateOf(false) }
    var showReminderNote by rememberSaveable { mutableStateOf(false) }
    var showGoogleDriveIntro by rememberSaveable { mutableStateOf(false) }
    var showGoogleDriveNote by rememberSaveable { mutableStateOf(false) }
    var showSyncModeIntro by rememberSaveable { mutableStateOf(false) }
    var showSyncModeNote by rememberSaveable { mutableStateOf(false) }
    var showThemeIntro by rememberSaveable { mutableStateOf(false) }
    var showThemeNote by rememberSaveable { mutableStateOf(false) }
    var showFinishPrompt by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isInteractionLocked) {
        if (isInteractionLocked) return@LaunchedEffect
        if (!showReminderIntro) {
            kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
            showReminderIntro = true
        }
        if (!showReminderNote) {
            kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
            showReminderNote = true
        }
    }

    LaunchedEffect(hasAdvancedToDriveStep, showReminderNote, isInteractionLocked) {
        if (isInteractionLocked) return@LaunchedEffect
        if (!hasAdvancedToDriveStep || !showReminderNote || showGoogleDriveIntro) return@LaunchedEffect
        kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
        showGoogleDriveIntro = true
        kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
        showGoogleDriveNote = true
    }

    LaunchedEffect(hasAdvancedToSyncModeStep, showGoogleDriveNote, isInteractionLocked) {
        if (isInteractionLocked) return@LaunchedEffect
        if (!hasAdvancedToSyncModeStep || !showGoogleDriveNote || showSyncModeIntro) return@LaunchedEffect
        kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
        showSyncModeIntro = true
        kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
        showSyncModeNote = true
    }

    LaunchedEffect(
        hasAdvancedToThemeStep,
        hasSkippedGoogleDriveLinking,
        showGoogleDriveNote,
        showSyncModeNote,
        isInteractionLocked
    ) {
        if (isInteractionLocked) return@LaunchedEffect
        val canAdvanceToTheme =
            if (hasSkippedGoogleDriveLinking) showGoogleDriveNote else showSyncModeNote
        if (!hasAdvancedToThemeStep || !canAdvanceToTheme || showThemeIntro) return@LaunchedEffect
        kotlinx.coroutines.delay(
            if (hasSkippedGoogleDriveLinking) ONBOARDING_MESSAGE_DELAY_MS
            else ONBOARDING_THEME_TRANSITION_DELAY_MS
        )
        showThemeIntro = true
        kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
        showThemeNote = true
        kotlinx.coroutines.delay(ONBOARDING_MESSAGE_DELAY_MS)
        showFinishPrompt = true
    }

    val visibleOnboardingItemCount by remember(
        showReminderIntro,
        showReminderNote,
        showGoogleDriveIntro,
        showGoogleDriveNote,
        showSyncModeIntro,
        showSyncModeNote,
        showThemeIntro,
        showThemeNote,
        showFinishPrompt
    ) {
        derivedStateOf {
            var count = 0
            if (showReminderIntro) count += 1
            if (showReminderNote) count += 2
            if (showGoogleDriveIntro) count += 1
            if (showGoogleDriveNote) count += 2
            if (showSyncModeIntro) count += 1
            if (showSyncModeNote) count += 2
            if (showThemeIntro) count += 1
            if (showThemeNote) count += 1
            if (showFinishPrompt) count += 1
            count
        }
    }

    LaunchedEffect(visibleOnboardingItemCount) {
        if (visibleOnboardingItemCount > 0) {
            listState.animateScrollToItem(visibleOnboardingItemCount - 1)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (showFinishPrompt) {
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
                        AnimatedChatContent(animationKey = "onboarding_start_button") {
                            Button(
                                onClick = onFinish,
                                shape = ControlShape
                            ) {
                                Text(LABEL_START)
                            }
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
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                if (showReminderIntro) {
                    item(key = "onboarding_reminder_intro") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_REMINDER,
                            isBot = true,
                            animationKey = "onboarding_reminder_intro"
                        )
                    }
                }
                if (showReminderNote) {
                    item(key = "onboarding_reminder_note") {
                        MessageBubble(
                            text = PROMPT_NOTIFICATION_TIME_NOTE,
                            isBot = true,
                            animationKey = "onboarding_reminder_note"
                        )
                    }
                    item(key = "onboarding_reminder_card") {
                        AnimatedChatContent(animationKey = "onboarding_reminder_card") {
                            OnboardingReminderCard(
                                reminderTimes = reminderTimes,
                                onPickTime = onAddReminderTime,
                                onRemoveReminderTime = onRemoveReminderTime,
                                onSkip = { hasSkippedReminderSelection = true }
                            )
                        }
                    }
                }
                if (showGoogleDriveIntro) {
                    item(key = "onboarding_google_drive_intro") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_GOOGLE_DRIVE,
                            isBot = true,
                            animationKey = "onboarding_google_drive_intro"
                        )
                    }
                }
                if (showGoogleDriveNote) {
                    item(key = "onboarding_google_drive_note") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_GOOGLE_DRIVE_PERMISSION,
                            isBot = true,
                            animationKey = "onboarding_google_drive_note"
                        )
                    }
                    item(key = "onboarding_google_drive_card") {
                        AnimatedChatContent(animationKey = "onboarding_google_drive_card") {
                            OnboardingGoogleDriveCard(
                                backupAccountEmail = backupAccountEmail,
                                onConnect = {
                                    hasChosenGoogleDriveOption = true
                                    hasSkippedGoogleDriveLinking = false
                                    onConnectGoogleDrive()
                                },
                                onSkip = {
                                    hasChosenGoogleDriveOption = true
                                    hasSkippedGoogleDriveLinking = true
                                }
                            )
                        }
                    }
                }
                if (showSyncModeIntro) {
                    item(key = "onboarding_sync_mode_intro") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_SYNC_MODE,
                            isBot = true,
                            animationKey = "onboarding_sync_mode_intro"
                        )
                    }
                }
                if (showSyncModeNote) {
                    item(key = "onboarding_sync_mode_note") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_SYNC_MODE_NOTE,
                            isBot = true,
                            animationKey = "onboarding_sync_mode_note"
                        )
                    }
                    item(key = "onboarding_sync_mode_card") {
                        AnimatedChatContent(animationKey = "onboarding_sync_mode_card") {
                            OnboardingSyncModeCard(
                                selectedMode = googleDriveSyncMode,
                                onSelectMode = {
                                    hasChosenSyncModeOption = true
                                    onSetGoogleDriveSyncMode(it)
                                }
                            )
                        }
                    }
                }
                if (showThemeIntro) {
                    item(key = "onboarding_theme_intro") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_THEME,
                            isBot = true,
                            animationKey = "onboarding_theme_intro"
                        )
                    }
                }
                if (showThemeNote) {
                    item(key = "onboarding_theme_card") {
                        AnimatedChatContent(animationKey = "onboarding_theme_card") {
                            ThemeSelectionInlineCard(
                                selectedThemePreset = themePreset,
                                themeIntensity = themeIntensity,
                                onSelectThemePreset = onSelectThemePreset,
                                onThemeIntensityChange = onThemeIntensityChange
                            )
                        }
                    }
                }
                if (showFinishPrompt) {
                    item(key = "onboarding_finish_prompt") {
                        MessageBubble(
                            text = PROMPT_ONBOARDING_FINISH,
                            isBot = true,
                            animationKey = "onboarding_finish_prompt"
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
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
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
                            .clip(ControlShape)
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.exactBorderColor(),
                                shape = ControlShape
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
                                    color = MaterialTheme.colorScheme.secondaryTextColor(0.46f)
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
                shape = ControlShape,
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
                shape = ControlShape
            ) {
                Text(if (hasPhotos) LABEL_ADD_MEDIA else LABEL_PICK_MEDIA)
            }
            OutlinedButton(
                onClick = if (hasPhotos) onFinishPhotos else onNoPhotos,
                modifier = Modifier.weight(1f),
                shape = ControlShape
            ) {
                Text(if (hasPhotos) LABEL_FINISH else LABEL_NO_PHOTO)
            }
        }
    }
}

@Composable
private fun MessageBubble(
    text: String,
    isBot: Boolean,
    animationKey: String = "${if (isBot) "bot" else "user"}:$text"
) {
    AnimatedChatContent(animationKey = animationKey) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isBot) Arrangement.Start else Arrangement.End
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isBot) {
                        MaterialTheme.colorScheme.surface
                    } else if (MaterialTheme.colorScheme.isPureMonochromeTheme()) {
                        MaterialTheme.colorScheme.primary
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
}

@Composable
private fun AnimatedChatContent(
    animationKey: String,
    content: @Composable () -> Unit
) {
    var hasAnimated by rememberSaveable(animationKey) { mutableStateOf(false) }
    if (hasAnimated) {
        content()
        return
    }

    val visibleState = remember(animationKey) {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    LaunchedEffect(animationKey, visibleState.isIdle, visibleState.currentState) {
        if (visibleState.isIdle && visibleState.currentState) {
            hasAnimated = true
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 360)
        ) + slideInVertically(
            animationSpec = tween(durationMillis = 360),
            initialOffsetY = { fullHeight -> fullHeight / 4 }
        )
    ) {
        content()
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
    onOpenDate: (LocalDate) -> Unit,
    onEditDate: (LocalDate) -> Unit
) {
    val days = remember(uiState.visibleMonth) { calendarDays(uiState.visibleMonth) }
    val colorScheme = MaterialTheme.colorScheme
    val isMonochrome = colorScheme.isPureMonochromeTheme()
    val calendarShellColor = colorScheme.calendarShellColor()
    val calendarShellContentColor = colorScheme.calendarShellContentColor()
    var selectedDate by rememberSaveable(uiState.visibleMonth) { mutableStateOf<LocalDate?>(null) }
    var isMonthPickerVisible by rememberSaveable { mutableStateOf(false) }
    val selectedEntry = selectedDate?.let(uiState::entryFor)

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = LABEL_CALENDAR_TITLE,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    ),
                    color = colorScheme.onBackground
                )
                UtilityActionButton(
                    text = LABEL_EDIT,
                    onClick = { selectedDate?.let(onEditDate) },
                    enabled = selectedDate != null,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = ScreenShape,
                colors = CardDefaults.cardColors(containerColor = calendarShellColor),
                border = BorderStroke(1.dp, colorScheme.exactBorderColor())
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onPreviousMonth,
                            colors = ButtonDefaults.textButtonColors(contentColor = calendarShellContentColor)
                        ) { Text(LABEL_PREVIOUS_MONTH) }
                        Row(
                            modifier = Modifier
                                .clip(ControlShape)
                                .background(
                                    if (isMonochrome) colorScheme.background
                                    else colorScheme.primary.copy(alpha = 0.08f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isMonochrome) colorScheme.onBackground else colorScheme.primary.copy(alpha = 0.22f),
                                    shape = ControlShape
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
                                ),
                                color = if (isMonochrome) colorScheme.onBackground else colorScheme.onSurface
                            )
                            Text(
                                text = "\u25be",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = JetBrainsMsGothicFontFamily
                                ),
                                color = if (isMonochrome) colorScheme.onBackground else colorScheme.primary
                            )
                        }
                        TextButton(
                            onClick = onNextMonth,
                            colors = ButtonDefaults.textButtonColors(contentColor = calendarShellContentColor)
                        ) { Text(LABEL_NEXT_MONTH) }
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
                                style = MaterialTheme.typography.labelLarge,
                                color = calendarShellContentColor
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
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
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

            selectedDate?.let { date ->
                Spacer(modifier = Modifier.height(14.dp))
                CalendarSelectedEntryCard(
                    date = date,
                    entry = selectedEntry,
                    onClick = { onOpenDate(date) }
                )
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
                                shape = ControlShape,
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (isSelected) {
                                        if (MaterialTheme.colorScheme.isPureMonochromeTheme()) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                                        }
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                    contentColor = if (isSelected) {
                                        if (MaterialTheme.colorScheme.isPureMonochromeTheme()) {
                                            MaterialTheme.colorScheme.background
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
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
private fun CalendarSelectedEntryCard(
    date: LocalDate,
    entry: DiaryEntry?,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val previewText = entry?.userText?.takeIf { it.isNotBlank() } ?: PROMPT_EMPTY_DIARY
    val previewPhoto = entry?.photoUris?.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.exactBorderColor())
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("yy/MM/dd", Locale.JAPAN)),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    ),
                    color = colorScheme.onSurface
                )
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.secondaryTextColor(0.9f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (previewPhoto != null) {
                val previewMediaKind = rememberDiaryMediaKind(previewPhoto)
                Box(
                    modifier = Modifier
                        .size(width = 88.dp, height = 88.dp)
                        .clip(ControlShape)
                ) {
                    AsyncImage(
                        model = rememberMediaModel(
                            context = context,
                            uriString = previewPhoto,
                            mediaKind = previewMediaKind
                        ),
                        contentDescription = "カレンダープレビュー",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if (previewMediaKind == DiaryMediaKind.Video) {
                        VideoBadge(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiaryMenuScreen(
    uiState: DiaryUiState,
    onOpenEntry: (LocalDate) -> Unit,
    onEditToday: () -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val completedEntries = remember(uiState.entries) {
        uiState.entries.filter { it.isCompleted }.sortedByDescending(DiaryEntry::date)
    }
    val monthSections = remember(completedEntries) { buildDiaryMonthSections(completedEntries) }
    val diaryMenuListItems = remember(monthSections) { buildDiaryMenuListItems(monthSections) }
    val listState = rememberLazyListState()

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEditToday,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_diary),
                    contentDescription = LABEL_EDIT
                )
            }
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
            Spacer(modifier = Modifier.height(12.dp))

            if (completedEntries.isEmpty()) {
                Card(
                    shape = PanelShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = PROMPT_DIARY_MENU_EMPTY,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        monthSections.forEach { section ->
                            stickyHeader(key = "diary-month-${section.month}") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(bottom = 10.dp)
                                ) {
                                    DiaryMenuMonthHeader(
                                        month = section.month,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            items(
                                count = section.entries.size,
                                key = { index -> section.entries[index].date.toString() }
                            ) { index ->
                                DiaryMenuEntryCard(
                                    entry = section.entries[index],
                                    onClick = { onOpenEntry(section.entries[index].date) }
                                )
                            }
                        }
                    }

                    if (completedEntries.size > 50) {
                        DiaryMenuJumpRail(
                            listItems = diaryMenuListItems,
                            listState = listState,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

private data class DiaryMonthSection(
    val month: YearMonth,
    val entries: List<DiaryEntry>,
    val listIndex: Int
)

private data class DiaryMenuListItemMeta(
    val isMonthHeader: Boolean,
    val hasPreviewPhoto: Boolean
)

private fun buildDiaryMonthSections(entries: List<DiaryEntry>): List<DiaryMonthSection> =
    entries
        .groupBy { YearMonth.from(it.date) }
        .toList()
        .sortedByDescending { it.first }
        .runningFold(0 to emptyList<DiaryMonthSection>()) { (listIndex, sections), (month, groupedEntries) ->
            val nextSection = DiaryMonthSection(
                month = month,
                entries = groupedEntries.sortedByDescending(DiaryEntry::date),
                listIndex = listIndex
            )
            (listIndex + 1 + groupedEntries.size) to (sections + nextSection)
        }
        .last()
        .second

private fun buildDiaryMenuListItems(monthSections: List<DiaryMonthSection>): List<DiaryMenuListItemMeta> =
    buildList {
        monthSections.forEach { section ->
            add(DiaryMenuListItemMeta(isMonthHeader = true, hasPreviewPhoto = false))
            section.entries.forEach { entry ->
                add(
                    DiaryMenuListItemMeta(
                        isMonthHeader = false,
                        hasPreviewPhoto = entry.photoUris.isNotEmpty()
                    )
                )
            }
        }
    }

@Composable
private fun DiaryMenuMonthHeader(
    month: YearMonth,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        modifier = modifier,
        shape = ControlShape,
        color = colorScheme.surface,
        shadowElevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.exactBorderColor(0.08f)
        )
    ) {
        Text(
            text = month.format(DateTimeFormatter.ofPattern("yyyy\u5e74MM\u6708", Locale.JAPAN)),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = JetBrainsMsGothicFontFamily
            ),
            color = colorScheme.onSurface
        )
    }
}

@Composable
private fun DiaryMenuEntryCard(
    entry: DiaryEntry,
    onClick: () -> Unit
) {
    val previewPhoto = entry.photoUris.firstOrNull()
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.exactBorderColor())
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
                    text = entry.date.format(DateTimeFormatter.ofPattern("dd\u65e5", Locale.JAPAN)),
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
                val previewMediaKind = rememberDiaryMediaKind(previewPhoto)
                Box(
                    modifier = Modifier
                        .size(width = 88.dp, height = 88.dp)
                        .clip(ControlShape)
                ) {
                    AsyncImage(
                        model = rememberMediaModel(
                            context = context,
                            uriString = previewPhoto,
                            mediaKind = previewMediaKind
                        ),
                        contentDescription = "\u65e5\u8a18\u30d7\u30ec\u30d3\u30e5\u30fc",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if (previewMediaKind == DiaryMediaKind.Video) {
                        VideoBadge(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiaryMenuJumpRail(
    listItems: List<DiaryMenuListItemMeta>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    if (listItems.size <= 1) return

    val colorScheme = MaterialTheme.colorScheme
    val density = LocalDensity.current
    var isRailInteracting by remember { mutableStateOf(false) }
    val indicatorAlpha = remember { Animatable(0f) }
    val observedItemHeights = remember { mutableStateMapOf<Int, Int>() }
    val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
    SideEffect {
        visibleItemsInfo.forEach { itemInfo ->
            if (observedItemHeights[itemInfo.index] != itemInfo.size) {
                observedItemHeights[itemInfo.index] = itemInfo.size
            }
        }
    }
    val itemSpacingPx = with(density) { 12.dp.roundToPx() }
    val contentBottomPaddingPx = with(density) { 12.dp.roundToPx() }
    val defaultHeaderHeightPx = with(density) { 56.dp.roundToPx() }
    val defaultPhotoEntryHeightPx = with(density) { 124.dp.roundToPx() }
    val defaultTextOnlyEntryHeightPx = with(density) { 108.dp.roundToPx() }
    val estimatedItemHeights by remember(listItems, observedItemHeights) {
        derivedStateOf {
            val observedHeaderHeight = observedItemHeights
                .filterKeys { index -> listItems.getOrNull(index)?.isMonthHeader == true }
                .values
                .average()
                .takeIf { !it.isNaN() }
                ?.roundToInt()
            val observedPhotoEntryHeight = observedItemHeights
                .filterKeys { index ->
                    listItems.getOrNull(index)?.let { !it.isMonthHeader && it.hasPreviewPhoto } == true
                }
                .values
                .average()
                .takeIf { !it.isNaN() }
                ?.roundToInt()
            val observedTextOnlyEntryHeight = observedItemHeights
                .filterKeys { index ->
                    listItems.getOrNull(index)?.let { !it.isMonthHeader && !it.hasPreviewPhoto } == true
                }
                .values
                .average()
                .takeIf { !it.isNaN() }
                ?.roundToInt()

            listItems.mapIndexed { index, item ->
                observedItemHeights[index] ?: when {
                    item.isMonthHeader -> observedHeaderHeight ?: defaultHeaderHeightPx
                    item.hasPreviewPhoto -> observedPhotoEntryHeight ?: defaultPhotoEntryHeightPx
                    else -> observedTextOnlyEntryHeight ?: defaultTextOnlyEntryHeightPx
                }
            }
        }
    }
    val cumulativeOffsetsPx by remember(estimatedItemHeights, itemSpacingPx) {
        derivedStateOf {
            buildList {
                var accumulated = 0
                estimatedItemHeights.forEachIndexed { index, itemHeight ->
                    add(accumulated)
                    accumulated += itemHeight
                    if (index < estimatedItemHeights.lastIndex) {
                        accumulated += itemSpacingPx
                    }
                }
            }
        }
    }
    val maxScrollPx by remember(listState, estimatedItemHeights, itemSpacingPx, contentBottomPaddingPx) {
        derivedStateOf {
            val viewportHeight =
                (listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset)
                    .coerceAtLeast(1)
            val totalContentHeight =
                (estimatedItemHeights.sum() +
                    itemSpacingPx * (estimatedItemHeights.size - 1).coerceAtLeast(0) +
                    contentBottomPaddingPx).coerceAtLeast(viewportHeight)
            (totalContentHeight - viewportHeight).coerceAtLeast(1)
        }
    }
    val rawScrollProgress by remember(listState, visibleItemsInfo, cumulativeOffsetsPx, maxScrollPx) {
        derivedStateOf {
            if (listItems.size <= 1) {
                return@derivedStateOf 0f
            }
            if (!listState.canScrollBackward) {
                return@derivedStateOf 0f
            }
            if (!listState.canScrollForward) {
                return@derivedStateOf 1f
            }

            val viewportStart = listState.layoutInfo.viewportStartOffset
            val anchorItem = visibleItemsInfo
                .sortedBy { it.index }
                .firstOrNull { itemInfo ->
                    listItems.getOrNull(itemInfo.index)?.isMonthHeader == false
                }
                ?: visibleItemsInfo.firstOrNull()
            val currentScroll = anchorItem
                ?.let { itemInfo ->
                    cumulativeOffsetsPx.getOrNull(itemInfo.index)?.let { itemTop ->
                        itemTop - (itemInfo.offset - viewportStart)
                    }
                }
                ?.coerceIn(0, maxScrollPx)
                ?: 0

            (currentScroll.toFloat() / maxScrollPx.toFloat()).coerceIn(0f, 1f)
        }
    }

    LaunchedEffect(listState.isScrollInProgress, isRailInteracting) {
        if (listState.isScrollInProgress || isRailInteracting) {
            indicatorAlpha.stop()
            indicatorAlpha.snapTo(1f)
        } else if (indicatorAlpha.value > 0f) {
            kotlinx.coroutines.delay(1500)
            indicatorAlpha.stop()
            indicatorAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .width(96.dp)
    ) {
        val thumbSize = 54.dp
        val thumbSizePx = with(density) { thumbSize.toPx() }
        val availableDragHeightPx = (with(density) { maxHeight.toPx() } - thumbSizePx).coerceAtLeast(1f)
        var dragThumbTopPx by remember { mutableStateOf(availableDragHeightPx * rawScrollProgress) }
        val displayedThumbTopPx = if (isRailInteracting) dragThumbTopPx else availableDragHeightPx * rawScrollProgress

        LaunchedEffect(rawScrollProgress, isRailInteracting, availableDragHeightPx) {
            if (!isRailInteracting) {
                dragThumbTopPx = availableDragHeightPx * rawScrollProgress
            }
        }

        if (indicatorAlpha.value > 0.01f) {
            Canvas(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 28.dp)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = displayedThumbTopPx.roundToInt()
                        )
                    }
                    .size(thumbSize)
                    .graphicsLayer { alpha = indicatorAlpha.value }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { dragAmount ->
                            val contentDeltaPx =
                                (dragAmount / availableDragHeightPx) * maxScrollPx.toFloat()
                            val consumedContentPx = listState.dispatchRawDelta(contentDeltaPx)
                            val consumedThumbDeltaPx =
                                (consumedContentPx / maxScrollPx.toFloat()) * availableDragHeightPx
                            dragThumbTopPx =
                                (dragThumbTopPx + consumedThumbDeltaPx).coerceIn(0f, availableDragHeightPx)
                        },
                        onDragStarted = {
                            isRailInteracting = true
                            dragThumbTopPx = availableDragHeightPx * rawScrollProgress
                        },
                        onDragStopped = {
                            isRailInteracting = false
                        }
                    )
            ) {
                val radius = size.minDimension / 2f
                val arrowHalfWidth = radius * 0.35f
                val arrowHeight = radius * 0.38f
                val arrowGap = radius * 0.18f
                val thumbCenterX = size.width / 2f
                val thumbCenterY = size.height / 2f
                val indicatorColor = colorScheme.primary.copy(alpha = indicatorAlpha.value)
                val arrowColor = colorScheme.onPrimary.copy(alpha = indicatorAlpha.value)
                drawCircle(
                    color = indicatorColor,
                    radius = radius,
                    center = Offset(thumbCenterX, thumbCenterY)
                )
                drawPath(
                    path = Path().apply {
                        moveTo(thumbCenterX, thumbCenterY - arrowGap - arrowHeight)
                        lineTo(thumbCenterX - arrowHalfWidth, thumbCenterY - arrowGap)
                        lineTo(thumbCenterX + arrowHalfWidth, thumbCenterY - arrowGap)
                        close()
                    },
                    color = arrowColor
                )
                drawPath(
                    path = Path().apply {
                        moveTo(thumbCenterX, thumbCenterY + arrowGap + arrowHeight)
                        lineTo(thumbCenterX - arrowHalfWidth, thumbCenterY + arrowGap)
                        lineTo(thumbCenterX + arrowHalfWidth, thumbCenterY + arrowGap)
                        close()
                    },
                    color = arrowColor
                )
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    themePreset: AppThemePreset,
    reminderTimes: List<LocalTime>,
    notificationsEnabled: Boolean,
    fingerprintAuthEnabled: Boolean,
    passwordAuthEnabled: Boolean,
    hasPasswordCredential: Boolean,
    backupAccountEmail: String?,
    isGoogleDriveLinked: Boolean,
    googleDriveSyncMode: GoogleDriveSyncMode,
    onOpenThemeSelection: () -> Unit,
    onOpenReminderSettings: () -> Unit,
    onSetNotificationsEnabled: (Boolean) -> Unit,
    onSetGoogleDriveSyncMode: (GoogleDriveSyncMode) -> Unit,
    onSetFingerprintAuthEnabled: (Boolean) -> Unit,
    onSetPasswordAuthEnabled: (Boolean) -> Unit,
    onSavePasswordCredential: (String) -> Unit,
    onVerifyPassword: (String) -> Boolean,
    onConnectGoogleDrive: () -> Unit,
    onManualGoogleDriveSync: () -> Unit,
    onDisconnectGoogleDrive: () -> Unit,
    onClearAllData: () -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findComponentActivity()
    val scrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme
    val hasGoogleDriveSession = isGoogleDriveLinked || !backupAccountEmail.isNullOrBlank()
    var legalDialogTitle by rememberSaveable { mutableStateOf<String?>(null) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var securityDialogMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordDialogMode by remember { mutableStateOf<PasswordDialogMode?>(null) }
    var showSyncModeDialog by rememberSaveable { mutableStateOf(false) }

    val openPasswordSetup = {
        passwordDialogMode = if (hasPasswordCredential) {
            PasswordDialogMode.Change
        } else {
            PasswordDialogMode.CreateAndEnable
        }
    }

    val handleFingerprintToggle: (Boolean) -> Unit = { enabled ->
        if (!enabled) {
            onSetFingerprintAuthEnabled(false)
        } else {
            val availabilityMessage = biometricAvailabilityMessage(context)
            when {
                activity == null -> securityDialogMessage = PROMPT_BIOMETRIC_UNAVAILABLE
                availabilityMessage != null -> securityDialogMessage = availabilityMessage
                else -> {
                    showBiometricPrompt(
                        activity = activity,
                        title = LABEL_FINGERPRINT_AUTH,
                        subtitle = PROMPT_SECURITY_LOCK,
                        onSuccess = { onSetFingerprintAuthEnabled(true) },
                        onError = { message ->
                            if (message.isNotBlank()) {
                                securityDialogMessage = message
                            }
                        }
                    )
                }
            }
        }
    }

    val handlePasswordToggle: (Boolean) -> Unit = { enabled ->
        if (!enabled) {
            onSetPasswordAuthEnabled(false)
        } else if (hasPasswordCredential) {
            onSetPasswordAuthEnabled(true)
        } else {
            passwordDialogMode = PasswordDialogMode.CreateAndEnable
        }
    }

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
                .verticalScroll(scrollState)
                .padding(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = LABEL_SETTINGS_TITLE,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                )
            )

            SettingsSection(
                title = LABEL_SETTINGS_SECTION_NOTIFICATION_THEME
            ) {
                SettingsActionRow(
                    title = LABEL_NOTIFICATION_TIME,
                    trailingText = formatReminderSummary(reminderTimes),
                    onClick = onOpenReminderSettings
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                SettingsSwitchRow(
                    title = LABEL_NOTIFICATION_TOGGLE,
                    checked = notificationsEnabled,
                    onCheckedChange = onSetNotificationsEnabled
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
            SettingsActionRow(
                title = LABEL_THEME,
                trailingContent = {
                    SettingsThemeSwatch(
                        previewColor = paletteForTheme(themePreset, DEFAULT_THEME_INTENSITY).previewColors.firstOrNull()
                            ?: colorScheme.primary,
                        borderColor = colorScheme.exactBorderColor(0.24f)
                    )
                },
                onClick = onOpenThemeSelection
            )
            }

            SettingsSection(
                title = LABEL_SETTINGS_SECTION_IMPORTANT
            ) {
                SettingsActionRow(
                    title = LABEL_DELETE_ALL_DATA,
                    titleColor = colorScheme.error,
                    onClick = { showDeleteDialog = true }
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                SettingsActionRow(
                    title = LABEL_TERMS_OF_USE,
                    onClick = { legalDialogTitle = LABEL_TERMS_OF_USE }
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                SettingsActionRow(
                    title = LABEL_PRIVACY_POLICY,
                    onClick = { legalDialogTitle = LABEL_PRIVACY_POLICY }
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                SettingsActionRow(
                    title = LABEL_COMMERCIAL_DISCLOSURE,
                    onClick = { legalDialogTitle = LABEL_COMMERCIAL_DISCLOSURE }
                )
            }

            SettingsSection(
                title = LABEL_SETTINGS_SECTION_SECURITY
            ) {
                SettingsSwitchRow(
                    title = LABEL_FINGERPRINT_AUTH,
                    checked = fingerprintAuthEnabled,
                    onCheckedChange = handleFingerprintToggle
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                SettingsSwitchRow(
                    title = LABEL_PASSWORD_AUTH,
                    checked = passwordAuthEnabled,
                    onCheckedChange = handlePasswordToggle,
                    onClick = openPasswordSetup
                )
            }

            SettingsSection(
                title = LABEL_SETTINGS_SECTION_BACKUP
            ) {
                SettingsActionRow(
                    title = LABEL_GOOGLE_ACCOUNT,
                    trailingText = backupAccountEmail ?: if (isGoogleDriveLinked) LABEL_CONNECTED else LABEL_UNSET,
                    onClick = onConnectGoogleDrive
                )
                HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                SettingsActionRow(
                    title = LABEL_SYNC_METHOD,
                    trailingText = googleDriveSyncMode.label,
                    onClick = { showSyncModeDialog = true }
                )
                if (hasGoogleDriveSession) {
                    HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                    SettingsActionRow(
                        title = LABEL_SYNC_NOW,
                        onClick = onManualGoogleDriveSync
                    )
                    HorizontalDivider(color = colorScheme.exactBorderColor(0.08f))
                    SettingsActionRow(
                        title = LABEL_LOGOUT,
                        onClick = onDisconnectGoogleDrive
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = LABEL_SETTING_VERSION,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                ),
                color = colorScheme.secondaryTextColor(0.52f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    passwordDialogMode?.let { mode ->
        PasswordSetupDialog(
            title = if (mode == PasswordDialogMode.Change) LABEL_CHANGE_PASSWORD else LABEL_SET_PASSWORD,
            requireCurrentPassword = mode == PasswordDialogMode.Change,
            onDismiss = { passwordDialogMode = null },
            onConfirm = { currentPassword, newPassword ->
                if (mode == PasswordDialogMode.Change && !onVerifyPassword(currentPassword)) {
                    PROMPT_PASSWORD_INCORRECT
                } else {
                    onSavePasswordCredential(newPassword)
                    if (mode == PasswordDialogMode.CreateAndEnable || passwordAuthEnabled) {
                        onSetPasswordAuthEnabled(true)
                    }
                    passwordDialogMode = null
                    null
                }
            }
        )
    }

    legalDialogTitle?.let { title ->
        AlertDialog(
            onDismissRequest = { legalDialogTitle = null },
            confirmButton = {
                TextButton(onClick = { legalDialogTitle = null }) {
                    Text(LABEL_CLOSE)
                }
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    )
                )
            },
            text = {
                Box(
                    modifier = Modifier
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(legalBodyForTitle(title))
                }
            }
        )
    }

    securityDialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { securityDialogMessage = null },
            confirmButton = {
                TextButton(onClick = { securityDialogMessage = null }) {
                    Text(LABEL_CLOSE)
                }
            },
            text = {
                Text(message)
            }
        )
    }

    if (showSyncModeDialog) {
        SyncModeSelectionDialog(
            selectedMode = googleDriveSyncMode,
            onSelectMode = {
                onSetGoogleDriveSyncMode(it)
                showSyncModeDialog = false
            },
            onDismiss = { showSyncModeDialog = false }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onClearAllData()
                    }
                ) {
                    Text(
                        text = LABEL_DELETE,
                        color = colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(LABEL_CANCEL)
                }
            },
            title = {
                Text(
                    text = LABEL_DELETE_ALL_DATA,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    ),
                    color = colorScheme.error
                )
            },
            text = {
                Text(PROMPT_DELETE_ALL_DATA)
            }
        )
    }
}

private enum class PasswordDialogMode {
    CreateAndEnable,
    Change
}

@Composable
private fun PasswordSetupDialog(
    title: String,
    requireCurrentPassword: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (currentPassword: String, newPassword: String) -> String?
) {
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val passwordFieldTextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontFamily = JetBrainsMsGothicFontFamily
    )
    val passwordFieldLabelStyle = MaterialTheme.typography.bodySmall.copy(
        fontFamily = JetBrainsMsGothicFontFamily
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        newPassword.length < 4 -> errorMessage = PROMPT_PASSWORD_MINIMUM
                        newPassword != confirmPassword -> errorMessage = PROMPT_PASSWORD_MISMATCH
                        else -> {
                            val result = onConfirm(currentPassword, newPassword)
                            if (result == null) {
                                onDismiss()
                            } else {
                                errorMessage = result
                            }
                        }
                    }
                }
            ) {
                Text(LABEL_SAVE)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(LABEL_CANCEL)
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                )
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (requireCurrentPassword) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = {
                            currentPassword = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp),
                        singleLine = true,
                        textStyle = passwordFieldTextStyle,
                        label = { Text(text = LABEL_CURRENT_PASSWORD, style = passwordFieldLabelStyle) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        shape = ControlShape
                    )
                }
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    singleLine = true,
                    textStyle = passwordFieldTextStyle,
                    label = { Text(text = LABEL_NEW_PASSWORD, style = passwordFieldLabelStyle) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    shape = ControlShape
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    singleLine = true,
                    textStyle = passwordFieldTextStyle,
                    label = { Text(text = LABEL_CONFIRM_PASSWORD, style = passwordFieldLabelStyle) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    shape = ControlShape
                )
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
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
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.exactBorderColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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

@Composable
private fun OnboardingGoogleDriveCard(
    backupAccountEmail: String?,
    onConnect: () -> Unit,
    onSkip: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.exactBorderColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (!backupAccountEmail.isNullOrBlank()) {
                Text(
                    text = backupAccountEmail,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    ),
                    color = colorScheme.primary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = onConnect,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(LABEL_LOGIN_WITH_GOOGLE)
                }
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(LABEL_LATER)
                }
            }
        }
    }
}

@Composable
private fun OnboardingSyncModeCard(
    selectedMode: GoogleDriveSyncMode,
    onSelectMode: (GoogleDriveSyncMode) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.exactBorderColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SyncModeOption(
                title = LABEL_SYNC_MODE_AUTO,
                description = "チャット完了時や保存を押したときに同期します",
                selected = selectedMode == GoogleDriveSyncMode.AutoOnSave,
                onClick = { onSelectMode(GoogleDriveSyncMode.AutoOnSave) }
            )
            SyncModeOption(
                title = LABEL_SYNC_MODE_MANUAL,
                description = "設定から好きなタイミングで同期できます",
                selected = selectedMode == GoogleDriveSyncMode.Manual,
                onClick = { onSelectMode(GoogleDriveSyncMode.Manual) }
            )
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
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.exactBorderColor()
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
                        color = colorScheme.secondaryTextColor(0.7f)
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
                    color = colorScheme.secondaryTextColor(0.58f)
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
    val isMonochrome = colorScheme.isPureMonochromeTheme()

    Row(
        modifier = Modifier
            .clip(ControlShape)
            .background(
                if (isMonochrome) colorScheme.surface else colorScheme.primary.copy(alpha = 0.12f)
            )
            .border(
                1.dp,
                if (isMonochrome) colorScheme.onSurface else colorScheme.primary.copy(alpha = 0.22f),
                ControlShape
            )
            .padding(start = 12.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatReminderTime(time),
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMsGothicFontFamily
            ),
            color = if (isMonochrome) colorScheme.onSurface else colorScheme.primary
        )
        TextButton(
            onClick = onRemove,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
        ) {
            Text(LABEL_REMOVE)
        }
    }
}

private val ThemeDisplayOrder = listOf(
    AppThemePreset.White,
    AppThemePreset.IvoryBlack,
    AppThemePreset.Bougainvillea,
    AppThemePreset.CyclamenPink,
    AppThemePreset.Apricot,
    AppThemePreset.CreamYellow,
    AppThemePreset.SpringGreen,
    AppThemePreset.HorizonBlue,
    AppThemePreset.EcruBeige,
    AppThemePreset.Lilac
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemeSelectionInlineCard(
    selectedThemePreset: AppThemePreset,
    themeIntensity: Float,
    onSelectThemePreset: (AppThemePreset) -> Unit,
    onThemeIntensityChange: (Float) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.exactBorderColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ThemeDisplayOrder.forEach { theme ->
                ThemeInlineChip(
                    themePreset = theme,
                    themeIntensity = themeIntensity,
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
    themeIntensity: Float,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val previewColors = paletteForTheme(themePreset, themeIntensity).previewColors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(ControlShape)
            .background(colorScheme.background)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) colorScheme.primary else colorScheme.exactBorderColor(),
                shape = ControlShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        ThemePreviewRow(previewColors = previewColors, swatchSize = 28.dp)
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMsGothicFontFamily
            ),
            color = colorScheme.secondaryTextColor(0.74f)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(color = colorScheme.exactBorderColor(0.18f))
            content()
            HorizontalDivider(color = colorScheme.exactBorderColor(0.18f))
        }
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    supportingText: String? = null,
    trailingText: String? = null,
    trailingContent: (@Composable (() -> Unit))? = null,
    titleColor: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val resolvedTitleColor = if (titleColor == Color.Unspecified) colorScheme.onSurface else titleColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                ),
                color = resolvedTitleColor
            )
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.secondaryTextColor()
                )
            }
        }
        if (trailingContent != null || trailingText != null) {
            Spacer(modifier = Modifier.width(12.dp))
            if (trailingContent != null) {
                trailingContent()
            } else if (trailingText != null) {
                Text(
                    text = trailingText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = JetBrainsMsGothicFontFamily
                    ),
                    color = if (colorScheme.isPureMonochromeTheme()) colorScheme.onSurface else colorScheme.primary,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    supportingText: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .height(60.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                ),
                color = colorScheme.onSurface
            )
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.secondaryTextColor()
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsThemeSwatch(
    previewColor: Color,
    borderColor: Color
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .border(
                width = 1.dp,
                color = borderColor
            )
            .background(previewColor)
    )
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
    themeIntensity: Float,
    onSelectThemePreset: (AppThemePreset) -> Unit,
    onThemeIntensityChange: (Float) -> Unit,
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
                items(ThemeDisplayOrder.size) { index ->
                    val themePreset = ThemeDisplayOrder[index]
                    ThemePresetCard(
                        themePreset = themePreset,
                        themeIntensity = themeIntensity,
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
    themeIntensity: Float,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val previewColors = paletteForTheme(themePreset, themeIntensity).previewColors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = PanelShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) colorScheme.primary else colorScheme.exactBorderColor()
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            ThemePreviewRow(previewColors = previewColors, swatchSize = 32.dp)
        }
    }
}

@Composable
private fun ThemePreviewRow(
    previewColors: List<Color>,
    swatchSize: Dp = 24.dp
) {
    val colorScheme = MaterialTheme.colorScheme
    val previewColor = previewColors.firstOrNull() ?: colorScheme.primary
    Box(
        modifier = Modifier
            .size(swatchSize)
            .clip(CircleShape)
            .background(previewColor)
            .border(1.dp, colorScheme.exactBorderColor(0.1f), CircleShape)
    )
}

private fun Context.findComponentActivity(): FragmentActivity? {
    var currentContext: Context? = this
    while (currentContext is ContextWrapper) {
        if (currentContext is FragmentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return currentContext as? FragmentActivity
}

private fun biometricAvailabilityMessage(context: Context): String? =
    when (
        BiometricManager.from(context).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
    ) {
        BiometricManager.BIOMETRIC_SUCCESS -> null
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> PROMPT_BIOMETRIC_NOT_ENROLLED
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> PROMPT_BIOMETRIC_TEMPORARY_ERROR
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> PROMPT_BIOMETRIC_UNAVAILABLE
        else -> PROMPT_BIOMETRIC_UNAVAILABLE
    }

private fun showBiometricPrompt(
    activity: FragmentActivity,
    title: String,
    subtitle: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val prompt = BiometricPrompt(
        activity,
        ContextCompat.getMainExecutor(activity),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                onError(PROMPT_BIOMETRIC_FAILED)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (
                    errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                    errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                    errorCode == BiometricPrompt.ERROR_CANCELED
                ) {
                    onError("")
                    return
                }
                onError(
                    when (errorCode) {
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> PROMPT_BIOMETRIC_TEMPORARY_ERROR
                        BiometricPrompt.ERROR_NO_BIOMETRICS,
                        BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> PROMPT_BIOMETRIC_NOT_ENROLLED
                        else -> errString.toString()
                    }
                )
            }
        }
    )
    prompt.authenticate(
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
    )
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
    val isMonochrome = colorScheme.isPureMonochromeTheme()
    val cellShape = RoundedCornerShape(16.dp)
    val todayIndicatorColor = Color(0xFFE53935)
    val entryIndicatorColor = Color(0xFF2EAD4B)
    val backgroundColor = when {
        isMonochrome && isSelected -> colorScheme.onBackground
        isMonochrome -> colorScheme.background
        isSelected -> colorScheme.primary.copy(alpha = 0.18f)
        hasEntry -> colorScheme.surface
        else -> colorScheme.background
    }
    val borderColor = when {
        isMonochrome && isSelected -> colorScheme.primary
        isMonochrome -> colorScheme.exactBorderColor()
        isSelected -> colorScheme.primary
        else -> Color.Transparent
    }
    val textColor = when {
        isMonochrome && isSelected -> colorScheme.background
        isMonochrome -> colorScheme.onBackground
        else -> colorScheme.onBackground
    }

    Box(
        modifier = modifier
            .clip(cellShape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = cellShape
            )
            .clickable(enabled = date != null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
            if (hasEntry || isToday) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isToday) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(todayIndicatorColor)
                        )
                    }
                    if (hasEntry) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(entryIndicatorColor)
                        )
                    }
                }
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
    onOpenDate: (LocalDate, Boolean) -> Unit,
    onSaveText: (LocalDate, String) -> Unit,
    onSavePhotos: (LocalDate, List<Uri>) -> Unit,
    onRemovePhoto: (LocalDate, String) -> Unit,
    onOpenDiaryMenu: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current
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
    var horizontalDragTotal by remember(date, isEditing) { mutableStateOf(0f) }

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
                .pointerInput(date, isEditing) {
                    if (isEditing) return@pointerInput
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            horizontalDragTotal += dragAmount
                        },
                        onDragEnd = {
                            val currentDate = entry.date
                            when {
                                horizontalDragTotal <= -56f -> onOpenDate(currentDate.plusDays(1), false)
                                horizontalDragTotal >= 56f -> onOpenDate(currentDate.minusDays(1), false)
                            }
                            horizontalDragTotal = 0f
                        },
                        onDragCancel = {
                            horizontalDragTotal = 0f
                        }
                    )
                }
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
                        UtilityActionButton(
                            text = LABEL_ADD_MEDIA,
                            onClick = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )
                            }
                        )
                        UtilityActionButton(
                            text = LABEL_SAVE,
                            onClick = {
                                onSaveText(entry.date, editText)
                                isEditing = false
                            }
                        )
                    } else {
                        UtilityActionButton(
                            text = LABEL_EDIT,
                            onClick = { isEditing = true }
                        )
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
                onDateClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            onOpenDate(LocalDate.of(year, month + 1, dayOfMonth), isEditing)
                        },
                        entry.date.year,
                        entry.date.monthValue - 1,
                        entry.date.dayOfMonth
                    ).show()
                },
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
    onDateClick: () -> Unit,
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
                modifier = Modifier.clickable(onClick = onDateClick),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = JetBrainsMsGothicFontFamily,
                    textDecoration = TextDecoration.Underline
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
    val colorScheme = MaterialTheme.colorScheme
    val density = LocalDensity.current
    val textStyle = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.onSurface)
    val lineHeight = with(density) { textStyle.lineHeight.toDp() }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val isDarkTheme = colorScheme.background.luminance() < 0.5f
    val lineColor = if (colorScheme.isPureMonochromeTheme()) {
        colorScheme.onSurface
    } else if (isDarkTheme) {
        colorScheme.onSurface.copy(alpha = 0.18f)
    } else {
        colorScheme.primary.copy(alpha = 0.22f)
    }

    Box(
        modifier = modifier
            .heightIn(min = lineHeight)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val lineSpacing = textStyle.lineHeight.toPx()
            val layout = textLayoutResult
            var currentY = lineSpacing
            if (layout != null && layout.lineCount > 0) {
                repeat(layout.lineCount) { index ->
                    val lineY = layout.getLineBottom(index)
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, lineY),
                        end = Offset(size.width, lineY),
                        strokeWidth = 2f
                    )
                    currentY = lineY + lineSpacing
                }
            }
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
                .heightIn(min = lineHeight),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            readOnly = !isEditing,
            onTextLayout = { textLayoutResult = it },
            cursorBrush = SolidColor(
                if (isEditing) colorScheme.onSurface else Color.Transparent
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
    val context = LocalContext.current
    var fullscreenMedia by remember(photoUris) { mutableStateOf<FullscreenDiaryMedia?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        photoUris.forEachIndexed { index, uri ->
            val mediaKind = rememberDiaryMediaKind(uri)
            val painter = rememberAsyncImagePainter(
                model = rememberMediaModel(
                    context = context,
                    uriString = uri,
                    mediaKind = mediaKind
                )
            )
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
                    modifier = imageModifier.clickable {
                        fullscreenMedia = FullscreenDiaryMedia(
                            index = index,
                            uri = uri,
                            kind = mediaKind
                        )
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painter,
                        contentDescription = if (mediaKind == DiaryMediaKind.Video) {
                            "\u65e5\u8a18\u306e\u52d5\u753b"
                        } else {
                            "\u65e5\u8a18\u306e\u5199\u771f"
                        },
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    if (mediaKind == DiaryMediaKind.Video) {
                        VideoBadge(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if (isEditing) {
                        val colorScheme = MaterialTheme.colorScheme
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 10.dp, y = (-10).dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (colorScheme.isPureMonochromeTheme()) colorScheme.background
                                    else colorScheme.background.copy(alpha = 0.92f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = colorScheme.exactBorderColor(0.18f),
                                    shape = CircleShape
                                )
                                .clickable { onRemovePhoto(uri) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "\u00d7",
                                style = MaterialTheme.typography.labelLarge,
                                color = colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    fullscreenMedia?.let { media ->
        FullscreenMediaDialog(
            mediaUris = photoUris,
            initialIndex = media.index,
            onDismiss = { fullscreenMedia = null }
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
private fun FullscreenMediaDialog(
    mediaUris: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = initialIndex.coerceIn(0, (mediaUris.size - 1).coerceAtLeast(0))
    ) {
        mediaUris.size
    }

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
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val mediaUri = mediaUris[page]
                val mediaKind = rememberDiaryMediaKind(mediaUri)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (mediaKind == DiaryMediaKind.Video) {
                        FullscreenVideoPlayer(
                            videoUri = mediaUri,
                            isActive = pagerState.currentPage == page,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        AsyncImage(
                            model = rememberMediaModel(
                                context = context,
                                uriString = mediaUri,
                                mediaKind = DiaryMediaKind.Image
                            ),
                            contentDescription = "\u62e1\u5927\u8868\u793a\u4e2d\u306e\u5199\u771f",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 12.dp, end = 12.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.22f),
                        shape = CircleShape
                    )
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\u00d7",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun FullscreenVideoPlayer(
    videoUri: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val parsedUri = remember(videoUri) { Uri.parse(videoUri) }
    val exoPlayer = remember(context, videoUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(parsedUri))
            prepare()
            playWhenReady = isActive
        }
    }

    LaunchedEffect(exoPlayer, isActive) {
        exoPlayer.playWhenReady = isActive
        if (isActive) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            PlayerView(viewContext).apply {
                player = exoPlayer
                useController = true
                controllerAutoShow = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setShowNextButton(false)
                setShowPreviousButton(false)
                setBackgroundColor(android.graphics.Color.BLACK)
            }
        },
        update = { view ->
            view.player = exoPlayer
        }
    )
}

@Composable
private fun VideoBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.54f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\u25b6",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

@Composable
private fun rememberDiaryMediaKind(uriString: String): DiaryMediaKind {
    val context = LocalContext.current
    return remember(uriString, context) {
        val mimeType = runCatching {
            context.contentResolver.getType(Uri.parse(uriString))
        }.getOrNull().orEmpty()
        if (mimeType.startsWith("video/")) {
            DiaryMediaKind.Video
        } else {
            DiaryMediaKind.Image
        }
    }
}

@Composable
private fun rememberMediaModel(
    context: Context,
    uriString: String,
    mediaKind: DiaryMediaKind
): Any = remember(context, uriString, mediaKind) {
    val parsedUri = Uri.parse(uriString)
    if (mediaKind == DiaryMediaKind.Video) {
        ImageRequest.Builder(context)
            .data(parsedUri)
            .videoFrameMillis(0)
            .build()
    } else {
        parsedUri
    }
}

@Composable
private fun SyncModeSelectionDialog(
    selectedMode: GoogleDriveSyncMode,
    onSelectMode: (GoogleDriveSyncMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(LABEL_CLOSE)
            }
        },
        title = {
            Text(
                text = LABEL_SYNC_METHOD,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = JetBrainsMsGothicFontFamily
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SyncModeOption(
                    title = "\u81ea\u52d5\u3067\u65e5\u8a18\u3092\u4fdd\u5b58\u3057\u305f\u969b\u306b\u540c\u671f\u3059\u308b",
                    selected = selectedMode == GoogleDriveSyncMode.AutoOnSave,
                    onClick = { onSelectMode(GoogleDriveSyncMode.AutoOnSave) }
                )
                SyncModeOption(
                    title = "\u624b\u52d5\u3067\u540c\u671f\u3059\u308b",
                    selected = selectedMode == GoogleDriveSyncMode.Manual,
                    onClick = { onSelectMode(GoogleDriveSyncMode.Manual) }
                )
            }
        }
    )
}

@Composable
private fun SyncModeOption(
    title: String,
    description: String? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val isMonochrome = colorScheme.isPureMonochromeTheme()
    val containerColor = when {
        selected && isMonochrome -> colorScheme.primary
        selected -> colorScheme.primary.copy(alpha = 0.12f)
        else -> colorScheme.surface
    }
    val borderColor = if (selected && !isMonochrome) colorScheme.primary else colorScheme.exactBorderColor()
    val titleColor = if (selected && isMonochrome) colorScheme.background else colorScheme.onSurface
    val descriptionColor =
        if (selected && isMonochrome) colorScheme.background else colorScheme.secondaryTextColor(0.68f)
    val selectedLabelColor = if (selected && isMonochrome) colorScheme.background else colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ControlShape)
            .background(containerColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = ControlShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = titleColor
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = descriptionColor
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = if (selected) LABEL_SELECTED else "",
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMsGothicFontFamily
            ),
            color = selectedLabelColor
        )
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







