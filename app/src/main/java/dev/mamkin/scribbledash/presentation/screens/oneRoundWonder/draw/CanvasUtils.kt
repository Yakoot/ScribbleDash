package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw

import android.graphics.PathMeasure
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.calculateTotalBounds
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

internal fun List<PathData>.createPaths(): List<AndroidPath> = map {
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
    }.asAndroidPath()
}

fun Modifier.drawGrid(
    color: Color,
    radius: Dp = 24.dp
) = drawBehind {
    val canvasWidth = size.width
    val canvasHeight = size.height

    // Draw rounded border
    val cornerRadius = radius.toPx()

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

fun DrawScope.drawPathsWithThickness(paths: List<AndroidPath>, thickness: Float = 10f) {
    paths.forEach { path ->
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

fun AndroidPath.length(): Float {
    val measure = PathMeasure(this, false)
    var lengthSum = 0f

    lengthSum += measure.length

    while (measure.nextContour()) {
        lengthSum += measure.length
    }
    return lengthSum
}

fun List<AndroidPath>.totalLength(): Float =
    this.fold(0f) { acc, path -> acc + path.length() }