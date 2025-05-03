package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.graphics.Path
import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.domain.DifficultyLevel

sealed interface OneRoundWonderAction {
    object Close : OneRoundWonderAction
    object TryAgain : OneRoundWonderAction
    data class LevelClick(val level: DifficultyLevel) : OneRoundWonderAction
    data class SizeChanged(val size: Size) : OneRoundWonderAction
    data class ResultsImageSizeChanged(val size: Size) : OneRoundWonderAction
    data class ImageDrawn(val paths: List<Path>) : OneRoundWonderAction
}