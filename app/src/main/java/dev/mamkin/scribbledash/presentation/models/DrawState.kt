package dev.mamkin.scribbledash.presentation.models

import androidx.compose.ui.graphics.Color

data class DrawState(
    val selectedColor: Color = Color.Companion.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
    val isDoneEnabled: Boolean = false,
    val isUndoEnabled: Boolean = false,
    val isRedoEnabled: Boolean = false
)