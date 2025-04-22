package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import dev.mamkin.scribbledash.data.repository.GameRepository
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.difficultyLevel.DifficultyLevel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.PathData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawPath
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw.drawVectorOnCanvas
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewImages
import kotlin.random.Random

// Base stroke width assumed for user drawings when calculating example thickness
const val BASE_USER_STROKE_WIDTH = 1f

/**
 * ViewModel для управления игровым процессом, включая выбор случайного изображения
 */
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
    suspend fun preloadImagesToCache() {
        if (cachedImages != null) return // Already cached/preloaded

        Log.d("GameViewModel", "Preloading images into cache...")
        val images = PreviewImages.entries.mapNotNull { entry ->
            try {
                gameRepository.getImageData(entry.resourceId)
            } catch (e: Exception) {
                Log.e("GameViewModel", "Failed to preload image data for resource ID: ${entry.resourceId}", e)
                null
            }
        }
        if (images.isEmpty()) {
            Log.w("GameViewModel", "Warning: No images were preloaded successfully.")
        }
        cachedImages = images
        Log.d("GameViewModel", "Images preloaded into cache.")
    }
    
    /**
     * Устанавливает уровень сложности
     */
    fun setDifficultyLevel(level: DifficultyLevel) {
        Log.d("GameViewModel", "Difficulty level set to: $level")
        this.difficultyLevel = level
    }
    
    /**
     * Загружает все изображения (если не кэшированы), выбирает случайное,
     * сохраняет его в приватной переменной и возвращает.
     */
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

    /**
     * Генерирует образец изображения в Bitmap и сохраняет его,
     * используя толщину линии, зависящую от уровня сложности.
     */
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

        val exampleStrokeWidth = BASE_USER_STROKE_WIDTH * thicknessMultiplier
        Log.d("GameViewModel", "Generating example bitmap with stroke width: $exampleStrokeWidth (Multiplier: $thicknessMultiplier)")

        // Create a copy of the ImageData with the modified thickness
        val imageWithModifiedThickness = originalImage.copy(thickness = exampleStrokeWidth)

        // Pass the modified image data to the drawing function
        val bitmap = drawExampleToBitmap(imageWithModifiedThickness, canvasSize).asAndroidBitmap()
        gameRepository.saveBitmapToFile(bitmap, "example_${System.currentTimeMillis()}.jpg")
    }
    
    /**
     * Получает множитель (для очков) для текущего уровня сложности
     */
    fun getDifficultyMultiplier(): Int {
        val level = this.difficultyLevel ?: DifficultyLevel.Beginner
        return level.multiplier
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
                Log.e("GameViewModel", "Failed to load image data for resource ID: ${entry.resourceId}", e)
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
    }

    fun setCanvasSize (size: Size) {
        this.canvasSize = size
    }
}

fun drawToBitmap(paths: List<PathData>, size: Size): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        // Draw whatever you want here; for instance, a white background and a red line.
        drawRect(color = Color.White, topLeft = Offset.Zero, size = size)
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                thickness = 140f
            )
        }
    }
    return bitmap
}

fun drawExampleToBitmap(image: ImageData, size: Size): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        // Draw whatever you want here; for instance, a white background and a red line.
        drawRect(color = Color.White, topLeft = Offset.Zero, size = size)
        drawVectorOnCanvas(image)
    }
    return bitmap
} 