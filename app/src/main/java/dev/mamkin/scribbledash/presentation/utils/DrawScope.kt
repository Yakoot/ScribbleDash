package dev.mamkin.scribbledash.presentation.utils

import android.graphics.PathMeasure
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import dev.mamkin.scribbledash.ui.theme.PenColor
import kotlin.math.abs
import android.graphics.Path as AndroidPath

internal fun DrawScope.drawPath(
    path: List<Offset>,
    penColor: PenColor?,
    thickness: Float = 10f
) {
    val smoothedPath = Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)

            val smoothness = 5
            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)

                if (dx >= smoothness || dy >= smoothness) {
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

    when (penColor) {
        is PenColor.Gradient -> {
            val androidPath = smoothedPath.asAndroidPath()
            val pathMeasure = PathMeasure(androidPath, false)
            val pathLength = pathMeasure.length
            val steps = 100
            val pos = FloatArray(2)

            // Length of one gradient repetition segment in pixels
            val segmentLength = 1000f

            // Helper to interpolate colors between multiple gradient stops
            fun lerpColor(colors: List<Color>, fraction: Float): Color {
                if (colors.isEmpty()) return Color.Black
                if (colors.size == 1) return colors[0]

                val scaledFraction = fraction * (colors.size - 1)
                val index = scaledFraction.toInt().coerceIn(0, colors.size - 2)
                val t = scaledFraction - index

                val c0 = colors[index]
                val c1 = colors[index + 1]

                val r = c0.red + (c1.red - c0.red) * t
                val g = c0.green + (c1.green - c0.green) * t
                val b = c0.blue + (c1.blue - c0.blue) * t
                val a = c0.alpha + (c1.alpha - c0.alpha) * t

                return Color(r, g, b, a)
            }

            var prevPoint: Offset? = null

            for (i in 0..steps) {
                val distance = pathLength * i / steps
                val fraction = (distance % segmentLength) / segmentLength

                if (pathMeasure.getPosTan(distance, pos, null)) {
                    val currentPoint = Offset(pos[0], pos[1])

                    if (prevPoint != null) {
                        drawLine(
                            color = lerpColor(penColor.colors, fraction),
                            start = prevPoint,
                            end = currentPoint,
                            strokeWidth = thickness,
                            cap = StrokeCap.Round
                        )
                    }
                    prevPoint = currentPoint
                }
            }
        }
        is PenColor.SolidColor -> {
            drawPath(
                path = smoothedPath,
                color = penColor.color,
                style = Stroke(
                    width = thickness,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
        else -> {
            drawPath(
                path = smoothedPath,
                color = Color.Black,
                style = Stroke(
                    width = thickness,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
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