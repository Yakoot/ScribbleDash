package dev.mamkin.scribbledash.presentation.screens.endlessMode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeAction.ImageDrawn
import dev.mamkin.scribbledash.presentation.screens.endlessMode.EndlessModeAction.LevelClick
import dev.mamkin.scribbledash.ui.components.DrawingsCount
import dev.mamkin.scribbledash.ui.components.common.AppCloseIcon
import dev.mamkin.scribbledash.ui.components.common.AppTopBar
import dev.mamkin.scribbledash.ui.components.draw.DrawView
import dev.mamkin.scribbledash.ui.components.draw.measureWithoutPadding
import dev.mamkin.scribbledash.ui.components.game.DifficultyLevelView
import dev.mamkin.scribbledash.ui.components.game.PreviewView
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.compose.viewmodel.koinViewModel

@Destination<RootGraph>
@Composable
fun EndlessModeRoot(
    viewModel: EndlessModeViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawingsCount by viewModel.drawingsCount.collectAsStateWithLifecycle()

    EndlessModeScreen(
        modifier = Modifier.measureWithoutPadding(
            onSizeChanged = {
                viewModel.onAction(EndlessModeAction.SizeChanged(it))
            }
        ),
        state = state,
        onAction = {
            when (it) {
                is EndlessModeAction.Close -> {
                    navigator.popBackStack(
                        route = HomeRootDestination,
                        inclusive = false
                    )
                }

                else -> viewModel.onAction(action = it)
            }
        },
        drawingsCount = drawingsCount
    )
}

@Composable
fun EndlessModeScreen(
    modifier: Modifier = Modifier,
    state: EndlessModeState,
    onAction: (EndlessModeAction) -> Unit,
    drawingsCount: Int,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            if (state is EndlessModeState.DifficultyLevel || state is EndlessModeState.Results) {
                AppTopBar(
                    actions = {
                        AppCloseIcon {
                            onAction(EndlessModeAction.Close)
                        }
                    },
                )
            } else {
                AppTopBar(
                    actions = {
                        AppCloseIcon {
                            onAction(EndlessModeAction.Close)
                        }
                    },
                    title = {
                        DrawingsCount(
                            count = drawingsCount,
                        )
                    },
                    titleCentered = true
                )
            }

            when (state) {
                EndlessModeState.DifficultyLevel -> {
                    DifficultyLevelView(
                        onLevelClick = {
                            onAction(LevelClick(it))
                        }
                    )
                }

                is EndlessModeState.Preview -> {
                    PreviewView(
                        image = state.image,
                        secondsLeft = state.secondsLeft,
                    )
                }

                is EndlessModeState.Draw -> {
                    DrawView(
                        onDone = {
                            onAction(ImageDrawn(it))
                        }
                    )
                }

                is EndlessModeState.Results -> {
                    EndlessModeResults(
                        state = state,
                        onDrawAgainClick = {
                            onAction(EndlessModeAction.DrawAgain)
                        },
                    )
                }

                is EndlessModeState.RoundResults -> {
                    RoundResultsView(
                        state = state,
                        onFinish = {
                            onAction(EndlessModeAction.Finish)
                        },
                        onNext = {
                            onAction(EndlessModeAction.NextDrawing)
                        },
                        onImageSizeChanged = {
                            onAction(EndlessModeAction.ResultsImageSizeChanged(it))
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRoundResults() {
    ScribbleDashTheme {
        EndlessModeScreen(
            state = EndlessModeState.RoundResults(
                percent = "0",
                exampleImageData = emptyList(),
                userImageData = emptyList(),
                rating = Rating.OOPS,
                showCheckImage = false,
                showNextButton = false,
            ),
            onAction = {},
            drawingsCount = 3
        )
    }
}