package dev.mamkin.scribbledash.presentation.screens.speedDraw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeRootDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.ui.components.PaletteStat
import dev.mamkin.scribbledash.ui.components.Timer
import dev.mamkin.scribbledash.ui.components.common.AppCloseIcon
import dev.mamkin.scribbledash.ui.components.common.AppTopBar
import dev.mamkin.scribbledash.ui.components.game.DifficultyLevelView
import dev.mamkin.scribbledash.ui.components.game.DrawView
import dev.mamkin.scribbledash.ui.components.game.PreviewView
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun SpeedDrawRoot(
    viewModel: SpeedDrawViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpeedDrawScreen(
        state = state,
        onAction = {
            when (it) {
                is SpeedDrawAction.Close -> {
                    navigator.popBackStack(
                        route = HomeRootDestination,
                        inclusive = false
                    )
                }

                else -> viewModel.onAction(action = it)
            }
        }
    )
}

@Composable
fun SpeedDrawScreen(
    state: SpeedDrawState,
    onAction: (SpeedDrawAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            if (state is SpeedDrawState.DifficultyLevel || state is SpeedDrawState.Results) {
                AppTopBar(
                    actions = {
                        AppCloseIcon {
                            onAction(SpeedDrawAction.Close)
                        }
                    },
                )
            } else {
                AppTopBar(
                    actions = {
                        AppCloseIcon {
                            onAction(SpeedDrawAction.Close)
                        }
                    },
                    timer = {
                        Row {
                            Spacer(Modifier.width(10.dp))
                            Timer(
                                text = "2:00"
                            )
                        }
                    },
                    title = {
                        PaletteStat()
                    },
                    titleCentered = true
                )
            }

            when (state) {
                SpeedDrawState.DifficultyLevel -> {
                    DifficultyLevelView(
                        onLevelClick = {
                            onAction(SpeedDrawAction.LevelClick(it))
                        }
                    )
                }

                is SpeedDrawState.Preview -> {
                    PreviewView(
                        image = state.image,
                        secondsLeft = state.secondsLeft,
                        onSizeChanged = {
                            onAction(SpeedDrawAction.SizeChanged(it))
                        }
                    )
                }

                is SpeedDrawState.Draw -> {
                    DrawView(
                        state = state.drawState,
                        onAction = {
                            onAction(SpeedDrawAction.Draw(it))
                        }
                    )
                }

                is SpeedDrawState.Results -> {
                    SpeedDrawResults(
                        percent = state.percent,
                        rating = state.rating,
                        onDrawAgainClick = {
                            onAction(SpeedDrawAction.DrawAgain)
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDifficultyLevel() {
    ScribbleDashTheme {
        SpeedDrawScreen(
            state = SpeedDrawState.DifficultyLevel,
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewPreview() {
    ScribbleDashTheme {
        SpeedDrawScreen(
            state = SpeedDrawState.Preview(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDraw() {
    ScribbleDashTheme {
        SpeedDrawScreen(
            state = SpeedDrawState.Draw(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewResults() {
    ScribbleDashTheme {
        SpeedDrawScreen(
            state = SpeedDrawState.Results(),
            onAction = {}
        )
    }
}