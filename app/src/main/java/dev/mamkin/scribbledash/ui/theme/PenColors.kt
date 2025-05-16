package dev.mamkin.scribbledash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Basic Pen Colors
val MidnightBlack = Color(0xFF101820) // Default, always unlocked
val CrimsonRed = Color(0xFFB22234)
val SunshineYellow = Color(0xFFF9D85D)
val OceanBlue = Color(0xFF1D4E89)
val EmeraldGreen = Color(0xFF4CAF50)
val FlameOrange = Color(0xFFF57F20)

// Premium Pen Colors
val RoseQuartz = Color(0xFFF4A6B8)
val RoyalPurple = Color(0xFF6A0FAB)
val TealDream = Color(0xFF008C92)
val GoldenGlow = Color(0xFFFFD700)
val CoralReef = Color(0xFFFF6F61)
val MajesticIndigo = Color(0xFF4B0082)
val CopperAura = Color(0xFFB87333)

// Legendary Pen Colors
val RainbowPenBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFB02FB), // Magenta
        Color(0xFF0000FF), // Blue
        Color(0xFF00EEFF), // Cyan
        Color(0xFF008000), // Green
        Color(0xFFFFFF00), // Yellow
        Color(0xFFFFA500), // Orange
        Color(0xFFFF0000), // Red
    )
)
