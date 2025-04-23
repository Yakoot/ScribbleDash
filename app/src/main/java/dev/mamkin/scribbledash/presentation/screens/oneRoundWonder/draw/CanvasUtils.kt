package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.EXAMPLE_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.USER_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.calculateTotalBounds
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import kotlin.math.abs
import kotlin.math.min
import android.graphics.Path as AndroidPath

internal fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
    val smoothedPath = Path().apply {
        if(path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)

            val smoothness = 5
            for(i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)

                if(dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }
    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

internal fun List<PathData>.createPaths(): List<Path> = map {
    val path = it.path
    Path().apply {
        if(path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)

            val smoothness = 5
            for(i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)

                if(dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }
}

fun Modifier.drawGrid(
    color: Color
) = drawBehind {
    val canvasWidth = size.width
    val canvasHeight = size.height

    // Draw rounded border
    val cornerRadius = 24.dp.toPx()

    drawRoundRect(
        color = color,
        size = size,
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        style = Stroke(width = 2.dp.toPx())
    )

    val thirdWidth = canvasWidth / 3
    val thirdHeight = canvasHeight / 3

    val lineWidth = 2.dp.toPx()

    // Draw vertical lines
    for (i in 1..2) {
        val x = thirdWidth * i
        drawLine(
            color = color,
            start = Offset(x, 0f),
            end = Offset(x, canvasHeight),
            strokeWidth = lineWidth
        )
    }

    // Draw horizontal lines
    for (i in 1..2) {
        val y = thirdHeight * i
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(canvasWidth, y),
            strokeWidth = lineWidth
        )
    }
}

fun DrawScope.drawUserVectorOnCanvasForDebug(
    paths: List<Path>,
) {
    val thickness = USER_STROKE_WIDTH
    val inset = -thickness/2 - (EXAMPLE_STROKE_WIDTH - USER_STROKE_WIDTH)/2
    val canvasWidth = size.width
    val canvasHeight = size.height

    val androidPaths: List<AndroidPath> = paths.map { it.asAndroidPath() }
    val movedPaths = androidPaths.moveToTopLeftCorner(inset, canvasWidth, canvasHeight)


    movedPaths.forEach { path ->
        drawPath(
            path = path.asComposePath(),
            color = Color.Black,
            style = Stroke(
                width = thickness,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

// To draw on bitmap
fun DrawScope.drawExampleVectorOnCanvasForDebug(
    vectorData: ImageData
) {
    if (vectorData.viewportWidth <= 0f || vectorData.viewportHeight <= 0f) {
        println("Warning: Invalid viewport dimensions for the selected image.")
        return
    }

    val inset = -vectorData.thickness / 2f

    val canvasWidth = size.width
    val canvasHeight = size.height

    val scaleX = canvasWidth / vectorData.viewportWidth
    val scaleY = canvasHeight / vectorData.viewportHeight
    val scale = min(scaleX, scaleY)
    val matrix = android.graphics.Matrix().apply {
        setScale(scale, scale)
    }

    val transformed = vectorData.paths.map { orig ->
        android.graphics.Path(orig).apply {
            transform(matrix)
        }
    }

    val movedPaths = transformed.moveToTopLeftCorner(inset, canvasWidth, canvasHeight)

    movedPaths.forEach { path ->
        drawPath(
            path = path.asComposePath(),
            color = Color.Black,
            style = Stroke(
                width = vectorData.thickness,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

fun List<AndroidPath>.moveToTopLeftCorner(inset: Float, canvasWidth: Float, canvasHeight: Float): List<AndroidPath> {
    val bounds = calculateTotalBounds(this)
    bounds.inset(inset, inset)

    val newScaleX = canvasWidth  / bounds.width()
    val newScaleY = canvasHeight  / bounds.height()
    val newScale = min(newScaleX, newScaleY)

    val newMatrix = android.graphics.Matrix().apply {
        setScale(newScale, newScale)
        postTranslate(-bounds.left * newScale, -bounds.top * newScale)
    }

    return this.map { original ->
        AndroidPath(original).apply {
            transform(newMatrix)
        }
    }
}

// It displays on screen
fun DrawScope.drawVectorOnCanvasForUser(vectorData: ImageData) {
    if (vectorData.viewportWidth <= 0f || vectorData.viewportHeight <= 0f) {
        println("Warning: Invalid viewport dimensions for the selected image.")
        return
    }

    val canvasWidth = size.width
    val canvasHeight = size.height

    val scaleX = canvasWidth / vectorData.viewportWidth
    val scaleY = canvasHeight / vectorData.viewportHeight
    val scale = min(scaleX, scaleY)
    val matrix = android.graphics.Matrix().apply {
        setScale(scale, scale)
    }

    val translatedPaths = vectorData.paths.map { original ->
        AndroidPath(original).apply {
            transform(matrix)
        }
    }

    translatedPaths.forEach { path ->
        drawPath(
            path = path.asComposePath(),
            color = Color.Black,
            style = Stroke(
                width = vectorData.thickness,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}