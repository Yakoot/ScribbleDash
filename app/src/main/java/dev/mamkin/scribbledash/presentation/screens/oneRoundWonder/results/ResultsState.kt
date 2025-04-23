package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

import android.graphics.Path

data class ResultsState(
    val percent: String = "0",
    val exampleImageData: List<Path> = emptyList(),
    val userImageData: List<Path> = emptyList(),
    val title: String = "Woohoo!",
    val subtitle: String = "You’ve officially raised the bar!\n" +
            "I’m going to need a ladder to reach it!",
)