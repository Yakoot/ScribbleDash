package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.preview

import dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw.PathData

data class PreviewState(
    val paths: List<PathData> = emptyList(),
    val secondsLeft: Int = 3
)