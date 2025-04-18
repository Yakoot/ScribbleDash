package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreviewViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(PreviewState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
                startCountdown()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PreviewState()
        )

    fun onAction(action: PreviewAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    private fun startCountdown() = viewModelScope.launch {
        // read initial from state so you can easily change the duration later
        val start = _state.value.secondsLeft
        for (sec in start downTo 0) {
            if (sec == 0) {
                _events.send(UiEvent.NavigateToDraw)
                return@launch
            }
            _state.update { it.copy(secondsLeft = sec) }
            delay(1_000L)
        }
    }

}

sealed interface UiEvent {
    data object NavigateToDraw : UiEvent
}