package dev.mamkin.scribbledash.presentation.screens.endlessMode

import StatisticsRepository
import android.graphics.Path
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.data.repository.ImagesRepository
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.domain.DrawingResult
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.domain.calculateResults
import dev.mamkin.scribbledash.presentation.models.ImageData
import dev.mamkin.scribbledash.presentation.utils.scaleToNewSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EndlessModeViewModel(
    private val imagesRepository: ImagesRepository,
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var difficultyLevel: DifficultyLevel? = null
    private var canvasSize = Size.Zero
    private var images: List<ImageData> = emptyList()
    private var exampleImagePaths: List<Path> = emptyList()
    private var userImagePaths: List<Path> = emptyList()
    private val results: MutableList<DrawingResult> = mutableListOf()

    private val _state = MutableStateFlow<EndlessModeState>(EndlessModeState.DifficultyLevel)
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
            initialValue = EndlessModeState.DifficultyLevel
        )

    private val _drawingsCount = MutableStateFlow<Int>(0)
    val drawingsCount = _drawingsCount.asStateFlow()

    fun onAction(action: EndlessModeAction) {
        when (action) {
            EndlessModeAction.NextDrawing -> showNextImage()
            EndlessModeAction.Finish -> finishGame()
            EndlessModeAction.DrawAgain -> restartGame()
            is EndlessModeAction.ImageDrawn -> onImageDrawn(action.paths)
            is EndlessModeAction.LevelClick -> onLevelSelected(action.level)
            is EndlessModeAction.ResultsImageSizeChanged -> onResultsImageSizeChanged(action.size)
            is EndlessModeAction.SizeChanged -> {
                canvasSize = action.size
            }

            else -> {}
        }
    }

    private fun finishGame() {
        val drawingsCompleted = _drawingsCount.value
        val averageScore = if (results.isNotEmpty()) {
            results.sumOf { it.score } / results.size
        } else {
            0
        }

        viewModelScope.launch {
            val currentHighestScore = statisticsRepository.highestEndlessModeScore.first()
            val currentHighestDrawCount = statisticsRepository.endlessModeCount.first()

            statisticsRepository.updateEndlessModeCount(drawingsCompleted)
            statisticsRepository.updateHighestEndlessModeScore(averageScore.toInt())

            val isNewHighScore = averageScore.toInt() > currentHighestScore
            val isNewDrawingsCountRecord = drawingsCompleted > currentHighestDrawCount

            _state.value = EndlessModeState.Results(
                averageScore = averageScore.toString(),
                newHighScore = isNewHighScore,
                drawingsCompleted = drawingsCompleted,
                drawingsCountRecord = isNewDrawingsCountRecord,
                rating = Rating.fromScore(averageScore)
            )
        }
    }

    private fun onResultsImageSizeChanged(size: Size) {
        val state = _state.value
        if (state !is EndlessModeState.RoundResults) return
        val exampleImageScaled = exampleImagePaths.scaleToNewSize(canvasSize, size)
        val userImageScaled = userImagePaths.scaleToNewSize(canvasSize, size)
        _state.value = state.copy(
            exampleImageData = exampleImageScaled,
            userImageData = userImageScaled
        )
    }

    private fun onLevelSelected(level: DifficultyLevel) {
        difficultyLevel = level
        startGame()
    }

    private fun startGame() {
        showNextImage()
    }

    private fun showNextImage() {
        if (images.isEmpty()) {
            images = imagesRepository.getShuffledImages()
        }
        val image = images.last()
        images = images.dropLast(1)
        val scaledImagePaths: List<Path> = image.paths.scaleToNewSize(
            Size(
                image.viewportWidth,
                image.viewportHeight
            ), canvasSize
        )
        this.exampleImagePaths = scaledImagePaths
        _state.value = EndlessModeState.Preview(
            image = scaledImagePaths
        )
        startCountdown()
    }

    private fun startCountdown() = viewModelScope.launch {
        val state = _state.value
        if (state !is EndlessModeState.Preview) return@launch
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
        _state.value = EndlessModeState.Draw
    }

    private fun onImageDrawn(paths: List<Path>) {
        val state = _state.value
        if (state !is EndlessModeState.Draw) return
        userImagePaths = paths
        calculateScore()
    }

    private fun calculateScore() {
        viewModelScope.launch {
            val drawingResult = calculateResults(
                exampleImagePaths = exampleImagePaths,
                userImagePaths = userImagePaths,
                canvasSize = canvasSize,
                difficultyLevel = difficultyLevel!!
            )
            val isSuccessRound = drawingResult.rating != Rating.OOPS &&
                    drawingResult.rating != Rating.MEH

            _state.value = EndlessModeState.RoundResults(
                percent = drawingResult.score.toString(),
                exampleImageData = emptyList(),
                userImageData = emptyList(),
                rating = drawingResult.rating,
                showCheckImage = isSuccessRound,
                showNextButton = isSuccessRound
            )

            if (isSuccessRound) {
                _drawingsCount.update { it + 1 }
                results.add(drawingResult)
            }
        }
    }

    private fun restartGame() {
        images = emptyList()
        _drawingsCount.value = 0
        _state.value = EndlessModeState.DifficultyLevel
    }
}