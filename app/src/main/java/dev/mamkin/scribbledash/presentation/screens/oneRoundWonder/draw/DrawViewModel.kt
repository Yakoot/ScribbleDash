package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class DrawViewModel(
    private val oneRoundWonderViewModel: OneRoundWonderViewModel
) : ViewModel(), KoinComponent {

    private var hasLoadedInitialData = false

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(DrawState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DrawState()
        )

    fun onAction(action: DrawAction) {
        when (action) {
            DrawAction.OnDoneClick -> onDoneClick()
            is DrawAction.OnDraw -> onDraw(action.offset)
            is DrawAction.OnNewPathStart -> onNewPathStart(action.offset)
            DrawAction.OnPathEnd -> onPathEnd()
            DrawAction.OnRedo -> onRedo()
            DrawAction.OnUndo -> onUndo()
        }
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = null,
                paths = it.paths + currentPathData,
                isDoneEnabled = true,
                isUndoEnabled = true,
                isRedoEnabled = false
            )
        }
    }

    private fun onNewPathStart(offset: Offset) {
        _state.update {
            it.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = it.selectedColor,
                    path = listOf(offset)
                )
            )
        }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path + offset
                )
            )
        }
    }

    private fun onDoneClick() {
        val paths = _state.value.paths
        oneRoundWonderViewModel.saveUserDrawing(paths)
        viewModelScope.launch {
            _events.send(UiEvent.NavigateToResults)
        }
    }

    private fun onUndo() {
        val paths = state.value.paths
        if (paths.isEmpty()) return
        val lastItem = paths.last()
        val newPaths = paths.dropLast(1)
        val newRedoPaths = (state.value.redoPaths + lastItem).takeLast(5)
        _state.update {
            it.copy(
                paths = newPaths,
                redoPaths = newRedoPaths,
                isRedoEnabled = newRedoPaths.isNotEmpty(),
                isUndoEnabled = newPaths.isNotEmpty(),
                isDoneEnabled = newPaths.isNotEmpty()
            )
        }
    }

    private fun onRedo() {
        val redoPaths = state.value.redoPaths
        if (redoPaths.isEmpty()) return
        val lastItem = redoPaths.last()
        val newPaths = state.value.paths + lastItem
        val newRedoPaths = redoPaths.dropLast(1)
        _state.update {
            it.copy(
                paths = newPaths,
                redoPaths = newRedoPaths,
                isRedoEnabled = newRedoPaths.isNotEmpty(),
                isUndoEnabled = newPaths.isNotEmpty(),
                isDoneEnabled = newPaths.isNotEmpty()
            )
        }
    }
}

sealed interface UiEvent {
    data object NavigateToResults : UiEvent
}
