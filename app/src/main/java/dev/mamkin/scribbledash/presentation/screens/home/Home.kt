package dev.mamkin.scribbledash.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Destination<RootGraph>(
    start = true
)
@Composable
fun HomeRoot(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}