package dev.mamkin.scribbledash.presentation.screens.speedDraw

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
fun SpeedDrawRoot(
    viewModel: SpeedDrawViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpeedDrawScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SpeedDrawScreen(
    state: SpeedDrawState,
    onAction: (SpeedDrawAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        SpeedDrawScreen(
            state = SpeedDrawState(),
            onAction = {}
        )
    }
}