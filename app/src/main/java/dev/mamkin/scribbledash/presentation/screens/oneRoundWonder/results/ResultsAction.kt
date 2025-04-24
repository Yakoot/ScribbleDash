package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

import androidx.compose.ui.geometry.Size

sealed interface ResultsAction {
    object Close: ResultsAction
    object TryAgain: ResultsAction
    data class ImageSizeChanged(val size: Size): ResultsAction
}