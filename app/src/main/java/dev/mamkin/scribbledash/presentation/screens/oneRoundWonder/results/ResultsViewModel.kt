package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.GameViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.scaleToNewSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ResultsViewModel(
    private val gameViewModel: GameViewModel
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ResultsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                prepareResults()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ResultsState()
        )

    fun onAction(action: ResultsAction) {
        when (action) {
            is ResultsAction.ImageSizeChanged -> onImageSizeChanged(action.size)
            else -> {}
        }
    }

    fun onImageSizeChanged(newSize: Size) {
        val exampleImage = gameViewModel.getExampleImageForResults()
        val userImage = gameViewModel.getUserImageForResults()
        val oldCanvasSize = gameViewModel.getSize()
        val exampleImageScaled = exampleImage.scaleToNewSize(oldCanvasSize, newSize)
        val userImageScaled = userImage.scaleToNewSize(oldCanvasSize, newSize)
        _state.update { it.copy(
            exampleImageData = exampleImageScaled,
            userImageData = userImageScaled
        ) }
    }

    private fun prepareResults() {
        val rating = gameViewModel.getRating()
        val percent = gameViewModel.getPercent()
        _state.update { it.copy(
            rating = rating,
            percent = percent.toString()
        ) }

    }
}