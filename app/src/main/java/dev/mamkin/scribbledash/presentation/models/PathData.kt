package dev.mamkin.scribbledash.presentation.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.USER_STROKE_WIDTH

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>,
    val thickness: Float = USER_STROKE_WIDTH
)