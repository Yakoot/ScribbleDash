package dev.mamkin.scribbledash.presentation.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.theme.CanvasBackground
import dev.mamkin.scribbledash.ui.theme.CanvasBackgroundAsset
import dev.mamkin.scribbledash.ui.theme.PenAsset
import dev.mamkin.scribbledash.ui.theme.PenColor
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Composable
fun ShopCard(
    modifier: Modifier = Modifier,
    state: ShopItemState
) {
    val containerColor = when (state.grade) {
        ShopItemGrade.BASIC -> Color.White
        ShopItemGrade.PREMIUM -> MaterialTheme.colorScheme.secondary
        ShopItemGrade.LEGENDARY -> MaterialTheme.colorScheme.tertiary
    }

    val contentColor = when (state.grade) {
        ShopItemGrade.BASIC -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
    }

    val priceTextColor = when (state.grade) {
        ShopItemGrade.BASIC -> MaterialTheme.colorScheme.onBackground
        else -> MaterialTheme.colorScheme.onPrimary
    }

    val borderColor = when (state.grade) {
        ShopItemGrade.BASIC -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onPrimary
    }

    val previewBackgroundColor = MaterialTheme.colorScheme.onPrimary
    val previewShape = RoundedCornerShape(14.dp)
    val text = when (state.grade) {
        ShopItemGrade.BASIC -> "BASIC"
        ShopItemGrade.PREMIUM -> "PREMIUM"
        ShopItemGrade.LEGENDARY -> "LEGENDARY"
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 12.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .background(previewBackgroundColor, previewShape)
                    .border(
                        2.dp,
                        borderColor,
                        previewShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!state.isAvailable) {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = "Not available"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (state.isPurchased) {
                Icon(
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = "Already purchased"
                )
            } else {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "Price",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = state.price.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = priceTextColor
                    )
                }
            }
        }
    }
}

enum class ShopItemGrade {
    BASIC,
    PREMIUM,
    LEGENDARY
}

sealed interface ItemType {
    data class Pen(val data: PenAsset) : ItemType
    data class Canvas(val data: CanvasBackgroundAsset) : ItemType
}

data class ShopItemState(
    val id: Int,
    val penColor: PenColor,
    val canvasBackground: CanvasBackground,
    val grade: ShopItemGrade,
    val price: Int,
    val isPurchased: Boolean,
    val isSelected: Boolean,
    val isAvailable: Boolean,
)

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        Column {
            ShopCard(
                state = ShopItemState(
                    id = 0,
                    penColor = PenColor.SolidColor(Color.Red),
                    canvasBackground = CanvasBackground.SolidColor(Color.Cyan),
                    grade = ShopItemGrade.BASIC,
                    price = 200,
                    isPurchased = true,
                    isSelected = false,
                    isAvailable = true
                )
            )
            ShopCard(
                state = ShopItemState(
                    id = 0,
                    penColor = PenColor.SolidColor(Color.Red),
                    canvasBackground = CanvasBackground.SolidColor(Color.Cyan),
                    grade = ShopItemGrade.PREMIUM,
                    price = 200,
                    isPurchased = true,
                    isSelected = false,
                    isAvailable = true
                )
            )
            ShopCard(
                state = ShopItemState(
                    id = 0,
                    penColor = PenColor.SolidColor(Color.Red),
                    canvasBackground = CanvasBackground.SolidColor(Color.Cyan),
                    grade = ShopItemGrade.LEGENDARY,
                    price = 200,
                    isPurchased = false,
                    isSelected = false,
                    isAvailable = true
                )
            )
        }
    }
}

