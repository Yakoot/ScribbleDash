package dev.mamkin.scribbledash.presentation.screens.endlessMode

import android.graphics.Path
import dev.mamkin.scribbledash.domain.Rating


sealed interface EndlessModeState {
    data object DifficultyLevel : EndlessModeState
    data class Preview(
        val image: List<Path> = emptyList(),
        val secondsLeft: Int = 3,
    ) : EndlessModeState

    data object Draw : EndlessModeState
    data class Results(
        val averageScore: String = "0",
        val newHighScore: Boolean = false,
        val drawingsCompleted: Int = 0,
        val drawingsCountRecord: Boolean = false,
        val rating: Rating = Rating.OOPS,
    ) : EndlessModeState

    data class RoundResults(
        val percent: String = "0",
        val exampleImageData: List<Path> = emptyList(),
        val userImageData: List<Path> = emptyList(),
        val rating: Rating = Rating.OOPS,
        val showCheckImage: Boolean = false,
        val showNextButton: Boolean = false,
    ) : EndlessModeState
}