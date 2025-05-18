package dev.mamkin.scribbledash.presentation.screens.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.components.draw.drawCanvasBackground
import dev.mamkin.scribbledash.ui.theme.CanvasBackground
import dev.mamkin.scribbledash.ui.theme.PenColor
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.Success

@Composable
fun ShopCard(
    modifier: Modifier = Modifier,
    state: ShopItemState,
    onClick: () -> Unit = {}
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
    val cardBorder = if (state.isSelected) BorderStroke(2.dp, Success) else null
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        border = cardBorder,
        modifier = modifier.clickable(onClick = onClick)
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
                    .clip(previewShape)
                    .drawCanvasBackground(state.canvasBackground)
                    .border(
                        2.dp,
                        borderColor,
                        previewShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                state.penColor?.let {
                    when (it) {
                        is PenColor.SolidColor -> {
                            Icon(
                                painter = painterResource(id = R.drawable.pen_preview),
                                contentDescription = "Pen",
                                tint = it.color
                            )
                        }
                        is PenColor.Gradient -> {
                            val brushGradient = Brush.linearGradient(it.colors)
                            Icon(
                                modifier = Modifier
                                    .graphicsLayer(alpha = 0.99f)
                                    .drawWithCache {
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(brushGradient, blendMode = BlendMode.SrcAtop)
                                        }
                                    },
                                painter = painterResource(id = R.drawable.pen_preview),
                                contentDescription = null,
                            )
                        }
                    }
                }
                if (state.isLocked) {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = "Not available",
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (!state.isLocked) {
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

data class ShopItemState(
    val id: Int,
    val penColor: PenColor?,
    val canvasBackground: CanvasBackground?,
    val grade: ShopItemGrade,
    val price: Int,
    val isLocked: Boolean,
    val isSelected: Boolean,
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
                    isLocked = false,
                    isSelected = false,
                )
            )
            ShopCard(
                state = ShopItemState(
                    id = 0,
                    penColor = PenColor.SolidColor(Color.Red),
                    canvasBackground = CanvasBackground.SolidColor(Color.Cyan),
                    grade = ShopItemGrade.PREMIUM,
                    price = 200,
                    isLocked = true,
                    isSelected = false,
                )
            )
            ShopCard(
                state = ShopItemState(
                    id = 0,
                    penColor = PenColor.Gradient(listOf(Color.Red, Color.Green)),
                    canvasBackground = null,
                    grade = ShopItemGrade.LEGENDARY,
                    price = 200,
                    isLocked = true,
                    isSelected = false,
                )
            )
        }
    }
}

