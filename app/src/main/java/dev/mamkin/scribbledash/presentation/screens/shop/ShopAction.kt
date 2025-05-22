package dev.mamkin.scribbledash.presentation.screens.shop

sealed interface ShopAction {
    data class SelectTab(val tab: ShopTab) : ShopAction
    data class PenClick(val id: Int) : ShopAction
    data class CanvasClick(val id: Int) : ShopAction
    data object TestAddCoins: ShopAction
    data object TestResetAll: ShopAction
}