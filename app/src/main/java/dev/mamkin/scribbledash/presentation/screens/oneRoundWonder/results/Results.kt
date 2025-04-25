package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.generated.navgraphs.OneRoundWonderNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.GameViewModel
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.OneRoundWonderGraph
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.components.PreviewCanvas
import dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.utils.drawGrid
import dev.mamkin.scribbledash.ui.components.AppButton
import dev.mamkin.scribbledash.ui.components.AppTopBar
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination<OneRoundWonderGraph>
@Composable
fun ResultsRoot(
    navigator: DestinationsNavigator,
    gameViewModel: GameViewModel,
    viewModel: ResultsViewModel = koinViewModel { parametersOf(gameViewModel) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler { }

    ResultsScreen(
        state = state,
        onAction = {
            when (it) {
                is ResultsAction.Close -> {
                    navigator.popBackStack(
                        route = HomeRootDestination,
                        inclusive = false
                    )
                }

                is ResultsAction.TryAgain -> {
                    navigator.navigate(OneRoundWonderNavGraph)
                }

                else -> {
                    viewModel.onAction(action = it)
                }
            }
        }
    )
}

@Composable
fun ResultsScreen(
    state: ResultsState,
    onAction: (ResultsAction) -> Unit,
) {
    val oopsArray: Array<String> = stringArrayResource(R.array.oops_array)
    val goodArray: Array<String> = stringArrayResource(R.array.good_array)
    val woohooArray: Array<String> = stringArrayResource(R.array.woohoo_array)
    val title = state.rating.getTitle()
    val subtitle = when (state.rating) {
        Rating.OOPS, Rating.MEH -> oopsArray.random()
        Rating.GREAT, Rating.GOOD -> goodArray.random()
        Rating.WOOHOO -> woohooArray.random()
    }
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
                        onClick = { onAction(ResultsAction.Close) }
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
                    text = "${state.percent}%",
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
                                    .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 12.dp),
                                paths = state.exampleImageData,
                                onSizeChanged = { onAction(ResultsAction.ImageSizeChanged(it)) }
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
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .drawGrid(MaterialTheme.colorScheme.onSurfaceVariant, 12.dp),
                                paths = state.userImageData,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnBackgroundVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                AppButton(
                    text = "TRY AGAIN",
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { onAction(ResultsAction.TryAgain) },
                    enabled = true
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        ResultsScreen(
            state = ResultsState(),
            onAction = {}
        )
    }
}