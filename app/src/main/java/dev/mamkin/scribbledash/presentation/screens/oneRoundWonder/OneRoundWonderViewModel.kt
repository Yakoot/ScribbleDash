package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.util.Log
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.PathData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewImages
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results.Rating
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.utils.createPaths
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.utils.drawPathsWithThickness
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.utils.moveToTopLeftCorner
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.utils.totalLength
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random
import android.graphics.Path as AndroidPath

const val USER_STROKE_WIDTH = 10f
const val EXAMPLE_STROKE_WIDTH = 10f

class OneRoundWonderViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private var cachedImages: List<ImageData>? = null
    private var canvasSize = Size.Zero
    private var difficultyLevel: DifficultyLevel? = null
    private var finalScore: Int = 0
    private var rating: Rating = Rating.OOPS

    private var exampleImagePaths: List<Path> = emptyList()
    private var userImagePaths: List<Path> = emptyList()

    fun preloadImagesToCache() {
        if (cachedImages != null) return // Already cached/preloaded

        val images = PreviewImages.entries.mapNotNull { entry ->
            try {
                gameRepository.getImageData(entry.resourceId)
            } catch (e: Exception) {
                Log.e(
                    "GameViewModel",
                    "Failed to preload image data for resource ID: ${entry.resourceId}",
                    e
                )
                null
            }
        }
        if (images.isEmpty()) {
            Log.w("GameViewModel", "Warning: No images were preloaded successfully.")
        }
        cachedImages = images
    }

    fun setDifficultyLevel(level: DifficultyLevel) {
        this.difficultyLevel = level
    }

    fun selectAndSetRandomImage(): List<Path> {
        val images = cachedImages ?: loadImages() // Load images on demand if cache is empty

        if (images.isEmpty()) {
            Log.e("GameViewModel", "No images loaded or available to select from.")
            return emptyList()
        }

        val randomImage = images[Random.nextInt(images.size)]
        val scaledImagePaths: List<Path> = randomImage.paths.scaleToNewSize(
            Size(
                randomImage.viewportWidth,
                randomImage.viewportHeight
            ), canvasSize
        )
        this.exampleImagePaths = scaledImagePaths // Store the selected image
        return scaledImagePaths // Return the selected image
    }

    fun calculateFinalScore() {
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

    /**
     * Loads images if not already cached. Does NOT update the state flow directly.
     */
    private fun loadImages(): List<ImageData> {
        cachedImages?.let { return it }

        val images = PreviewImages.entries.mapNotNull { entry ->
            try {
                gameRepository.getImageData(entry.resourceId)
            } catch (e: Exception) {
                Log.e(
                    "GameViewModel",
                    "Failed to load image data for resource ID: ${entry.resourceId}",
                    e
                )
                null
            }
        }

        if (images.isEmpty()) {
            Log.w("GameViewModel", "Warning: No images were loaded successfully.")
        }

        cachedImages = images
        return images
    }

    fun saveUserDrawing(data: List<PathData>) {
        this.userImagePaths = data.createPaths()
        calculateFinalScore()
    }

    fun setCanvasSize(size: Size) {
        this.canvasSize = size
    }

    fun getSize(): Size {
        return canvasSize
    }

    fun getExampleImageForResults(): List<Path> {
        return exampleImagePaths
    }

    fun getUserImageForResults(): List<Path> {
        return userImagePaths
    }

    fun getRating(): Rating {
        return rating
    }

    fun getPercent(): Int {
        return finalScore
    }
}


fun drawPathsToBitmap(
    paths: List<Path>,
    size: Size,
    thickness: Float = EXAMPLE_STROKE_WIDTH,
    inset: Float = 0f
): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    val movedPaths = paths.moveToTopLeftCorner(inset, size.width, size.height)

    drawScope.draw(
        density = Density(2f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        drawPathsWithThickness(movedPaths, thickness)
    }
    return bitmap
}

fun calculateTotalBounds(paths: List<AndroidPath>): RectF {
    if (paths.isEmpty()) return RectF()

    val tempBounds = RectF()
    val totalBounds = RectF().also { first ->
        paths[0].computeBounds(tempBounds, true)
        first.set(tempBounds)
    }

    for (i in 1 until paths.size) {
        paths[i].computeBounds(tempBounds, true)
        totalBounds.union(tempBounds)
    }

    return totalBounds
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

fun List<Path>.scaleToNewSize(
    fromSize: Size,
    toSize: Size
): List<Path> {
    if (fromSize.width <= 0f || fromSize.height <= 0f) {
        println("Warning: Invalid initial size")
        return emptyList()
    }

    val scaleX = toSize.width / fromSize.width
    val scaleY = toSize.height / fromSize.height
    val scale = min(scaleX, scaleY)
    val matrix = android.graphics.Matrix().apply {
        setScale(scale, scale)
    }

    return this.map { original ->
        AndroidPath(original).apply {
            transform(matrix)
        }
    }
}
