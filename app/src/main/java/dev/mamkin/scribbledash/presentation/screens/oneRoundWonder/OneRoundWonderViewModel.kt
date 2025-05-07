package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.graphics.Path
import android.util.Log
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.data.repository.ImagesRepository
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.domain.calculateResults
import dev.mamkin.scribbledash.presentation.utils.scaleToNewSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OneRoundWonderViewModel(
    private val imagesRepository: ImagesRepository
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
            is OneRoundWonderAction.ImageDrawn -> onImageDrawn(action.paths)
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

    private fun onImageDrawn(paths: List<Path>) {
        val state = _state.value
        if (state !is OneRoundWonderState.Draw) return
        userImagePaths = paths
        viewModelScope.launch {
            val drawingResult = calculateResults(
                exampleImagePaths = exampleImagePaths,
                userImagePaths = userImagePaths,
                canvasSize = canvasSize,
                difficultyLevel = difficultyLevel!!
            )
            _state.value = OneRoundWonderState.Results(
                rating = drawingResult.rating,
                percent = drawingResult.score.toString(),
                exampleImageData = exampleImagePaths,
                userImageData = userImagePaths
            )
        }
    }

    private fun onSizeChanged(size: Size) {
        canvasSize = size
    }

    private fun startGame() {
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
        startGame()
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
        _state.value = OneRoundWonderState.Draw
    }

    private suspend fun calculateFinalScore() {
        if (canvasSize == Size.Zero) {
            Log.w("GameViewModel", "Cannot generate example bitmap: canvasSize is Zero.")
            return
        }
        val drawingResult = calculateResults(
            exampleImagePaths = exampleImagePaths,
            userImagePaths = userImagePaths,
            canvasSize = canvasSize,
            difficultyLevel = difficultyLevel!!
        )
        finalScore = drawingResult.score
        rating = drawingResult.rating
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
