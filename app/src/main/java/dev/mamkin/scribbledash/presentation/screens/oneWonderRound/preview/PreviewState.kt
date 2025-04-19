package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.preview

data class PreviewState(
    val images: List<ImageData> = emptyList(),
    val secondsLeft: Int = 3,
    val resourceId: Int? = null
)