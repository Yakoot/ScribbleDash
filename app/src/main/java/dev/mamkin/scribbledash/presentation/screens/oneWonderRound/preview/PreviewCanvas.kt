package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.preview

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw.drawGrid
import dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw.drawRandomVectorOnCanvas

@Composable
fun PreviewCanvas(
    images: List<ImageData>,
    modifier: Modifier = Modifier,
) {
    val lineColor = MaterialTheme.colorScheme.onSurfaceVariant
    val context = LocalContext.current
    Canvas(
        modifier = modifier
            .drawGrid(lineColor)
    ) {
        drawRandomVectorOnCanvas(context, images)
    }
}