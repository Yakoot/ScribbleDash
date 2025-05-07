package dev.mamkin.scribbledash.presentation.screens.speedDraw

import android.graphics.Path
import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.domain.DifficultyLevel

sealed interface SpeedDrawAction {
    object Close : SpeedDrawAction
    object DrawAgain : SpeedDrawAction
    data class LevelClick(val level: DifficultyLevel) : SpeedDrawAction
    data class SizeChanged(val size: Size) : SpeedDrawAction
    data class ImageDrawn(val paths: List<Path>) : SpeedDrawAction
}