package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview

import android.graphics.Path
import androidx.compose.runtime.Immutable

@Immutable
data class PreviewState(
    val image: List<Path> = emptyList(),
    val secondsLeft: Int = 3,
    val resourceId: Int? = null
)