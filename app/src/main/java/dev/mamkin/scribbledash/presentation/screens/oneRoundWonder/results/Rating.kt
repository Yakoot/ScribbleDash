package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

enum class Rating(val minScore: Int, val maxScore: Int) {
    OOPS(0, 39),
    MEH(40, 69),
    GOOD(70, 79),
    GREAT(80, 89),
    WOOHOO(90, 100);

    companion object {
        fun fromScore(score: Int): Rating {
            return entries.find { score in it.minScore..it.maxScore } ?: OOPS
        }
    }

    fun getTitle(): String = when (this) {
        OOPS -> "Oops"
        MEH -> "Meh"
        GOOD -> "Good"
        GREAT -> "Great"
        WOOHOO -> "Woohoo!"
    }
} 