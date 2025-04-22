package dev.mamkin.scribbledash.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview.ImageData
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException

class GameRepository(private val applicationContext: Context) {

    @Throws(IOException::class, XmlPullParserException::class)
    fun getImageData(idRes: Int): ImageData {
        // Используем applicationContext для доступа к ресурсам
        val parser = applicationContext.resources.getXml(idRes)
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
        // Важно закрывать парсер после использования
        parser.close()

        return ImageData(
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            paths = paths
        )
    }

    fun saveBitmapToFile(bitmap: Bitmap, fileName: String): String? {
        return try {
            // Используем applicationContext для доступа к файловой системе
            val imagesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val outputFile = File(imagesDir, fileName)

            outputFile.outputStream().use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }

            outputFile.absolutePath
        } catch (e: Exception) {
            // Лучше логировать ошибку здесь
            e.printStackTrace()
            null
        }
    }
} 