package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.BASE_USER_STROKE_WIDTH

data class DrawState(
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
    val isDoneEnabled: Boolean = false,
    val isUndoEnabled: Boolean = false,
    val isRedoEnabled: Boolean = false
)


data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>,
    val thickness: Float = BASE_USER_STROKE_WIDTH
)