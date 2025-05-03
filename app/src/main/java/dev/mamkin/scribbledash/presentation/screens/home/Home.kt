package dev.mamkin.scribbledash.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.EndlessModeRootDestination
import com.ramcosta.composedestinations.generated.destinations.OneRoundWonderRootDestination
import com.ramcosta.composedestinations.generated.destinations.SpeedDrawRootDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.components.common.AppTopBar
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientEnd
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientStart
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.Primary
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.Success
import dev.mamkin.scribbledash.ui.theme.TertiaryContainer

@Destination<RootGraph>(
    start = true
)
@Composable
fun HomeRoot(
    navigator: DestinationsNavigator
) {
    HomeScreen(
        onGameModeClick = {
            when (it) {
                GameMode.OneRoundWonder -> navigator.navigate(OneRoundWonderRootDestination)
                GameMode.SpeedDraw -> navigator.navigate(SpeedDrawRootDestination)
                GameMode.EndlessMode -> navigator.navigate(EndlessModeRootDestination)
            }
        }
    )
}

@Composable
fun HomeScreen(
    onGameModeClick: (GameMode) -> Unit,
) {
    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(
            BackgroundGradientStart,
            BackgroundGradientEnd
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) { innerPadding ->
        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            AppTopBar(
                title = {
                    Text(
                        text = "ScribbleDash",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(83.dp))
                Text(
                    text = "Start drawing!",
                    style = MaterialTheme.typography.displayMedium,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select game mode",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnBackgroundVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                GameModeCards(
                    onClick = onGameModeClick,
                    gameModes = listOf(
                        GameMode.OneRoundWonder,
                        GameMode.SpeedDraw,
                        GameMode.EndlessMode
                    )
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameModeCards(
    modifier: Modifier = Modifier,
    gameModes: List<GameMode> = emptyList(),
    onClick: (GameMode) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        gameModes.forEach { gameMode ->
            GameModeCard(
                onClick = { onClick(gameMode) },
                text = gameMode.text,
                borderColor = gameMode.borderColor,
                image = gameMode.image
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        HomeScreen(
            onGameModeClick = {},
        )
    }
}

sealed class GameMode(
    val image: Int,
    val text: String,
    val borderColor: Color
) {
    object OneRoundWonder : GameMode(
        image = R.drawable.one_round_wonder,
        text = "One Round\nWonder",
        borderColor = Success
    )

    object SpeedDraw : GameMode(
        image = R.drawable.speed_draw,
        text = "Speed\nDraw",
        borderColor = Primary
    )

    object EndlessMode : GameMode(
        image = R.drawable.endless_mode,
        text = "Endless\nMode",
        borderColor = TertiaryContainer
    )
}
