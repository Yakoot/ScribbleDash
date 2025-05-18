package dev.mamkin.scribbledash.domain

enum class DifficultyLevel(val multiplier: Int, val coinsMultiplier: Float) {
    Beginner(15, 0.5f),
    Challenging(7, 1f),
    Master(4, 1.75f)
}
