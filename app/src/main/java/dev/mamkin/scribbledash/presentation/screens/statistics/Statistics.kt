package dev.mamkin.scribbledash.presentation.screens.statistics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Destination<RootGraph>
@Composable
fun StatisticsRoot(
    viewModel: StatisticsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    StatisticsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun StatisticsScreen(
    state: StatisticsState,
    onAction: (StatisticsAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        StatisticsScreen(
            state = StatisticsState(),
            onAction = {}
        )
    }
}