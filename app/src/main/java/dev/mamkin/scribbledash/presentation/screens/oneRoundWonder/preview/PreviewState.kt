package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview

import androidx.compose.runtime.Immutable

@Immutable
data class PreviewState(
    val image: ImageData? = null,
    val secondsLeft: Int = 3,
    val resourceId: Int? = null
)