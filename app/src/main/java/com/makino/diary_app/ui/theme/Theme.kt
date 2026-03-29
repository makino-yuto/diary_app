package com.makino.diary_app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.makino.diary_app.model.AppThemePreset

data class DiaryThemePalette(
    val colorScheme: ColorScheme,
    val previewColors: List<Color>
)

private val BougainvilleaPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFFA12674),
        secondary = Color(0xFFD765A1),
        tertiary = Color(0xFF7D9A67),
        background = Color(0xFFFAEAF3),
        surface = Color(0xFFF0D7E6),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFFA12674), Color(0xFFD765A1), Color(0xFFFAEAF3))
)

private val CyclamenPinkPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFFD8478C),
        secondary = Color(0xFFF1ABC8),
        tertiary = Color(0xFF7AB39B),
        background = Color(0xFFFFF1F6),
        surface = Color(0xFFFFE2EC),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Ink,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFFD8478C), Color(0xFFF1ABC8), Color(0xFFFFF1F6))
)

private val ApricotPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFFD97746),
        secondary = Color(0xFFEDAD7D),
        tertiary = Color(0xFF81A86F),
        background = Color(0xFFFFF7EF),
        surface = Color(0xFFFFEDD8),
        onPrimary = Ink,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFFD97746), Color(0xFFEDAD7D), Color(0xFFFFF7EF))
)

private val CreamYellowPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFFCEA837),
        secondary = Color(0xFFE7D286),
        tertiary = Color(0xFF80A97B),
        background = Color(0xFFFFFAE8),
        surface = Color(0xFFFFF0C5),
        onPrimary = Ink,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFFCEA837), Color(0xFFE7D286), Color(0xFFFFFAE8))
)

private val SpringGreenPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFF3E9B71),
        secondary = Color(0xFF97D3B6),
        tertiary = Color(0xFF6E9FD1),
        background = Color(0xFFF0FAF3),
        surface = Color(0xFFDFF3E7),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFF3E9B71), Color(0xFF97D3B6), Color(0xFFF0FAF3))
)

private val HorizonBluePalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFF4B86CC),
        secondary = Color(0xFF96BCE8),
        tertiary = Color(0xFF7EA97B),
        background = Color(0xFFF1F7FF),
        surface = Color(0xFFE0EDFB),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFF4B86CC), Color(0xFF96BCE8), Color(0xFFF1F7FF))
)

private val LilacPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFF8E6DCC),
        secondary = Color(0xFFC6B2EB),
        tertiary = Color(0xFF6AA698),
        background = Color(0xFFF6F2FC),
        surface = Color(0xFFEBE4F7),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFF8E6DCC), Color(0xFFC6B2EB), Color(0xFFF6F2FC))
)

private val EcruBeigePalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFFA6845D),
        secondary = Color(0xFFD8C6AF),
        tertiary = Color(0xFF7EA58E),
        background = Color(0xFFFAF5ED),
        surface = Color(0xFFEDE3D3),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Color.White,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFFA6845D), Color(0xFFD8C6AF), Color(0xFFFAF5ED))
)

private val IvoryBlackPalette = DiaryThemePalette(
    colorScheme = darkColorScheme(
        primary = Color(0xFFDBCDB1),
        secondary = Color(0xFF8A99BC),
        tertiary = Color(0xFF7EB898),
        background = Color(0xFF151412),
        surface = Color(0xFF211F1B),
        onPrimary = Color(0xFF151412),
        onSecondary = Color(0xFF151412),
        onTertiary = Color(0xFF151412),
        onBackground = Color(0xFFF2E7D6),
        onSurface = Color(0xFFF2E7D6)
    ),
    previewColors = listOf(Color(0xFF151412), Color(0xFFDBCDB1), Color(0xFF8A99BC))
)

private val BlancDeZincPalette = DiaryThemePalette(
    colorScheme = lightColorScheme(
        primary = Color(0xFF76849A),
        secondary = Color(0xFFC1C9D4),
        tertiary = Color(0xFFB5936D),
        background = Color(0xFFF4F5F2),
        surface = Color(0xFFE5E7E3),
        onPrimary = Color.White,
        onSecondary = Ink,
        onTertiary = Ink,
        onBackground = Ink,
        onSurface = Ink
    ),
    previewColors = listOf(Color(0xFF76849A), Color(0xFFC1C9D4), Color(0xFFF4F5F2))
)

fun paletteForTheme(themePreset: AppThemePreset): DiaryThemePalette =
    when (themePreset) {
        AppThemePreset.Bougainvillea -> BougainvilleaPalette
        AppThemePreset.CyclamenPink -> CyclamenPinkPalette
        AppThemePreset.Apricot -> ApricotPalette
        AppThemePreset.CreamYellow -> CreamYellowPalette
        AppThemePreset.SpringGreen -> SpringGreenPalette
        AppThemePreset.HorizonBlue -> HorizonBluePalette
        AppThemePreset.Lilac -> LilacPalette
        AppThemePreset.EcruBeige -> EcruBeigePalette
        AppThemePreset.IvoryBlack -> IvoryBlackPalette
        AppThemePreset.BlancDeZinc -> BlancDeZincPalette
    }

@Composable
fun DiaryappTheme(
    themePreset: AppThemePreset,
    content: @Composable () -> Unit
) {
    val palette = paletteForTheme(themePreset)

    MaterialTheme(
        colorScheme = palette.colorScheme,
        typography = Typography,
        content = content
    )
}
