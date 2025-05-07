package dev.mamkin.scribbledash.domain

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import android.graphics.PathMeasure
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asAndroidBitmap
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.EXAMPLE_STROKE_WIDTH
import dev.mamkin.scribbledash.presentation.utils.drawPathsToBitmap
import dev.mamkin.scribbledash.ui.components.draw.USER_STROKE_WIDTH
import kotlin.math.roundToInt

suspend fun calculateResults(
    exampleImagePaths: List<Path>,
    userImagePaths: List<Path>,
    canvasSize: Size,
    difficultyLevel: DifficultyLevel
): DrawingResult {
    val thicknessMultiplier = difficultyLevel.multiplier

    val exampleStrokeWidth = USER_STROKE_WIDTH * thicknessMultiplier

    val exampleInset = -exampleStrokeWidth / 2f
    val userInset = -USER_STROKE_WIDTH / 2f - (EXAMPLE_STROKE_WIDTH - USER_STROKE_WIDTH) / 2

    val exampleBitmap =
        drawPathsToBitmap(exampleImagePaths, canvasSize, exampleStrokeWidth, exampleInset)
            .asAndroidBitmap()

    val userBitmap = drawPathsToBitmap(userImagePaths, canvasSize, USER_STROKE_WIDTH, userInset)
        .asAndroidBitmap()

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
    var finalScore = (coveragePercent - missingLengthPenalty).roundToInt()
    if (finalScore < 0) finalScore = 0
    val rating = Rating.fromScore(finalScore)
    return DrawingResult(
        rating = rating,
        score = finalScore
    )
}

fun calculateCoverage(
    userBmp: Bitmap,
    exampleBmp: Bitmap
): Float {
    // 1) size check
    require(
        userBmp.width == exampleBmp.width &&
                userBmp.height == exampleBmp.height
    ) {
        "Bitmaps must be the same dimensions"
    }

    val width = userBmp.width
    val height = userBmp.height
    val totalPixels = width * height

    // 2) bulk-read all pixels
    val userPixels = IntArray(totalPixels)
    val examplePixels = IntArray(totalPixels)
    userBmp.getPixels(userPixels, 0, width, 0, 0, width, height)
    exampleBmp.getPixels(examplePixels, 0, width, 0, 0, width, height)

    // 3) compare
    var visibleUserPixels = 0
    var matchedUserPixels = 0

    for (i in 0 until totalPixels) {
        val userAlpha = Color.alpha(userPixels[i])
        val exampleAlpha = Color.alpha(examplePixels[i])

        // skip if both are transparent
        if (userAlpha == 0 && exampleAlpha == 0) continue

        // count every non-transparent user pixel
        if (userAlpha != 0) {
            visibleUserPixels++
            if (exampleAlpha != 0) {
                matchedUserPixels++
            }
        }
    }

    // 4) avoid divide-by-zero
    if (visibleUserPixels == 0) return 0f

    return matchedUserPixels.toFloat() / visibleUserPixels.toFloat()
}

fun Path.length(): Float {
    val measure = PathMeasure(this, false)
    var lengthSum = 0f

    lengthSum += measure.length

    while (measure.nextContour()) {
        lengthSum += measure.length
    }
    return lengthSum
}

fun List<Path>.totalLength(): Float =
    this.fold(0f) { acc, path -> acc + path.length() }

data class DrawingResult(
    val rating: Rating,
    val score: Int
)