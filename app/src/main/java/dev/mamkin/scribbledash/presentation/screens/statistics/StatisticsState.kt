package dev.mamkin.scribbledash.presentation.screens.statistics

data class StatisticsState(
    val highestSpeedDrawScore: Int = 0,
    val speedDrawCount: Int = 0,
    val highestEndlessModeScore: Int = 0,
    val endlessModeCount: Int = 0
)