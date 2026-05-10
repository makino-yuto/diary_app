package com.makino.diary_app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.makino.diary_app.model.AppThemePreset

data class DiaryThemePalette(
    val colorScheme: ColorScheme,
    val previewColors: List<Color>
)

const val DEFAULT_THEME_INTENSITY = 0.5f
private const val MAX_EFFECTIVE_THEME_INTENSITY = 0.75f

private data class DiaryThemeRecipe(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val isDark: Boolean = false,
    val invertIntensityControl: Boolean = false
)

private val BougainvilleaRecipe = DiaryThemeRecipe(
    primary = Color(0xFFA12674),
    secondary = Color(0xFFD765A1),
    tertiary = Color(0xFF7D9A67),
    background = Color(0xFFFAEAF3),
    surface = Color(0xFFF0D7E6)
)

private val CyclamenPinkRecipe = DiaryThemeRecipe(
    primary = Color(0xFFD8478C),
    secondary = Color(0xFFF1ABC8),
    tertiary = Color(0xFF7AB39B),
    background = Color(0xFFFFF1F6),
    surface = Color(0xFFFFE2EC)
)

private val ApricotRecipe = DiaryThemeRecipe(
    primary = Color(0xFFD97746),
    secondary = Color(0xFFEDAD7D),
    tertiary = Color(0xFF81A86F),
    background = Color(0xFFFFF7EF),
    surface = Color(0xFFFFEDD8)
)

private val CreamYellowRecipe = DiaryThemeRecipe(
    primary = Color(0xFFCEA837),
    secondary = Color(0xFFE7D286),
    tertiary = Color(0xFF80A97B),
    background = Color(0xFFFFFAE8),
    surface = Color(0xFFFFF0C5)
)

private val SpringGreenRecipe = DiaryThemeRecipe(
    primary = Color(0xFF3E9B71),
    secondary = Color(0xFF97D3B6),
    tertiary = Color(0xFF6E9FD1),
    background = Color(0xFFF0FAF3),
    surface = Color(0xFFDFF3E7)
)

private val HorizonBlueRecipe = DiaryThemeRecipe(
    primary = Color(0xFF4B86CC),
    secondary = Color(0xFF96BCE8),
    tertiary = Color(0xFF7EA97B),
    background = Color(0xFFF1F7FF),
    surface = Color(0xFFE0EDFB)
)

private val LilacRecipe = DiaryThemeRecipe(
    primary = Color(0xFF8E6DCC),
    secondary = Color(0xFFC6B2EB),
    tertiary = Color(0xFF6AA698),
    background = Color(0xFFF6F2FC),
    surface = Color(0xFFEBE4F7)
)

private val EcruBeigeRecipe = DiaryThemeRecipe(
    primary = Color(0xFFA6845D),
    secondary = Color(0xFFD8C6AF),
    tertiary = Color(0xFF7EA58E),
    background = Color(0xFFFAF5ED),
    surface = Color(0xFFEDE3D3)
)

private val IvoryBlackRecipe = DiaryThemeRecipe(
    primary = Color(0xFFDBCDB1),
    secondary = Color(0xFF8A99BC),
    tertiary = Color(0xFF7EB898),
    background = Color(0xFF151412),
    surface = Color(0xFF211F1B),
    isDark = true
)

private val BlancDeZincRecipe = DiaryThemeRecipe(
    primary = Color(0xFF76849A),
    secondary = Color(0xFFC1C9D4),
    tertiary = Color(0xFFB5936D),
    background = Color(0xFFF4F5F2),
    surface = Color(0xFFE5E7E3)
)

private fun themeRecipeFor(themePreset: AppThemePreset): DiaryThemeRecipe =
    when (themePreset) {
        AppThemePreset.Bougainvillea -> BougainvilleaRecipe
        AppThemePreset.CyclamenPink -> CyclamenPinkRecipe
        AppThemePreset.Apricot -> ApricotRecipe
        AppThemePreset.CreamYellow -> CreamYellowRecipe
        AppThemePreset.SpringGreen -> SpringGreenRecipe
        AppThemePreset.HorizonBlue -> HorizonBlueRecipe
        AppThemePreset.Lilac -> LilacRecipe
        AppThemePreset.EcruBeige -> EcruBeigeRecipe
        AppThemePreset.IvoryBlack -> IvoryBlackRecipe
        AppThemePreset.BlancDeZinc -> BlancDeZincRecipe
    }

private fun buildPalette(
    recipe: DiaryThemeRecipe,
    themeIntensity: Float
): DiaryThemePalette {
    val sliderIntensity = (themeIntensity.coerceIn(0f, 1f) * MAX_EFFECTIVE_THEME_INTENSITY).coerceIn(0f, 1f)
    val intensity = sliderIntensity.let { normalizedIntensity ->
        if (recipe.invertIntensityControl) 1f - normalizedIntensity else normalizedIntensity
    }
    val primary = adjustAccentColor(recipe.primary, intensity, recipe.isDark)
    val secondary = adjustAccentColor(recipe.secondary, intensity, recipe.isDark)
    val tertiary = adjustAccentColor(recipe.tertiary, intensity, recipe.isDark)
    val background = adjustSurfaceColor(
        base = recipe.background,
        accent = primary,
        intensity = intensity,
        isDark = recipe.isDark,
        isSurface = false
    )
    val surface = adjustSurfaceColor(
        base = recipe.surface,
        accent = primary,
        intensity = intensity,
        isDark = recipe.isDark,
        isSurface = true
    )
    val onBackground = if (recipe.isDark) Color(0xFFF2E7D6) else Ink
    val onSurface = if (recipe.isDark) Color(0xFFF2E7D6) else Ink
    val colorScheme = if (recipe.isDark) {
        darkColorScheme(
            primary = primary,
            secondary = secondary,
            tertiary = tertiary,
            background = background,
            surface = surface,
            onPrimary = accentContentColor(primary),
            onSecondary = accentContentColor(secondary),
            onTertiary = accentContentColor(tertiary),
            onBackground = onBackground,
            onSurface = onSurface
        )
    } else {
        lightColorScheme(
            primary = primary,
            secondary = secondary,
            tertiary = tertiary,
            background = background,
            surface = surface,
            onPrimary = accentContentColor(primary),
            onSecondary = accentContentColor(secondary),
            onTertiary = accentContentColor(tertiary),
            onBackground = onBackground,
            onSurface = onSurface
        )
    }

    return DiaryThemePalette(
        colorScheme = colorScheme,
        previewColors = listOf(primary, secondary, background)
    )
}

private fun adjustAccentColor(
    base: Color,
    intensity: Float,
    isDark: Boolean
): Color {
    val softenRatio = ((0.5f - intensity) / 0.5f).coerceAtLeast(0f)
    val deepenRatio = ((intensity - 0.5f) / 0.5f).coerceAtLeast(0f)
    val softened = lerp(
        base,
        if (isDark) Color(0xFF8F8A83) else Color.White,
        softenRatio * 0.48f
    )

    return lerp(
        softened,
        if (isDark) Color.White else Ink,
        deepenRatio * 0.34f
    )
}

private fun adjustSurfaceColor(
    base: Color,
    accent: Color,
    intensity: Float,
    isDark: Boolean,
    isSurface: Boolean
): Color {
    val softenRatio = ((0.5f - intensity) / 0.5f).coerceAtLeast(0f)
    val deepenRatio = ((intensity - 0.5f) / 0.5f).coerceAtLeast(0f)
    val softened = if (isDark) {
        lerp(base, Color(0xFF2A2723), softenRatio * if (isSurface) 0.16f else 0.08f)
    } else {
        lerp(base, Color.White, softenRatio * if (isSurface) 0.22f else 0.30f)
    }

    return lerp(
        softened,
        accent,
        deepenRatio * if (isSurface) 0.26f else 0.18f
    )
}

private fun accentContentColor(background: Color): Color =
    if (background.luminance() > 0.52f) Ink else Color.White

fun paletteForTheme(
    themePreset: AppThemePreset,
    themeIntensity: Float = DEFAULT_THEME_INTENSITY
): DiaryThemePalette = buildPalette(themeRecipeFor(themePreset), themeIntensity)

private val DiaryShapes = Shapes(
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(8.dp)
)

@Composable
fun DiaryappTheme(
    themePreset: AppThemePreset,
    themeIntensity: Float = DEFAULT_THEME_INTENSITY,
    content: @Composable () -> Unit
) {
    val palette = paletteForTheme(themePreset, themeIntensity)

    MaterialTheme(
        colorScheme = palette.colorScheme,
        typography = Typography,
        shapes = DiaryShapes,
        content = content
    )
}
