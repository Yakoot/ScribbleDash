package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.GameViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PreviewViewModel : ViewModel(), KoinComponent {

    private val gameViewModel: GameViewModel by inject()

    private var hasLoadedInitialData = false
    private var countdownStarted = false

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(PreviewState())
    val state: StateFlow<PreviewState> = _state
        .onStart {
            if (!hasLoadedInitialData) {
                // Load initial image when the flow starts and hasn't loaded yet
                val image = gameViewModel.selectAndSetRandomImage()
                _state.update { it.copy(image = image) }
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PreviewState()
        )

    private fun startCountdown() = viewModelScope.launch {
        if (countdownStarted) return@launch // Ensure countdown starts only once
        countdownStarted = true

        val start = _state.value.secondsLeft
        for (sec in start downTo 0) {
            _state.update { it.copy(secondsLeft = sec) }
            if (sec == 0) {
                gameViewModel.generateAndSaveExampleBitmap() // Generate bitmap just before navigating
                _events.send(UiEvent.NavigateToDraw)
                return@launch
            }
            delay(1_000L)
        }
    }

    fun onSizeChanged(size: Size) {
        gameViewModel.setCanvasSize(size)
        startCountdown()
    }
}

sealed interface UiEvent {
    data object NavigateToDraw : UiEvent
}

@Immutable
data class ImageData(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val paths: List<Path>,
    val thickness: Float = 2f
)
