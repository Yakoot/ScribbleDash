package dev.mamkin.scribbledash.presentation.screens.endlessMode

import android.graphics.Path
import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.domain.DifficultyLevel

sealed interface EndlessModeAction {
    object Close : EndlessModeAction
    object DrawAgain : EndlessModeAction
    object NextDrawing : EndlessModeAction
    object Finish : EndlessModeAction
    data class LevelClick(val level: DifficultyLevel) : EndlessModeAction
    data class SizeChanged(val size: Size) : EndlessModeAction
    data class ResultsImageSizeChanged(val size: Size) : EndlessModeAction
    data class ImageDrawn(val paths: List<Path>) : EndlessModeAction
}