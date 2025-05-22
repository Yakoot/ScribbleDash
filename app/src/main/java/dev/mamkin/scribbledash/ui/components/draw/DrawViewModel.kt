package dev.mamkin.scribbledash.ui.components.draw

import ShopRepository
import android.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.presentation.utils.createPaths
import dev.mamkin.scribbledash.ui.theme.CanvasBackground
import dev.mamkin.scribbledash.ui.theme.PenColor
import dev.mamkin.scribbledash.ui.theme.canvasBackgroundAssets
import dev.mamkin.scribbledash.ui.theme.penAssets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

class DrawViewModel(
    shopRepository: ShopRepository
) : ViewModel(), KoinComponent {

    private var hasLoadedInitialData = false

    val selectedPenId = shopRepository.selectedPenId
    val selectedBackgroundColorId = shopRepository.selectedCanvasId

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

    val selectedPenColor: StateFlow<PenColor?> = selectedPenId
        .map { id -> penAssets.find { it.id == id }?.color }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = penAssets.firstOrNull()?.color
        )

    val selectedCanvasBackground: StateFlow<CanvasBackground?> = selectedBackgroundColorId
        .map { id -> canvasBackgroundAssets.find { it.id == id }?.background }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = canvasBackgroundAssets.firstOrNull()?.background
        )

    fun onAction(action: DrawAction) {
        when (action) {
            is DrawAction.OnDraw -> onDraw(action.offset)
            is DrawAction.OnNewPathStart -> onNewPathStart(action.offset)
            DrawAction.OnPathEnd -> onPathEnd()
            DrawAction.OnRedo -> onRedo()
            DrawAction.OnUndo -> onUndo()
            else -> Unit
        }
    }

    fun clear() {
        _state.update {
            it.copy(
                currentPath = null,
                paths = emptyList(),
                redoPaths = emptyList(),
                isDoneEnabled = false,
                isUndoEnabled = false,
                isRedoEnabled = false
            )
        }
    }

    fun getPaths(): List<Path> {
        return state.value.paths.createPaths()
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
