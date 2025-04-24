package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

import android.graphics.Path

data class ResultsState(
    val percent: String = "0",
    val exampleImageData: List<Path> = emptyList(),
    val userImageData: List<Path> = emptyList(),
    val rating: Rating = Rating.OOPS,
)