package dev.mamkin.scribbledash.ui.components.game

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.presentation.utils.drawGrid
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Composable
fun PreviewView(
    modifier: Modifier = Modifier,
    image: List<Path>,
    onSizeChanged: (Size) -> Unit,
    secondsLeft: Int
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 29.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(53.dp))
        Text(
            text = "Ready, set...",
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
            PreviewCanvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 24.dp),
                paths = image,
                onSizeChanged = onSizeChanged
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Example",
            style = MaterialTheme.typography.labelSmall,
            color = OnSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        val text = pluralStringResource(
            id = R.plurals.seconds_left,
            count = secondsLeft,
            secondsLeft
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            color = OnBackground
        )
        Spacer(modifier = Modifier.height(41.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PreviewView(
                image = emptyList(),
                onSizeChanged = {},
                secondsLeft = 10
            )
        }
    }
}