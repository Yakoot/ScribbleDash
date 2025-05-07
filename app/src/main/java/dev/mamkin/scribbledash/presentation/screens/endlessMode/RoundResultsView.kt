package dev.mamkin.scribbledash.presentation.screens.endlessMode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.presentation.utils.drawGrid
import dev.mamkin.scribbledash.ui.components.RatingText
import dev.mamkin.scribbledash.ui.components.common.AppButton
import dev.mamkin.scribbledash.ui.components.draw.measureWithoutPadding
import dev.mamkin.scribbledash.ui.components.game.PreviewCanvas
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.Success

@Composable
fun RoundResultsView(
    modifier: Modifier = Modifier,
    state: EndlessModeState.RoundResults,
    onImageSizeChanged: (Size) -> Unit = {},
    onFinish: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 29.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(53.dp))
        Text(
            text = "${state.percent}%",
            style = MaterialTheme.typography.displayLarge,
            color = OnBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box {
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
                            paths = state.exampleImageData,
                        )
                    }
                }


                Column(
                    modifier = Modifier
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
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 12.dp),
                            paths = state.userImageData,
                        )
                    }
                }
            }
            val image = if (state.showCheckImage) {
                R.drawable.check
            } else {
                R.drawable.cross
            }
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .offset(y = (45).dp)
                    .align(Alignment.BottomCenter)
            )
        }


        Spacer(modifier = Modifier.height(48.dp))
        RatingText(state.rating)
        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            text = "FINISH",
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = onFinish,
            enabled = true
        )
        if (state.showNextButton) {
            Spacer(modifier = Modifier.height(12.dp))
            AppButton(
                text = "NEXT DRAWING",
                containerColor = Success,
                onClick = onNext,
                enabled = true
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        RoundResultsView(
            state = EndlessModeState.RoundResults(
                showNextButton = true,
                showCheckImage = true
            )
        )
    }
}