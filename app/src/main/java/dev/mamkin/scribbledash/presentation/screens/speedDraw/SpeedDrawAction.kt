package dev.mamkin.scribbledash.presentation.screens.speedDraw

import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.ui.components.draw.DrawAction

sealed interface SpeedDrawAction {
    object Close : SpeedDrawAction
    object DrawAgain : SpeedDrawAction
    data class LevelClick(val level: DifficultyLevel) : SpeedDrawAction
    data class SizeChanged(val size: Size) : SpeedDrawAction
    data class ResultsImageSizeChanged(val size: Size) : SpeedDrawAction
    data class Draw(val drawAction: DrawAction) : SpeedDrawAction
}