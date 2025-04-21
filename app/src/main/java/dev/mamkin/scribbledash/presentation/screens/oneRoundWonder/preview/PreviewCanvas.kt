package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawGrid
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawRandomVectorOnCanvas

@Composable
fun PreviewCanvas(
    images: List<ImageData>,
    modifier: Modifier = Modifier,
) {
    val lineColor = MaterialTheme.colorScheme.onSurfaceVariant
    Canvas(
        modifier = modifier
            .drawGrid(lineColor)
    ) {
        drawRandomVectorOnCanvas(images)
    }
}