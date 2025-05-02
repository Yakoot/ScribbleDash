package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import dev.mamkin.scribbledash.domain.DifficultyLevel

sealed interface OneRoundWonderAction {
    object Close : OneRoundWonderAction
    object TryAgain : OneRoundWonderAction
    data class LevelClick(val level: DifficultyLevel) : OneRoundWonderAction
    data class SizeChanged(val size: Size) : OneRoundWonderAction
    data class ResultsImageSizeChanged(val size: Size) : OneRoundWonderAction
    data class Draw(val drawAction: DrawAction) : OneRoundWonderAction
}

sealed interface DrawAction {
    data class OnNewPathStart(val offset: Offset) : DrawAction
    data class OnDraw(val offset: Offset) : DrawAction
    data object OnPathEnd : DrawAction
    data object OnUndo : DrawAction
    data object OnRedo : DrawAction
    data object OnDoneClick : DrawAction
}