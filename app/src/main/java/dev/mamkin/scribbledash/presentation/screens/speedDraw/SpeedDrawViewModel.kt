package dev.mamkin.scribbledash.presentation.screens.speedDraw

import StatisticsRepository
import android.graphics.Path
import android.util.Log
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.domain.DrawingResult
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.domain.calculateResults
import dev.mamkin.scribbledash.presentation.models.ImageData
import dev.mamkin.scribbledash.presentation.utils.scaleToNewSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val SPEED_DRAW_TIME = 120

class SpeedDrawViewModel(
    private val imagesRepository: GameRepository,
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var difficultyLevel: DifficultyLevel? = null
    private var canvasSize = Size.Zero
    private var images: List<ImageData> = emptyList()
    private var exampleImagePaths: List<Path> = emptyList()
    private var userImagePaths: List<Path> = emptyList()
    private var timerJob: Job? = null
    private var remainingTime = SPEED_DRAW_TIME
    private val results: MutableList<DrawingResult> = mutableListOf()

    private val _state = MutableStateFlow<SpeedDrawState>(SpeedDrawState.DifficultyLevel)
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
            initialValue = SpeedDrawState.DifficultyLevel
        )

    private val _appBarState = MutableStateFlow<SpeedDrawAppBarState>(SpeedDrawAppBarState())
    val appBarState = _appBarState.asStateFlow()


    fun onAction(action: SpeedDrawAction) {
        when (action) {
            SpeedDrawAction.DrawAgain -> restartGame()
            is SpeedDrawAction.LevelClick -> onLevelSelected(action.level)
            is SpeedDrawAction.ResultsImageSizeChanged -> {}
            is SpeedDrawAction.ImageDrawn -> onImageDrawn(action.paths)
            is SpeedDrawAction.SizeChanged -> onSizeChanged(action.size)
            else -> {}
        }
    }

    private fun onImageDrawn(paths: List<Path>) {
        val state = _state.value
        if (state !is SpeedDrawState.Draw) return
        userImagePaths = paths
        pauseTimer()
        viewModelScope.launch {
            calculateScore()
        }
        showNextImage()
    }

    private fun calculateScore() {
        viewModelScope.launch {
            val drawingResult = calculateResults(
                exampleImagePaths = exampleImagePaths,
                userImagePaths = userImagePaths,
                canvasSize = canvasSize,
                difficultyLevel = difficultyLevel!!
            )
            results.add(drawingResult)
            val needToIncreaseCounter = drawingResult.rating != Rating.OOPS
            if (needToIncreaseCounter) {
                _appBarState.update {
                    it.copy(
                        drawingsCompleted = it.drawingsCompleted + 1
                    )
                }
            }
            Log.d("SpeedDrawViewModel", "Calculated score: ${drawingResult.score}")
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime--
                _appBarState.update {
                    it.copy(
                        remainingTime = formatTime(remainingTime),
                        timerRed = remainingTime <= 30
                    )
                }
                if (remainingTime == 0) {
                    finishGame()
                    break
                }
            }
        }
    }

    private fun finishGame() {
        timerJob?.cancel()
        timerJob = null

        val drawingsCompleted = _appBarState.value.drawingsCompleted
        val averageScore = if (results.isNotEmpty()) {
            results.sumOf { it.score } / results.size
        } else {
            0
        }

        viewModelScope.launch {
            val currentHighestScore = statisticsRepository.highestSpeedDrawScore.first()
            val currentHighestDrawCount = statisticsRepository.speedDrawCount.first()

            statisticsRepository.updateSpeedDrawCount(drawingsCompleted)
            statisticsRepository.updateHighestSpeedDrawScore(averageScore.toInt())

            val isNewHighScore = averageScore.toInt() > currentHighestScore
            val isNewDrawingsCountRecord = drawingsCompleted > currentHighestDrawCount

            _state.value = SpeedDrawState.Results(
                averageScore = averageScore.toString(),
                newHighScore = isNewHighScore,
                drawingsCompleted = drawingsCompleted,
                drawingsCountRecord = isNewDrawingsCountRecord,
                rating = Rating.fromScore(averageScore)
            )
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun onLevelSelected(level: DifficultyLevel) {
        difficultyLevel = level
        startGame()
    }

    private fun startGame() {
        remainingTime = SPEED_DRAW_TIME
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
        _state.value = SpeedDrawState.Preview(
            image = scaledImagePaths
        )
        startCountdown()
    }

    private fun startCountdown() = viewModelScope.launch {
        val state = _state.value
        if (state !is SpeedDrawState.Preview) return@launch
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
        _state.value = SpeedDrawState.Draw
        startTimer()
    }

    private fun onSizeChanged(size: Size) {
        canvasSize = size
    }

    private fun restartGame() {
        images = emptyList()
        remainingTime = SPEED_DRAW_TIME
        _appBarState.value = SpeedDrawAppBarState()
        _state.value = SpeedDrawState.DifficultyLevel
    }

    override fun onCleared() {
        super.onCleared()
        images = emptyList()
        remainingTime = SPEED_DRAW_TIME
        timerJob?.cancel()
    }
}