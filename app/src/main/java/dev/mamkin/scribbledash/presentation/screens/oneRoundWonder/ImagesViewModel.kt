package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

import android.content.Context
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.lifecycle.ViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.PreviewImages
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class ImagesViewModel : ViewModel() {
    
    private var cachedImages: List<ImageData>? = null
    
    /**
     * Загружает изображения, если они ещё не были загружены
     * @return список загруженных изображений
     */
    suspend fun loadImages(context: Context): List<ImageData> {
        cachedImages?.let { return it }
        
        val images = PreviewImages.entries.map { entry ->
            getImageData(context, entry.resourceId)
        }
        
        cachedImages = images
        return images
    }
    
    @Throws(IOException::class, XmlPullParserException::class)
    private fun getImageData(context: Context, idRes: Int): ImageData {
        val parser = context.resources.getXml(idRes)
        var eventType = parser.eventType
        var viewportWidth = 0f
        var viewportHeight = 0f
        val paths = mutableListOf<Path>()
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
                                paths.add(pathParser.parsePathString(pathData).toPath())
                            }
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        parser.close() // Закрываем парсер

        return ImageData(
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            paths = paths
        )
    }
}