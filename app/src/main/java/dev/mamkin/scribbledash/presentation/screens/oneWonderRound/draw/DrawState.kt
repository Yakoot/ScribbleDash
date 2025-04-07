package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawState(
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
    val isClearEnabled: Boolean = false,
    val isUndoEnabled: Boolean = false,
    val isRedoEnabled: Boolean = false
)


data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>
)