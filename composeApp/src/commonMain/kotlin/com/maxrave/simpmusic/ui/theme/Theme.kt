/*
 * Copyright 2026 Naeem Music. All rights reserved.
 * 
 * This file contains the core theming logic for the application.
 * It enforces the "DrakoXNaeem" neon-purple AMOLED aesthetic.
 */

package com.maxrave.simpmusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
import com.maxrave.domain.manager.DataStoreManager
import com.maxrave.simpmusic.expect.ui.SystemBarAppearanceEffect
import com.maxrave.simpmusic.expect.ui.platformDynamicColorScheme

/**
 * [AppColors] defines custom, non-Material semantic tokens used throughout the app.
 * We use this to support consistent branding (e.g., favorite heart colors) across screens.
 */
@Immutable
data class AppColors(
    val favorite: Color,
    val lyricActive: Color,
    val shimmerBackground: Color,
    val shimmerLine: Color,
    val overlay: Color,
    val overlayHeavy: Color,
    val neonPurple: Color,
    val amoledBlack: Color,
)

/**
 * Dark theme specific semantic colors.
 * Optimized for AMOLED displays (Pure Black background).
 */
private val DarkAppColors = AppColors(
    favorite = favoriteColor,
    lyricActive = lyricActiveColor,
    shimmerBackground = shimmerBackground,
    shimmerLine = shimmerLine,
    overlay = overlay,
    overlayHeavy = blackMoreOverlay,
    neonPurple = Color(0xFF9D4EDD), // Primary Neon Brand Color
    amoledBlack = Color(0xFF000000)
)

/**
 * Light theme semantic colors.
 * Note: Keeps certain overlays dark even in light theme to preserve readability on album art.
 */
private val LightAppColors = DarkAppColors.copy(
    shimmerBackground = shimmerBackgroundLight,
    shimmerLine = shimmerLineLight,
    amoledBlack = Color(0xFFFFFFFF) // Reverse for Light Mode compatibility
)

// --- Composition Locals for Theme State ---

/** Access these colors via [LocalAppColors.current] */
val LocalAppColors = staticCompositionLocalOf { DarkAppColors }

/** Tracks whether the app is currently in dark mode */
val LocalIsDarkTheme = staticCompositionLocalOf { true }

/** Forces specific sub-trees to render in Dark mode regardless of global theme */
val LocalForcedDarkColorScheme = staticCompositionLocalOf<ColorScheme?> { null }

/** 
 * Simple utility to parse Hex Strings to [Color] objects.
 * Used for dynamic theme configuration.
 */
fun parseThemeColorHex(hex: String): Color? {
    val clean = hex.trim().removePrefix("#")
    val argb = when (clean.length) {
        6 -> "FF$clean"
        8 -> clean
        else -> return null
    }
    return argb.toLongOrNull(16)?.let { Color(it) }
}

/**
 * Applies neutral surfaces to Light Theme to prevent the "creamy/warm" tint
 * caused by Material 3 dynamic color generation on white backgrounds.
 */
private fun ColorScheme.withNeutralLightSurfaces(): ColorScheme = copy(
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1B1B1B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1B1B),
    surfaceVariant = Color(0xFFE2E2E2),
    onSurfaceVariant = Color(0xFF474747),
    surfaceTint = primary,
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFDADADA),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F7F7),
    surfaceContainer = Color(0xFFF1F1F1),
    surfaceContainerHigh = Color(0xFFECECEC),
    surfaceContainerHighest = Color(0xFFE6E6E6),
    outline = Color(0xFF777777),
    outlineVariant = Color(0xFFC7C7C7),
    inverseSurface = Color(0xFF303030),
    inverseOnSurface = Color(0xFFF1F1F1),
)

/**
 * The main entry point for Application Theming.
 * 
 * @param themeMode Defines whether to force Light, Dark, or System mode.
 * @param themeColorSource Defines if we use dynamic wallpaper colors or custom Neon Purple.
 * @param customThemeColor Optional override color.
 * @param content The composable content to wrap.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    themeMode: String = DataStoreManager.THEME_MODE_DARK,
    themeColorSource: String = DataStoreManager.THEME_COLOR_DEFAULT,
    customThemeColor: Color? = null,
    content: @Composable () -> Unit,
) {
    // 1. Resolve Theme Mode
    val isDark = when (themeMode) {
        DataStoreManager.THEME_MODE_LIGHT -> false
        DataStoreManager.THEME_MODE_SYSTEM -> isSystemInDarkTheme()
        else -> true // Default to Dark
    }

    // 2. Resolve Color Source
    val wallpaperScheme = if (themeColorSource == DataStoreManager.THEME_COLOR_WALLPAPER) {
        platformDynamicColorScheme(isDark)
    } else {
        null
    }

    // 3. Resolve Brand Seed Color (Default to Neon Purple)
    val seedColor = if (themeColorSource == DataStoreManager.THEME_COLOR_CUSTOM) {
        customThemeColor ?: seed
    } else {
        seed
    }

    // 4. Generate Color Scheme
    // We use isAmoled = isDark to force deep black backgrounds
    val colorScheme = wallpaperScheme ?: rememberDynamicColorScheme(
        seedColor = seedColor,
        isDark = isDark,
        isAmoled = isDark, 
        style = PaletteStyle.TonalSpot,
        modifyColorScheme = { cs -> if (isDark) cs else cs.withNeutralLightSurfaces() },
    )

    // 5. Handle Immersive Subtree (Force Dark)
    val forcedDarkScheme = if (isDark) {
        colorScheme
    } else {
        rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = true,
            isAmoled = true,
            style = PaletteStyle.TonalSpot,
        )
    }

    // 6. Apply System Bars
    SystemBarAppearanceEffect(isDark)

    // 7. Render Material Theme
    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = typo(colorScheme), // Uses your existing typography
        content = {
            CompositionLocalProvider(
                LocalContentColor provides colorScheme.onSurfaceVariant,
                LocalAppColors provides if (isDark) DarkAppColors else LightAppColors,
                LocalIsDarkTheme provides isDark,
                LocalForcedDarkColorScheme provides forcedDarkScheme,
                content = content,
            )
        }
    )
}

/**
 * ForceDarkContent is a wrapper for immersive screens (Players, Albums, Playlists).
 * It forces a dark color scheme even if the user is in light mode globally.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ForceDarkContent(content: @Composable () -> Unit) {
    val darkScheme = LocalForcedDarkColorScheme.current ?: MaterialTheme.colorScheme
    
    MaterialExpressiveTheme(
        colorScheme = darkScheme,
        typography = typo(darkScheme, forceDark = true),
        content = {
            CompositionLocalProvider(
                LocalIsDarkTheme provides true,
                LocalContentColor provides darkScheme.onSurfaceVariant,
                LocalAppColors provides DarkAppColors,
                content = content,
            )
        }
    )
}
