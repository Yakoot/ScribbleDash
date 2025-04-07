package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlin.math.abs

@Composable
fun DrawingCanvas(
    paths: List<PathData>,
    currentPath: PathData?,
    onAction: (DrawAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {
                        onAction(DrawAction.OnNewPathStart)
                    },
                    onDragEnd = {
                        onAction(DrawAction.OnPathEnd)
                    },
                    onDrag = { change, _ ->
                        onAction(DrawAction.OnDraw(change.position))
                    },
                    onDragCancel = {
                        onAction(DrawAction.OnPathEnd)
                    },
                )
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw rounded border
        val cornerRadius = 36.dp.toPx()

        drawRoundRect(
            color = lineColor,
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
                color = lineColor,
                start = Offset(x, 0f),
                end = Offset(x, canvasHeight),
                strokeWidth = lineWidth
            )
        }

        // Draw horizontal lines
        for (i in 1..2) {
            val y = thirdHeight * i
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(canvasWidth, y),
                strokeWidth = lineWidth
            )
        }
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
            )
        }
        currentPath?.let {
            drawPath(
                path = it.path,
                color = it.color
            )
        }
    }
}

private fun DrawScope.drawPath(
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