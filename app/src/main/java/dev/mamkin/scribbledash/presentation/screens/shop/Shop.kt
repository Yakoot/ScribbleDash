package dev.mamkin.scribbledash.presentation.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dev.mamkin.scribbledash.ui.components.CoinsCount
import dev.mamkin.scribbledash.ui.components.common.AppTopBar
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.labelXLarge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun ShopRoot(
    viewModel: ShopViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    ObserveAsEvents<Unit>(
        flow = viewModel.noMoneyEvent,
    ) { event ->
        showDialog = true
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
            ) {
                Text(
                    text = "You don't have enough coins to buy this item.\nTry to play more games.",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
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
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
            ,
        ) {
            AppTopBar(
                modifier = Modifier.padding(end = 16.dp),
                title = {
                    Text(
                        text = "Shop",
                        style = MaterialTheme.typography.labelXLarge,
                        color = OnBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    CoinsCount(count = state.coins)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShopTab.entries.forEach { tab ->
                    Tab(
                        text = tab.title,
                        isActive = state.tab == tab,
                        onClick = {
                            onAction(ShopAction.SelectTab(tab))
                        }
                    )
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                val shopItems = when (state.tab) {
                    ShopTab.PEN -> state.penItems
                    ShopTab.CANVAS -> state.canvasItems
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        vertical = 14.dp,
                        horizontal = 12.dp
                    )
                ) {
                    items(shopItems) {
                        ShopCard(
                            state = it,
                            onClick = {
                                when (state.tab) {
                                    ShopTab.PEN -> onAction(ShopAction.PenClick(it.id))
                                    ShopTab.CANVAS -> onAction(ShopAction.CanvasClick(it.id))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.Tab(
    modifier: Modifier = Modifier,
    text: String,
    isActive: Boolean = false,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Box(
            modifier = modifier
                .weight(1f)
                .height(46.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow.copy(
                        alpha = if (isActive) 1f else 0.5f
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                )
                .clickable(onClick = onClick)
            ,
            contentAlignment = Alignment.BottomCenter
        ) {
            val textStyle = if (isActive) {
                MaterialTheme.typography.labelLarge
            } else {
                MaterialTheme.typography.bodyLarge
            }
            val textColor = if (isActive) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.onSurface
            }
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                style = textStyle,
                color = textColor
            )
            if (!isActive) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }

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

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}