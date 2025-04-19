package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

sealed interface DrawAction {
    data class OnNewPathStart(val offset: Offset): DrawAction
    data class OnDraw(val offset: Offset): DrawAction
    data object OnPathEnd: DrawAction
    data object OnUndo: DrawAction
    data object OnRedo: DrawAction
    data object OnDoneClick: DrawAction
    
    // Новое действие для передачи размера холста
    data class OnCanvasSizeChanged(val size: Size, val context: Context): DrawAction
}