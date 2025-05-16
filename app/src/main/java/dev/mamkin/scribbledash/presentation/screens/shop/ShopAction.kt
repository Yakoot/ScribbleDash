package dev.mamkin.scribbledash.presentation.screens.shop

sealed interface ShopAction {
    data class SelectTab(val tab: ShopTab) : ShopAction
}