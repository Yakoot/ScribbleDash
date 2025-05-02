package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.graphics.Path
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.presentation.models.PathData
import dev.mamkin.scribbledash.presentation.models.USER_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.utils.calculateCoverage
import dev.mamkin.scribbledash.presentation.utils.createPaths
import dev.mamkin.scribbledash.presentation.utils.drawPathsToBitmap
import dev.mamkin.scribbledash.presentation.utils.scaleToNewSize
import dev.mamkin.scribbledash.presentation.utils.totalLength
import dev.mamkin.scribbledash.ui.components.draw.DrawAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class OneRoundWonderViewModel(
    private val imagesRepository: GameRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var difficultyLevel: DifficultyLevel? = null
    private var canvasSize = Size.Zero
    private var exampleImagePaths: List<Path> = emptyList()
    private var userImagePaths: List<Path> = emptyList()
    private var finalScore: Int = 0
    private var rating: Rating = Rating.OOPS

    private val _state = MutableStateFlow<OneRoundWonderState>(OneRoundWonderState.DifficultyLevel)
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
            initialValue = OneRoundWonderState.DifficultyLevel
        )

    fun onAction(action: OneRoundWonderAction) {
        when (action) {
            is OneRoundWonderAction.LevelClick -> onLevelSelected(action.level)
            is OneRoundWonderAction.SizeChanged -> onSizeChanged(action.size)
            is OneRoundWonderAction.Draw -> onDrawAction(action.drawAction)
            is OneRoundWonderAction.ResultsImageSizeChanged -> onResultsImageSizeChanged(action.size)
            is OneRoundWonderAction.TryAgain -> restartGame()
            else -> {}
        }
    }

    fun onResultsImageSizeChanged(size: Size) {
        val state = _state.value
        if (state !is OneRoundWonderState.Results) return
        val exampleImageScaled = exampleImagePaths.scaleToNewSize(canvasSize, size)
        val userImageScaled = userImagePaths.scaleToNewSize(canvasSize, size)
        _state.value = state.copy(
            exampleImageData = exampleImageScaled,
            userImageData = userImageScaled
        )
    }

    fun onDrawAction(action: DrawAction) {
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
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        val currentPathData = state.drawState.currentPath ?: return
        val drawState = state.drawState
        _state.value = OneRoundWonderState.Draw(
            drawState = drawState.copy(
                currentPath = null,
                paths = drawState.paths + currentPathData,
                isDoneEnabled = true,
                isUndoEnabled = true,
                isRedoEnabled = false
            )
        )
    }

    private fun onNewPathStart(offset: Offset) {
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        val drawState = state.drawState
        _state.value = OneRoundWonderState.Draw(
            drawState = drawState.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = drawState.selectedColor,
                    path = listOf(offset)
                )
            )
        )
    }

    private fun onDraw(offset: Offset) {
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        val currentPathData = state.drawState.currentPath ?: return
        val drawState = state.drawState
        _state.value = OneRoundWonderState.Draw(
            drawState = drawState.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path + offset
                )
            )
        )
    }

    private fun onDoneClick() {
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        val drawState = state.drawState
        this.userImagePaths = drawState.paths.createPaths()
        calculateFinalScore()
        _state.value = OneRoundWonderState.Results(
            rating = rating,
            percent = finalScore.toString(),
            exampleImageData = exampleImagePaths,
            userImageData = userImagePaths
        )
    }

    private fun onUndo() {
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        val drawState = state.drawState
        val paths = drawState.paths
        if (paths.isEmpty()) return
        val lastItem = paths.last()
        val newPaths = paths.dropLast(1)
        val newRedoPaths = (drawState.redoPaths + lastItem).takeLast(5)
        _state.value = OneRoundWonderState.Draw(
            drawState = drawState.copy(
                paths = newPaths,
                redoPaths = newRedoPaths,
                isRedoEnabled = newRedoPaths.isNotEmpty(),
                isUndoEnabled = newPaths.isNotEmpty(),
                isDoneEnabled = newPaths.isNotEmpty()
            )
        )
    }

    private fun onRedo() {
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        val drawState = state.drawState
        val redoPaths = drawState.redoPaths
        if (redoPaths.isEmpty()) return
        val lastItem = redoPaths.last()
        val newPaths = drawState.paths + lastItem
        val newRedoPaths = redoPaths.dropLast(1)
        _state.value = OneRoundWonderState.Draw(
            drawState = drawState.copy(
                paths = newPaths,
                redoPaths = newRedoPaths,
                isRedoEnabled = newRedoPaths.isNotEmpty(),
                isUndoEnabled = newPaths.isNotEmpty(),
                isDoneEnabled = newPaths.isNotEmpty()
            )
        )
    }

    private fun onSizeChanged(size: Size) {
        canvasSize = size
        val randomImage = imagesRepository.getRandomImage()
        val scaledImagePaths: List<Path> = randomImage.paths.scaleToNewSize(
            Size(
                randomImage.viewportWidth,
                randomImage.viewportHeight
            ), canvasSize
        )
        this.exampleImagePaths = scaledImagePaths
        _state.value = OneRoundWonderState.Preview(
            image = scaledImagePaths
        )
        startCountdown()
    }

    private fun onLevelSelected(level: DifficultyLevel) {
        difficultyLevel = level
        _state.value = OneRoundWonderState.Preview()
    }

    private fun startCountdown() = viewModelScope.launch {
        val state = _state.value
        if (state !is OneRoundWonderState.Preview) return@launch
        val start = state.secondsLeft
        for (sec in start downTo 0) {
            _state.update { state.copy(secondsLeft = sec) }
            if (sec == 0) {
                openDrawScreen()
                return@launch
            }
            delay(1_000L)
        }
    }

    private fun openDrawScreen() {
        _state.value = OneRoundWonderState.Draw()
    }

    private fun calculateFinalScore() {
        if (canvasSize == Size.Zero) {
            Log.w("GameViewModel", "Cannot generate example bitmap: canvasSize is Zero.")
            return
        }


        val thicknessMultiplier = when (difficultyLevel ?: DifficultyLevel.Beginner) {
            DifficultyLevel.Beginner -> 15f
            DifficultyLevel.Challenging -> 7f
            DifficultyLevel.Master -> 4f
        }

        val exampleStrokeWidth = USER_STROKE_WIDTH * thicknessMultiplier

        val exampleInset = -exampleStrokeWidth / 2f
        val userInset = -USER_STROKE_WIDTH / 2f - (EXAMPLE_STROKE_WIDTH - USER_STROKE_WIDTH) / 2

        val exampleBitmap =
            drawPathsToBitmap(exampleImagePaths, canvasSize, exampleStrokeWidth, exampleInset)
                .asAndroidBitmap()
//        gameRepository.saveBitmapToFile(exampleBitmap, "example_${System.currentTimeMillis()}.png")

        val userBitmap = drawPathsToBitmap(userImagePaths, canvasSize, USER_STROKE_WIDTH, userInset)
            .asAndroidBitmap()
//        gameRepository.saveBitmapToFile(userBitmap, "user_${System.currentTimeMillis()}.png")

        val coverage = calculateCoverage(userBitmap, exampleBitmap)
        val exampleLength = exampleImagePaths.totalLength()
        val userLength = userImagePaths.totalLength()
        val userLengthPercent = userLength / exampleLength * 100
        val missingLengthPenalty = if (userLengthPercent < 70) {
            (100 - userLengthPercent) * 1f
        } else {
            0f
        }
        val coveragePercent = coverage * 100f
        finalScore = (coveragePercent - missingLengthPenalty).roundToInt()
        if (finalScore < 0) finalScore = 0
        rating = Rating.fromScore(finalScore)
    }

    private fun restartGame() {
        _state.value = OneRoundWonderState.DifficultyLevel
        difficultyLevel = null
        exampleImagePaths = emptyList()
        userImagePaths = emptyList()
        finalScore = 0
    }
}

const val EXAMPLE_STROKE_WIDTH = 10f
