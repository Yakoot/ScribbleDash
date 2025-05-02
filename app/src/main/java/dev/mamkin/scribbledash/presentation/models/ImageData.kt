package dev.mamkin.scribbledash.presentation.models

import android.graphics.Path
import androidx.compose.runtime.Immutable

@Immutable
data class ImageData(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val paths: List<Path>,
    val thickness: Float = 10f
)