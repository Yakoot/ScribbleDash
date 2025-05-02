package dev.mamkin.scribbledash.presentation.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

fun calculateCoverage(
    userBmp: Bitmap,
    exampleBmp: Bitmap
): Float {
    // 1) size check
    require(
        userBmp.width == exampleBmp.width &&
                userBmp.height == exampleBmp.height
    ) {
        "Bitmaps must be the same dimensions"
    }

    val width = userBmp.width
    val height = userBmp.height
    val totalPixels = width * height

    // 2) bulk-read all pixels
    val userPixels = IntArray(totalPixels)
    val examplePixels = IntArray(totalPixels)
    userBmp.getPixels(userPixels, 0, width, 0, 0, width, height)
    exampleBmp.getPixels(examplePixels, 0, width, 0, 0, width, height)

    // 3) compare
    var visibleUserPixels = 0
    var matchedUserPixels = 0

    for (i in 0 until totalPixels) {
        val userAlpha = Color.alpha(userPixels[i])
        val exampleAlpha = Color.alpha(examplePixels[i])

        // skip if both are transparent
        if (userAlpha == 0 && exampleAlpha == 0) continue

        // count every non-transparent user pixel
        if (userAlpha != 0) {
            visibleUserPixels++
            if (exampleAlpha != 0) {
                matchedUserPixels++
            }
        }
    }

    // 4) avoid divide-by-zero
    if (visibleUserPixels == 0) return 0f

    return matchedUserPixels.toFloat() / visibleUserPixels.toFloat()
}

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