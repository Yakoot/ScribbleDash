package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.preview

import android.content.Context
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw.DrawAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class PreviewViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(PreviewState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
                startCountdown()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PreviewState()
        )


    fun onAction(action: DrawAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    private fun startCountdown() = viewModelScope.launch {
        // read initial from state so you can easily change the duration later
        val start = _state.value.secondsLeft
        for (sec in start downTo 0) {
            if (sec == 0) {
                _events.send(UiEvent.NavigateToDraw)
                return@launch
            }
            _state.update { it.copy(secondsLeft = sec) }
            delay(1_000L)
        }
    }

    fun loadPreviewImages(context: Context) {
        viewModelScope.launch {
            val imagesData = PreviewImages.entries.map { entry ->
                getImageData(context, entry.resourceId)
            }
            _state.update { it.copy(images = imagesData) }
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun getImageData(context: Context, idRes: Int): ImageData {
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
        parser.close() // Don't forget to close the parser

        return ImageData(
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            paths = paths
        )
    }
}

sealed interface UiEvent {
    data object NavigateToDraw : UiEvent
}

data class ImageData(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val paths: List<Path>,
)
