package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.DrawRootDestination
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.GameViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderGraph
import dev.mamkin.scribbledash.ui.components.AppTopBar
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination<OneRoundWonderGraph>
@Composable
fun PreviewRoot(
    navigator: DestinationsNavigator,
    gameViewModel: GameViewModel,
    viewModel: PreviewViewModel = koinViewModel { parametersOf(gameViewModel) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.NavigateToDraw -> {
                    navigator.navigate(DrawRootDestination())
                }
            }
        }
    }

    PreviewScreen(
        state = state,
        onClose = {
            navigator.popBackStack(
                route = HomeRootDestination,
                inclusive = false
            )
        },
        onSizeChanged = viewModel::onSizeChanged
    )
}

@Composable
fun PreviewScreen(
    state: PreviewState,
    onClose: () -> Unit,
    onSizeChanged: (Size) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            AppTopBar(
                actions = {
                    IconButton(
                        onClick = onClose
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            )
            Column(
                modifier = Modifier
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.Transparent)
                        .shadow(16.dp, RoundedCornerShape(36.dp), clip = false)
                        .clip(RoundedCornerShape(36.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    PreviewCanvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        image = state.image,
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
                    id    = R.plurals.seconds_left,
                    count = state.secondsLeft,
                    state.secondsLeft
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(41.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        PreviewScreen(
            state = PreviewState(),
            onClose = {},
            onSizeChanged = {}
        )
    }
}