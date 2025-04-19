package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.fastForEach

@Composable
fun DrawingCanvas(
    paths: List<PathData>,
    modifier: Modifier = Modifier,
    currentPath: PathData? = null,
    onAction: (DrawAction) -> Unit,
) {
    val lineColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(
        modifier = modifier
            .drawGrid(lineColor)
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { it ->
                        onAction(DrawAction.OnNewPathStart(it))
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