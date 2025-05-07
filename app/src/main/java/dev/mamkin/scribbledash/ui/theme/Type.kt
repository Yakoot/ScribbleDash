package dev.mamkin.scribbledash.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.mamkin.scribbledash.R

// Font families
val BagelFatOneFamily = FontFamily(
    Font(R.font.bagel_fat_one, FontWeight.Normal)
)

val OutfitFamily = FontFamily(
    Font(R.font.outfit, FontWeight.Normal),
    Font(R.font.outfit_medium, FontWeight.Medium),
    Font(R.font.outfit_semibold, FontWeight.SemiBold)
)

// Material 3 Typography
val Typography = Typography(
    // Display styles (Bagel Fat One)
    displayLarge = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 66.sp,
        lineHeight = 80.sp
    ),
    displayMedium = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 44.sp
    ),

    // Headline styles (Bagel Fat One)
    headlineLarge = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        lineHeight = 48.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 30.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),

    // Title styles (using Bagel Fat One small)
    titleLarge = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),

    // Body styles (Outfit)
    bodyLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),

    // Label styles (Outfit with SemiBold weight)
    labelLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
)

val Typography.headlineXSmall: TextStyle
    get() = TextStyle(
        fontFamily = BagelFatOneFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

val Typography.labelXLarge: TextStyle
    get() = TextStyle(
        fontFamily = OutfitFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp
    )
