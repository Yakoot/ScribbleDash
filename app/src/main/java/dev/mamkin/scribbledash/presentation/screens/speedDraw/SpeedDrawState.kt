package dev.mamkin.scribbledash.presentation.screens.speedDraw

import android.graphics.Path
import dev.mamkin.scribbledash.domain.Rating

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

sealed interface SpeedDrawState {
    data object DifficultyLevel : SpeedDrawState
    data class Preview(
        val image: List<Path> = emptyList(),
        val secondsLeft: Int = 3,
    ) : SpeedDrawState

    data object Draw : SpeedDrawState
    data class Results(
        val averageScore: String = "0",
        val newHighScore: Boolean = false,
        val drawingsCompleted: Int = 0,
        val drawingsCountRecord: Boolean = false,
        val rating: Rating = Rating.OOPS,
    ) : SpeedDrawState
}

data class SpeedDrawAppBarState(
    val remainingTime: String = formatTime(SPEED_DRAW_TIME),
    val drawingsCompleted: Int = 0,
    val timerRed: Boolean = SPEED_DRAW_TIME <= 30,
)