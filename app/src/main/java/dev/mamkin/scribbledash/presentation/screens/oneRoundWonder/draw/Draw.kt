package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.draw

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderGraph
import dev.mamkin.scribbledash.ui.components.AppTopBar
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.compose.viewmodel.koinViewModel

@Destination<OneRoundWonderGraph>
@Composable
fun DrawRoot(
    viewModel: DrawViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DrawScreen(
        state = state,
        onClose = {
            navigator.popBackStack(
                route = HomeRootDestination,
                inclusive = false
            )
        },
        onAction = viewModel::onAction
    )
}

@Composable
fun DrawScreen(
    state: DrawState,
    onClose: () -> Unit,
    onAction: (DrawAction) -> Unit = {}
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
                    text = "Time to draw!",
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
                    DrawingCanvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh), // important! same background
                        paths = state.paths,
                        currentPath = state.currentPath,
                        onAction = onAction
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your Drawing",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                DrawingControls(onAction = onAction, state = state)
                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}



@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        DrawScreen(
            state = DrawState(),
            onClose = {}
        )
    }
}
