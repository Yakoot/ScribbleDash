package dev.mamkin.scribbledash.presentation.screens.speedDraw

import android.graphics.Path
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.ui.components.draw.DrawState


sealed interface SpeedDrawState {
    data object DifficultyLevel : SpeedDrawState
    data class Preview(
        val image: List<Path> = emptyList(),
        val secondsLeft: Int = 3,
    ): SpeedDrawState
    data class Draw(
        val drawState: DrawState = DrawState(),
    ): SpeedDrawState
    data class Results(
        val percent: String = "0",
        val exampleImageData: List<Path> = emptyList(),
        val userImageData: List<Path> = emptyList(),
        val rating: Rating = Rating.OOPS,
    ): SpeedDrawState
}