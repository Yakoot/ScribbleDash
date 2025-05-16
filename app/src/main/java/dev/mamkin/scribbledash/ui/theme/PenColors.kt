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

val rainbow = listOf(
    Color(0xFFFB02FB), // Magenta
    Color(0xFF0000FF), // Blue
    Color(0xFF00EEFF), // Cyan
    Color(0xFF008000), // Green
    Color(0xFFFFFF00), // Yellow
    Color(0xFFFFA500), // Orange
    Color(0xFFFF0000), // Red
)

sealed class PenColor {
    data class SolidColor(val color: Color) : PenColor()
    data class Gradient(val colors: List<Color>) : PenColor()
}

enum class PenTier(val price: Int) {
    BASIC(20),
    PREMIUM(120),
    LEGENDARY(999)
}

data class PenAsset(
    val id: Int,
    val color: PenColor,
    val tier: PenTier,
)

val penAssets = listOf(
    PenAsset(id = 0, color = PenColor.SolidColor(MidnightBlack), tier = PenTier.BASIC),
    PenAsset(id = 1, color = PenColor.SolidColor(CrimsonRed), tier = PenTier.BASIC),
    PenAsset(id = 2, color = PenColor.SolidColor(SunshineYellow), tier = PenTier.BASIC),
    PenAsset(id = 3, color = PenColor.SolidColor(OceanBlue), tier = PenTier.BASIC),
    PenAsset(id = 4, color = PenColor.SolidColor(EmeraldGreen), tier = PenTier.BASIC),
    PenAsset(id = 5, color = PenColor.SolidColor(FlameOrange), tier = PenTier.BASIC),
    PenAsset(id = 6, color = PenColor.SolidColor(RoseQuartz), tier = PenTier.PREMIUM),
    PenAsset(id = 7, color = PenColor.SolidColor(RoyalPurple), tier = PenTier.PREMIUM),
    PenAsset(id = 8, color = PenColor.SolidColor(TealDream), tier = PenTier.PREMIUM),
    PenAsset(id = 9, color = PenColor.SolidColor(GoldenGlow), tier = PenTier.PREMIUM),
    PenAsset(id = 10, color = PenColor.SolidColor(CoralReef), tier = PenTier.PREMIUM),
    PenAsset(id = 11, color = PenColor.SolidColor(MajesticIndigo), tier = PenTier.PREMIUM),
    PenAsset(id = 12, color = PenColor.SolidColor(CopperAura), tier = PenTier.PREMIUM),
    PenAsset(id = 13, color = PenColor.Gradient(rainbow), tier = PenTier.LEGENDARY),
)