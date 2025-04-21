package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import kotlin.math.abs
import kotlin.random.Random

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
        style = Stroke(width = 2.dp.toPx()) // only stroke, not fill
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

fun DrawScope.drawRandomVectorOnCanvas(images: List<ImageData>) {
    // Exit early if the list is empty
    if (images.isEmpty()) {
        println("Warning: No images provided to drawRandomVectorOnCanvas.")
        return
    }

    // Pick a random image from the list
    val vectorData = images[Random.nextInt(images.size)]

    val canvasWidth = size.width
    val canvasHeight = size.height

    // Ensure viewport dimensions are not zero to avoid division by zero
    if (vectorData.viewportWidth <= 0f || vectorData.viewportHeight <= 0f) {
        println("Warning: Invalid viewport dimensions for the selected image.")
        return
    }

    // Calculate scale factors
    val scaleX = canvasWidth / vectorData.viewportWidth
    val scaleY = canvasHeight / vectorData.viewportHeight
    val scale = scaleX.coerceAtMost(scaleY) // Use the smaller scale factor to fit the image

    // Calculate translation to center the image
    val scaledWidth = vectorData.viewportWidth * scale
    val scaledHeight = vectorData.viewportHeight * scale
    val translateX = (canvasWidth - scaledWidth) / 2f
    val translateY = (canvasHeight - scaledHeight) / 2f

    withTransform({
        translate(left = translateX, top = translateY)
        scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
    }) {
        vectorData.paths.forEach { path ->
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(
                    width = 2f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}