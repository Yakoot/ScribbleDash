package dev.mamkin.scribbledash.ui.components.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>,
    val thickness: Float = USER_STROKE_WIDTH
)

const val USER_STROKE_WIDTH = 10f