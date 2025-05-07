package dev.mamkin.scribbledash.presentation.screens.statistics

import StatisticsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    val state: StateFlow<StatisticsState> = combine(
        statisticsRepository.highestSpeedDrawScore,
        statisticsRepository.speedDrawCount,
        statisticsRepository.highestEndlessModeScore,
        statisticsRepository.endlessModeCount
    ) { highestSpeedDraw, speedDrawCount, highestEndless, endlessCount ->
        StatisticsState(
            highestSpeedDrawScore = highestSpeedDraw,
            speedDrawCount = speedDrawCount,
            highestEndlessModeScore = highestEndless,
            endlessModeCount = endlessCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StatisticsState() // Начальное состояние
    )
}