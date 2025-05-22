package dev.mamkin.scribbledash.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import dev.mamkin.scribbledash.R

// Basic colors
val White = Color(0xFFFFFFFF) // Default, always unlocked
val LightGray = Color(0xFFE0E0E0)
val PaleBeige = Color(0xFFF5F5DC)
val SoftPowderBlue = Color(0xFFB0C4DE)
val LightSageGreen = Color(0xFFD3E8D2)
val PalePeach = Color(0xFFF4E1D9)
val SoftLavender = Color(0xFFE7D8E9)

// Premium colors
val FadedOlive = Color(0xFFB8CBB8)
val MutedMauve = Color(0xFFD1B2C1)
val DustyBlue = Color(0xFFA3BFD9)
val SoftKhaki = Color(0xFFD8D6C1)
val MutedCoral = Color(0xFFF2C5C3)
val PaleMint = Color(0xFFD9EDE1)
val SoftLilac = Color(0xFFE2D3E8)

// Legendary textures
val PaperlikeFeelResId = R.drawable.bg_paperlike_feel
val WoodTextureResId = R.drawable.bg_wood_texture
val VintageNotebookResId = R.drawable.bg_vintage_notebook

sealed class CanvasBackground {
    data class SolidColor(val color: Color) : CanvasBackground()
    data class Texture(@DrawableRes val resourceId: Int) : CanvasBackground()
}

enum class BackgroundTier(val price: Int) {
    BASIC(80),
    PREMIUM(150),
    LEGENDARY(250)
}

data class CanvasBackgroundAsset(
    val id: Int,
    val background: CanvasBackground,
    val tier: BackgroundTier,
)

val canvasBackgroundAssets = listOf<CanvasBackgroundAsset>(
    CanvasBackgroundAsset(id = 0, background = CanvasBackground.SolidColor(White), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 1, background = CanvasBackground.SolidColor(LightGray), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 2, background = CanvasBackground.SolidColor(PaleBeige), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 3, background = CanvasBackground.SolidColor(SoftPowderBlue), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 4, background = CanvasBackground.SolidColor(LightSageGreen), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 5, background = CanvasBackground.SolidColor(PalePeach), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 6, background = CanvasBackground.SolidColor(SoftLavender), tier = BackgroundTier.BASIC),
    CanvasBackgroundAsset(id = 7, background = CanvasBackground.SolidColor(FadedOlive), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 8, background = CanvasBackground.SolidColor(MutedMauve), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 9, background = CanvasBackground.SolidColor(DustyBlue), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 10, background = CanvasBackground.SolidColor(SoftKhaki), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 11, background = CanvasBackground.SolidColor(MutedCoral), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 12, background = CanvasBackground.SolidColor(PaleMint), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 13, background = CanvasBackground.SolidColor(SoftLilac), tier = BackgroundTier.PREMIUM),
    CanvasBackgroundAsset(id = 14, background = CanvasBackground.Texture(PaperlikeFeelResId), tier = BackgroundTier.LEGENDARY),
    CanvasBackgroundAsset(id = 15, background = CanvasBackground.Texture(WoodTextureResId), tier = BackgroundTier.LEGENDARY),
    CanvasBackgroundAsset(id = 16, background = CanvasBackground.Texture(VintageNotebookResId), tier = BackgroundTier.LEGENDARY),
)