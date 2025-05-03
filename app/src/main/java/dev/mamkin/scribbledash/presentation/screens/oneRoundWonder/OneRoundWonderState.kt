package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.graphics.Path
import dev.mamkin.scribbledash.domain.Rating

sealed interface OneRoundWonderState {
    data object DifficultyLevel : OneRoundWonderState
    data class Preview(
        val image: List<Path> = emptyList(),
        val secondsLeft: Int = 3,
    ) : OneRoundWonderState

    data object Draw : OneRoundWonderState
    data class Results(
        val percent: String = "0",
        val exampleImageData: List<Path> = emptyList(),
        val userImageData: List<Path> = emptyList(),
        val rating: Rating = Rating.OOPS,
    ) : OneRoundWonderState
}

