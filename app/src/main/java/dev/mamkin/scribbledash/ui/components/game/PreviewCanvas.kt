package dev.mamkin.scribbledash.ui.components.game

import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.EXAMPLE_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.utils.drawPathsWithThickness

@Composable
fun PreviewCanvas(
    paths: List<Path>,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
    ) {
        drawPathsWithThickness(paths, EXAMPLE_STROKE_WIDTH)
    }
}