package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawGrid
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawVectorOnCanvasForUser

@Composable
fun PreviewCanvas(
    image: ImageData?,
    modifier: Modifier = Modifier,
    onSizeChanged: (Size) -> Unit = {},
) {
    val lineColor = MaterialTheme.colorScheme.onSurfaceVariant
    var savedSize by remember { mutableStateOf(Size.Zero) }
    var hasSizeBeenReported by remember { mutableStateOf(false) }
    
    LaunchedEffect(savedSize) {
        Log.d("PreviewCanvas", "onSizeChanged: $savedSize")
        if (savedSize != Size.Zero && !hasSizeBeenReported) {
            onSizeChanged(savedSize)
            hasSizeBeenReported = true
        }
    }

    Canvas(
        modifier = modifier
            .drawGrid(lineColor)
    ) {
        savedSize = size
        image?.let { drawVectorOnCanvasForUser(it) }
    }
}