package dev.mamkin.scribbledash.presentation.screens.shop

data class ShopState(
    val coins: Int = 0,
    val tab: ShopTab = ShopTab.PEN
)

enum class ShopTab(val title: String) {
    PEN("Pen"),
    CANVAS("Canvas")
}