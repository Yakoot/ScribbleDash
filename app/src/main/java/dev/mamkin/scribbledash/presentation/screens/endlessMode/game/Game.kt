 package dev.mamkin.scribbledash.presentation.screens.endlessMode.game

 import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.components.PaletteStat
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

 @Composable
fun GameRoot(
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    GameScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameState,
    onClose: () -> Unit = {},
    onAction: (GameAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            CenterAlignedTopAppBar(
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
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    PaletteStat()
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        GameScreen(
            state = GameState(),
            onAction = {}
        )
    }
}