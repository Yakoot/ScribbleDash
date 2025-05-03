package dev.mamkin.scribbledash.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.PathParser
import dev.mamkin.scribbledash.presentation.models.ImageData
import dev.mamkin.scribbledash.presentation.models.PreviewImages
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException
import android.graphics.Path as AndroidPath

class ImagesRepository(private val applicationContext: Context) {

    private var cachedImages: List<ImageData>? = null


    fun getRandomImage(): ImageData {
        loadAllImagesToCache()
        return cachedImages?.random() ?: throw IllegalStateException("No images cached")
    }

    fun getShuffledImages(): List<ImageData> {
        loadAllImagesToCache()
        return cachedImages?.shuffled() ?: throw IllegalStateException("No images cached")
    }

    fun saveBitmapToFile(bitmap: Bitmap, fileName: String): String? {
        return try {
            val imagesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val outputFile = File(imagesDir, fileName)

            outputFile.outputStream().use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            }

            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadAllImagesToCache() {
        if (cachedImages != null) return // Already cached/preloaded

        val images = PreviewImages.entries.mapNotNull { entry ->
            try {
                getImageData(entry.resourceId)
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

    @Throws(IOException::class, XmlPullParserException::class)
    private fun getImageData(idRes: Int): ImageData {
        // Используем applicationContext для доступа к ресурсам
        val parser = applicationContext.resources.getXml(idRes)
        var eventType = parser.eventType
        var viewportWidth = 0f
        var viewportHeight = 0f
        val paths = mutableListOf<AndroidPath>()
        val pathParser = PathParser()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "vector" -> {
                            viewportWidth = parser.getAttributeValue(
                                "http://schemas.android.com/apk/res/android",
                                "viewportWidth"
                            )?.toFloatOrNull() ?: 0f
                            viewportHeight = parser.getAttributeValue(
                                "http://schemas.android.com/apk/res/android",
                                "viewportHeight"
                            )?.toFloatOrNull() ?: 0f
                        }

                        "path" -> {
                            val pathData = parser.getAttributeValue(
                                "http://schemas.android.com/apk/res/android",
                                "pathData"
                            )
                            if (pathData != null) {
                                paths.add(
                                    pathParser.parsePathString(pathData).toPath().asAndroidPath()
                                )
                            }
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        parser.close()

        return ImageData(
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            paths = paths
        )
    }
}