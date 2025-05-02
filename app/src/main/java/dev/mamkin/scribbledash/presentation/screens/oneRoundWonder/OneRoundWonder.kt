package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder

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
import dev.mamkin.scribbledash.ui.components.common.AppCloseIcon
import dev.mamkin.scribbledash.ui.components.common.AppTopBar
import dev.mamkin.scribbledash.ui.components.game.DifficultyLevelView
import dev.mamkin.scribbledash.ui.components.game.DrawView
import dev.mamkin.scribbledash.ui.components.game.PreviewView
import dev.mamkin.scribbledash.ui.components.game.ResultsView
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import org.koin.compose.viewmodel.koinViewModel

@Destination<RootGraph>
@Composable
fun OneRoundWonderRoot(
    viewModel: OneRoundWonderViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OneRoundWonderScreen(
        state = state,
        onAction = {
            when(it) {
                is OneRoundWonderAction.Close -> {
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
fun OneRoundWonderScreen(
    state: OneRoundWonderState,
    onAction: (OneRoundWonderAction) -> Unit,
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
                    AppCloseIcon {
                        onAction(OneRoundWonderAction.Close)
                    }
                }
            )
            when (state) {
                OneRoundWonderState.DifficultyLevel -> {
                    DifficultyLevelView(
                        onLevelClick = {
                            onAction(OneRoundWonderAction.LevelClick(it))
                        }
                    )
                }
                is OneRoundWonderState.Preview -> {
                    PreviewView(
                        image = state.image,
                        secondsLeft = state.secondsLeft,
                        onSizeChanged = {
                            onAction(OneRoundWonderAction.SizeChanged(it))
                        }
                    )
                }
                is OneRoundWonderState.Draw -> {
                    DrawView(
                        state = state.drawState,
                        onAction = {
                            onAction(OneRoundWonderAction.Draw(it))
                        }
                    )
                }
                is OneRoundWonderState.Results -> {
                    ResultsView(
                        percent = state.percent,
                        userImageData = state.userImageData,
                        exampleImageData = state.exampleImageData,
                        rating = state.rating,
                        onTryAgainClick = {
                            onAction(OneRoundWonderAction.TryAgain)
                        },
                        onImageSizeChanged = {
                            onAction(OneRoundWonderAction.ResultsImageSizeChanged(it))
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        OneRoundWonderScreen(
            state = OneRoundWonderState.DifficultyLevel,
            onAction = {}
        )
    }
}