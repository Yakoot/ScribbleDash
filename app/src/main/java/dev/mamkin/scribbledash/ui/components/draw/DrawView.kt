package dev.mamkin.scribbledash.ui.components.draw

import android.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mamkin.scribbledash.presentation.utils.drawGrid
import dev.mamkin.scribbledash.ui.theme.CanvasBackground
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnSurface
import org.koin.androidx.compose.koinViewModel


@Composable
fun DrawView(
    modifier: Modifier = Modifier,
    viewModel: DrawViewModel = koinViewModel(),
    onDone: (List<Path>) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedPenColor by viewModel.selectedPenColor.collectAsStateWithLifecycle()
    val selectedCanvasBackground by viewModel.selectedCanvasBackground.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 29.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(53.dp))
        Text(
            text = "Time to draw!",
            style = MaterialTheme.typography.displayMedium,
            color = OnBackground
        )
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(36.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            DrawingCanvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .drawCanvasBackground(selectedCanvasBackground)
                    .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 24.dp),
                paths = state.paths,
                currentPath = state.currentPath,
                onAction = viewModel::onAction,
                penColor = selectedPenColor,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Your Drawing",
            style = MaterialTheme.typography.labelSmall,
            color = OnSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        DrawingControls(onAction = {
            when (it) {
                is DrawAction.OnDoneClick -> {
                    onDone(viewModel.getPaths())
                    viewModel.clear()
                }
                else -> viewModel.onAction(it)
            }
        }, state = state)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun Modifier.drawCanvasBackground(backgroundAsset: CanvasBackground?): Modifier = when (val bg = backgroundAsset) {
    is CanvasBackground.SolidColor -> background(bg.color)
    is CanvasBackground.Texture -> paint(
        painter = painterResource(bg.resourceId),
        contentScale = ContentScale.Crop
    )

    null -> this
}