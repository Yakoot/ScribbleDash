package dev.mamkin.scribbledash.ui.components.draw

import androidx.compose.ui.geometry.Offset

sealed interface DrawAction {
    data class OnNewPathStart(val offset: Offset) : DrawAction
    data class OnDraw(val offset: Offset) : DrawAction
    data object OnPathEnd : DrawAction
    data object OnUndo : DrawAction
    data object OnRedo : DrawAction
    data object OnDoneClick : DrawAction
}