package dev.mamkin.scribbledash.ui.components.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.fastForEach
import dev.mamkin.scribbledash.presentation.models.PathData
import dev.mamkin.scribbledash.presentation.utils.drawPath

@Composable
fun DrawingCanvas(
    paths: List<PathData>,
    modifier: Modifier = Modifier,
    currentPath: PathData? = null,
    onAction: (DrawAction) -> Unit,
) {
    Canvas(
        modifier = modifier
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