package dev.mamkin.scribbledash.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.annotation.DrawableRes
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
val PaperlikeFeelResId = R.drawable.bg_wood_texture
val WoodTextureResId = R.drawable.bg_wood_texture
val VintageNotebookResId = R.drawable.bg_vintage_notebook

sealed class CanvasBackground {
    data class SolidColor(val color: Color) : CanvasBackground()
    data class Texture(@DrawableRes val resourceId: Int) : CanvasBackground()
}

enum class BackgroundTier {
    BASIC,
    PREMIUM,
    LEGENDARY
}

data class CanvasBackgroundAsset(
    val id: String, 
    val displayName: String, 
    val background: CanvasBackground,
    val tier: BackgroundTier,
    val isDefault: Boolean = false 
)

val allCanvasBackgrounds: List<CanvasBackgroundAsset> = listOf(
    // Basic colors
    CanvasBackgroundAsset("basic_white", "White", CanvasBackground.SolidColor(White), BackgroundTier.BASIC, isDefault = true),
    CanvasBackgroundAsset("basic_light_gray", "Light Gray", CanvasBackground.SolidColor(LightGray), BackgroundTier.BASIC),
    CanvasBackgroundAsset("basic_pale_beige", "Pale Beige", CanvasBackground.SolidColor(PaleBeige), BackgroundTier.BASIC),
    CanvasBackgroundAsset("basic_soft_powder_blue", "Soft Powder Blue", CanvasBackground.SolidColor(SoftPowderBlue), BackgroundTier.BASIC),
    CanvasBackgroundAsset("basic_light_sage_green", "Light Sage Green", CanvasBackground.SolidColor(LightSageGreen), BackgroundTier.BASIC),
    CanvasBackgroundAsset("basic_pale_peach", "Pale Peach", CanvasBackground.SolidColor(PalePeach), BackgroundTier.BASIC),
    CanvasBackgroundAsset("basic_soft_lavender", "Soft Lavender", CanvasBackground.SolidColor(SoftLavender), BackgroundTier.BASIC),

    // Premium colors
    CanvasBackgroundAsset("premium_faded_olive", "Faded Olive", CanvasBackground.SolidColor(FadedOlive), BackgroundTier.PREMIUM),
    CanvasBackgroundAsset("premium_muted_mauve", "Muted Mauve", CanvasBackground.SolidColor(MutedMauve), BackgroundTier.PREMIUM),
    CanvasBackgroundAsset("premium_dusty_blue", "Dusty Blue", CanvasBackground.SolidColor(DustyBlue), BackgroundTier.PREMIUM),
    CanvasBackgroundAsset("premium_soft_khaki", "Soft Khaki", CanvasBackground.SolidColor(SoftKhaki), BackgroundTier.PREMIUM),
    CanvasBackgroundAsset("premium_muted_coral", "Muted Coral", CanvasBackground.SolidColor(MutedCoral), BackgroundTier.PREMIUM),
    CanvasBackgroundAsset("premium_pale_mint", "Pale Mint", CanvasBackground.SolidColor(PaleMint), BackgroundTier.PREMIUM),
    CanvasBackgroundAsset("premium_soft_lilac", "Soft Lilac", CanvasBackground.SolidColor(SoftLilac), BackgroundTier.PREMIUM),

    // Legendary textures
    CanvasBackgroundAsset("legendary_paperlike", "Paperlike Feel", CanvasBackground.Texture(PaperlikeFeelResId), BackgroundTier.LEGENDARY),
    CanvasBackgroundAsset("legendary_wood", "Wood Texture", CanvasBackground.Texture(WoodTextureResId), BackgroundTier.LEGENDARY),
    CanvasBackgroundAsset("legendary_vintage_notebook", "Vintage Notebook", CanvasBackground.Texture(VintageNotebookResId), BackgroundTier.LEGENDARY)
)

fun getCanvasBackgroundAssetById(id: String): CanvasBackgroundAsset? {
    return allCanvasBackgrounds.find { it.id == id }
}

fun getDefaultCanvasBackgroundAsset(): CanvasBackgroundAsset {
    return allCanvasBackgrounds.first { it.isDefault }
}
