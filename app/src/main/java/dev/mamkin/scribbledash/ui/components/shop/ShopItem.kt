package dev.mamkin.scribbledash.ui.components.shop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShopItem(modifier: Modifier = Modifier) {

}

data class ShopItemState(
    val type: ShopItemType,
    val grade: ShopItemGrade,
    val price: Int,
    val isPurchased: Boolean,
    val isSelected: Boolean,
    val isAvailable: Boolean,
)

enum class ShopItemType {
    PEN, CANVAS
}

enum class ShopItemGrade {
    BASIC, PREMIUM, LEGENDARY
}
