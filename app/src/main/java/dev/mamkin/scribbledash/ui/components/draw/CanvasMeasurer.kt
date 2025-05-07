package dev.mamkin.scribbledash.ui.components.draw

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.measureWithoutPadding(
    paddingDp: Dp = 82.dp,
    onSizeChanged: (Size) -> Unit
): Modifier = composed {
    // Конвертируем dp в пиксели единожды
    val offsetPx = with(LocalDensity.current) { paddingDp.toPx() }
    this.onSizeChanged { intSize ->
        val w = intSize.width.toFloat() - offsetPx
        onSizeChanged(Size(w.coerceAtLeast(0f), w.coerceAtLeast(0f)))
    }
}