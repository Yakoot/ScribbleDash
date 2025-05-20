package dev.mamkin.scribbledash.ui.components.game

import ShopRepository
import android.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.presentation.utils.drawGrid
import dev.mamkin.scribbledash.ui.components.RatingText
import dev.mamkin.scribbledash.ui.components.common.AppButton
import dev.mamkin.scribbledash.ui.components.draw.drawCanvasBackground
import dev.mamkin.scribbledash.ui.components.draw.measureWithoutPadding
import dev.mamkin.scribbledash.ui.theme.CanvasBackground
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.PenColor
import dev.mamkin.scribbledash.ui.theme.canvasBackgroundAssets
import dev.mamkin.scribbledash.ui.theme.penAssets
import org.koin.compose.koinInject

@Composable
fun RoundResultsView(
    modifier: Modifier = Modifier,
    percent: String = "0",
    exampleImageData: List<Path> = emptyList(),
    userImageData: List<Path> = emptyList(),
    rating: Rating = Rating.OOPS,
    onImageSizeChanged: (Size) -> Unit = {},
    onTryAgainClick: () -> Unit = {},
) {
    val shopRepository: ShopRepository = koinInject()
    val selectedPenId by shopRepository.selectedPenId.collectAsStateWithLifecycle(0)
    val selectedCanvasId by shopRepository.selectedCanvasId.collectAsStateWithLifecycle(0)

    val penColor: PenColor = remember(selectedPenId) {
        penAssets.find { it.id == selectedPenId }?.color ?: PenColor.SolidColor(Color.Black)
    }
    val background: CanvasBackground = remember(selectedCanvasId) {
        canvasBackgroundAssets.find { it.id == selectedCanvasId }?.background ?: canvasBackgroundAssets.first().background
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 29.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(53.dp))
        Text(
            text = "${percent}%",
            style = MaterialTheme.typography.displayLarge,
            color = OnBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .rotate(-10f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Example",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier
                        .size(160.dp),
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    PreviewCanvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 12.dp)
                            .measureWithoutPadding(0.dp, {
                                onImageSizeChanged(it)
                            }),
                        paths = exampleImageData,
                    )
                }
            }


            Column(
                modifier = Modifier
                    .offset(y = 24.dp)
                    .rotate(10f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Drawing",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier
                        .size(160.dp),
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    PreviewCanvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .drawCanvasBackground(background)
                            .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 12.dp),
                        paths = userImageData,
                        penColor = penColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        RatingText(rating = rating)
        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            text = "TRY AGAIN",
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = onTryAgainClick,
            enabled = true
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}