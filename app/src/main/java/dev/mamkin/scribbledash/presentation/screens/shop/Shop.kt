package dev.mamkin.scribbledash.presentation.screens.shop

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
fun ShopRoot(
    viewModel: ShopViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ShopScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ShopScreen(
    state: ShopState,
    onAction: (ShopAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        ShopScreen(
            state = ShopState(),
            onAction = {}
        )
    }
}