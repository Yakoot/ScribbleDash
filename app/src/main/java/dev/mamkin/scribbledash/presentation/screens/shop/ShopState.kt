package dev.mamkin.scribbledash.presentation.screens.shop

data class ShopState(
    val coins: Int = 0,
    val tab: ShopTab = ShopTab.PEN,
    val penItems: List<ShopItemState> = emptyList(),
    val canvasItems: List<ShopItemState> = emptyList()
)

enum class ShopTab(val title: String) {
    PEN("Pen"),
    CANVAS("Canvas")
}

sealed interface ShopItem {

}