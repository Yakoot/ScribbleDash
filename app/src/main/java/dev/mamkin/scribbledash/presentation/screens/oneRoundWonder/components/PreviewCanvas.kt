package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.components

import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.EXAMPLE_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.utils.drawPathsWithThickness

@Composable
fun PreviewCanvas(
    paths: List<Path>,
    modifier: Modifier = Modifier,
    onSizeChanged: (Size) -> Unit = {},
) {
    var savedSize by remember { mutableStateOf(Size.Zero) }
    var hasSizeBeenReported by remember { mutableStateOf(false) }

    LaunchedEffect(savedSize) {
        if (savedSize != Size.Zero && !hasSizeBeenReported) {
            onSizeChanged(savedSize)
            hasSizeBeenReported = true
        }
    }

    Canvas(
        modifier = modifier
    ) {
        savedSize = size
        drawPathsWithThickness(paths, EXAMPLE_STROKE_WIDTH)
    }
}