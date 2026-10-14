package com.nfs.reelsbowl.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * "Electric Nocturne" — the ReelsBowl palette, taken from the Figma login design.
 *
 * These are raw values; screens should read them through [ReelsBowlTheme.brand] or
 * [androidx.compose.material3.MaterialTheme.colorScheme] rather than referencing them directly.
 */

// Surfaces, darkest to lightest.
val Ink900 = Color(0xFF0E0E0F) // app background
val Ink850 = Color(0xFF131314) // emblem frame
val Ink800 = Color(0xFF19191B) // feature badges
val Ink700 = Color(0xFF201F21) // status pill, guest button

// Neutrals.
val Slate600 = Color(0xFF484849) // separator dots
val Slate500 = Color(0xFF767576) // legal copy
val Mist = Color(0xFFADAAAB) // secondary text
val Snow = Color(0xFFFFFFFF)

// Accents.
val NeonPink = Color(0xFFFF8AA9)
val HotPink = Color(0xFFFF719B)
val Magenta = Color(0xFFE4006C)
val Cyan = Color(0xFF00EEFC)
val CyanDeep = Color(0xFF00DEEC)
val Violet = Color(0xFF7000FF)
val VioletSoft = Color(0xFFAC89FF)

// Ambient glow orbs behind the login screen.
val GlowPink = Color(0x26FF8AA9) // 15%
val GlowViolet = Color(0x337000FF) // 20%
val GlowMagenta = Color(0x26E4006C) // 15%
