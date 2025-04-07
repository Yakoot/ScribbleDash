package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

sealed interface DrawAction {
    data object OnNewPathStart: DrawAction
    data class OnDraw(val offset: Offset): DrawAction
    data object OnPathEnd: DrawAction
    data object OnUndo: DrawAction
    data object OnRedo: DrawAction
    data object OnClearCanvasClick: DrawAction
}