package com.nfs.reelsbowl.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Brand tokens that Material 3 has no slot for: the gradients and ambient glows that give
 * ReelsBowl its look. Read them via [ReelsBowlTheme.brand].
 */
@Immutable
data class BrandColors(
    val accentPink: Color,
    val accentPinkDeep: Color,
    val accentCyan: Color,
    val accentViolet: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val separator: Color,
    val surfacePill: Color,
    val surfaceBadge: Color,
    val surfaceEmblem: Color,
    val glowPink: Color,
    val glowViolet: Color,
    val glowMagenta: Color,
) {
    /** Pink → cyan sweep used on the wordmark and the emblem halo. */
    val brandGradient: Brush
        get() = Brush.linearGradient(listOf(accentPink, accentPinkDeep, accentCyan))
}

private val ReelsBowlBrandColors = BrandColors(
    accentPink = NeonPink,
    accentPinkDeep = HotPink,
    accentCyan = Cyan,
    accentViolet = VioletSoft,
    textSecondary = Mist,
    textTertiary = Slate500,
    separator = Slate600,
    surfacePill = Ink700,
    surfaceBadge = Ink800,
    surfaceEmblem = Ink850,
    glowPink = GlowPink,
    glowViolet = GlowViolet,
    glowMagenta = GlowMagenta,
)

/**
 * ReelsBowl is a dark-only, branded surface — there is deliberately no light scheme and no
 * dynamic color, so the palette stays identical on every device.
 */
private val ReelsBowlColorScheme = darkColorScheme(
    primary = NeonPink,
    onPrimary = Ink900,
    primaryContainer = Ink800,
    onPrimaryContainer = Snow,
    secondary = Cyan,
    onSecondary = Ink900,
    secondaryContainer = Ink700,
    onSecondaryContainer = Mist,
    tertiary = VioletSoft,
    onTertiary = Ink900,
    background = Ink900,
    onBackground = Snow,
    surface = Ink850,
    onSurface = Snow,
    surfaceVariant = Ink800,
    onSurfaceVariant = Mist,
    surfaceContainerHighest = Ink700,
    outline = Slate600,
    outlineVariant = Slate500,
    error = Magenta,
    onError = Snow,
    scrim = Color.Black,
)

// Deliberate: this mirrors how MaterialTheme itself distributes its color scheme. Brand
// tokens are ambient styling, not a dependency any caller should have to thread through.
@Suppress("ComposeCompositionLocalUsage")
private val LocalBrandColors = staticCompositionLocalOf { ReelsBowlBrandColors }

@Composable
fun ReelsBowlTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalBrandColors provides ReelsBowlBrandColors) {
        MaterialTheme(
            colorScheme = ReelsBowlColorScheme,
            typography = Typography,
            content = content,
        )
    }
}

/** Accessor for the tokens that live outside Material's color scheme. */
object ReelsBowlTheme {
    val brand: BrandColors
        @Composable
        @ReadOnlyComposable
        get() = LocalBrandColors.current
}
