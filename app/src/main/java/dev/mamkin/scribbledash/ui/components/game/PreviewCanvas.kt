package dev.mamkin.scribbledash.ui.components.game

import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.EXAMPLE_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.utils.drawPathsWithThickness
import dev.mamkin.scribbledash.ui.theme.PenColor

@Composable
fun PreviewCanvas(
    paths: List<Path>,
    modifier: Modifier = Modifier,
    penColor: PenColor = PenColor.SolidColor(Color.Black)
) {
    Canvas(
        modifier = modifier
    ) {
        drawPathsWithThickness(paths, EXAMPLE_STROKE_WIDTH, penColor)
    }
}