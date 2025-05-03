package dev.mamkin.scribbledash.presentation.utils

import android.graphics.Path
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


fun drawPathsToBitmap(
    paths: List<Path>,
    size: Size,
    thickness: Float,
    inset: Float = 0f
): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    val movedPaths = paths.moveToTopLeftCorner(inset, size.width, size.height)

    drawScope.draw(
        density = Density(2f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        drawPathsWithThickness(movedPaths, thickness)
    }
    return bitmap
}