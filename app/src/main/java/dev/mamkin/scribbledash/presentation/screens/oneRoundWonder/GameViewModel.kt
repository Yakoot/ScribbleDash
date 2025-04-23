package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.graphics.Bitmap
import android.graphics.Color
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
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.difficultyLevel.DifficultyLevel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.PathData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.createPaths
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawExampleVectorOnCanvasForDebug
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawUserVectorOnCanvasForDebug
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewImages
import kotlin.random.Random
import android.graphics.Path as AndroidPath

const val USER_STROKE_WIDTH = 10f
const val EXAMPLE_STROKE_WIDTH = 10f

class GameViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private var cachedImages: List<ImageData>? = null
    private var userDrawing: List<PathData>? = null
    private var canvasSize = Size.Zero
    private var difficultyLevel: DifficultyLevel? = null
    private var currentImage: ImageData? = null

    init {
        Log.d("ViewModelScope", "GameViewModel INIT, hashCode: ${this.hashCode()}")
    }

    /**
     * Preloads images into the cache without updating the main state flow immediately.
     */
    fun preloadImagesToCache() {
        if (cachedImages != null) return // Already cached/preloaded

        Log.d("GameViewModel", "Preloading images into cache...")
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
        Log.d("GameViewModel", "Images preloaded into cache.")
    }

    fun setDifficultyLevel(level: DifficultyLevel) {
        Log.d("GameViewModel", "Difficulty level set to: $level")
        this.difficultyLevel = level
    }

    fun selectAndSetRandomImage(): ImageData {
        val images = cachedImages ?: loadImages() // Load images on demand if cache is empty

        if (images.isEmpty()) {
            Log.e("GameViewModel", "No images loaded or available to select from.")
            // Return a default placeholder or handle error appropriately
            val placeholder = ImageData(0f, 0f, emptyList())
            this.currentImage = placeholder // Set the placeholder internally as well
            return placeholder
        }

        val randomImage = images[Random.nextInt(images.size)]
        this.currentImage = randomImage // Store the selected image
        Log.d("GameViewModel", "Random image selected and stored.")
        return randomImage // Return the selected image
    }

    fun generateAndSaveExampleBitmap() {
        val originalImage = this.currentImage
        if (originalImage == null) {
            Log.w("GameViewModel", "Cannot generate example bitmap: currentImage is null.")
            return
        }
        if (canvasSize == Size.Zero) {
            Log.w("GameViewModel", "Cannot generate example bitmap: canvasSize is Zero.")
            return
        }

        Log.d("GameViewModel", "Generating example bitmap... difficultyLevel = $difficultyLevel")

        // Determine thickness multiplier based on difficulty
        val thicknessMultiplier = when (difficultyLevel ?: DifficultyLevel.Beginner) {
            DifficultyLevel.Beginner -> 15f
            DifficultyLevel.Challenging -> 7f
            DifficultyLevel.Master -> 4f
        }

        val exampleStrokeWidth = USER_STROKE_WIDTH * thicknessMultiplier
        Log.d(
            "GameViewModel",
            "Generating example bitmap with stroke width: $exampleStrokeWidth (Multiplier: $thicknessMultiplier)"
        )

        // Create a copy of the ImageData with the modified thickness
        val imageWithModifiedThickness = originalImage.copy(thickness = exampleStrokeWidth)

        // Pass the modified image data to the drawing function
        val exampleBitmap =
            drawExampleToBitmap(imageWithModifiedThickness, canvasSize).asAndroidBitmap()
//        gameRepository.saveBitmapToFile(bitmap, "example_${System.currentTimeMillis()}.png")
        val userBitmap = drawToBitmap(userDrawing ?: emptyList(), canvasSize).asAndroidBitmap()
//        gameRepository.saveBitmapToFile(userBitmap, "user_${System.currentTimeMillis()}.png")
        val coverage = calculateCoverage(userBitmap, exampleBitmap)
        Log.d("GameViewModel", "Coverage: $coverage")
    }

    /**
     * Loads images if not already cached. Does NOT update the state flow directly.
     */
    private fun loadImages(): List<ImageData> {
        cachedImages?.let { return it }

        Log.d("GameViewModel", "Loading images on demand (cache was empty).")
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
        userDrawing = data
        generateAndSaveExampleBitmap()
    }

    fun setCanvasSize(size: Size) {
        this.canvasSize = size
    }
}

fun drawToBitmap(pathData: List<PathData>, size: Size): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)
    val paths = pathData.createPaths()

    drawScope.draw(
        density = Density(2f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        // Draw whatever you want here; for instance, a white background and a red line.
        drawUserVectorOnCanvasForDebug(paths)
    }
    return bitmap
}

fun drawExampleToBitmap(image: ImageData, size: Size): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(2f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        // Draw whatever you want here; for instance, a white background and a red line.
        drawExampleVectorOnCanvasForDebug(image)
    }
    return bitmap
}

fun calculateTotalBounds(paths: List<AndroidPath>): RectF {
    // If there are no paths, return an “empty” RectF
    if (paths.isEmpty()) return RectF()

    // temp will hold each individual path’s bounds
    val tempBounds = RectF()
    // totalBounds will accumulate the union
    val totalBounds = RectF().also { first ->
        // initialize with the bounds of the first path
        paths[0].computeBounds(tempBounds, true)
        first.set(tempBounds)
    }

    // union in all the rest
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