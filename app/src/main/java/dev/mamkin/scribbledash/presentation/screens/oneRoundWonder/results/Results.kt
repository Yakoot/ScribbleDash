package dev.mamkin.scribbledash.presentation.screens.oneRoundWonder.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.components.AppButton
import dev.mamkin.scribbledash.ui.components.AppTopBar
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.OnSurface
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Composable
fun ResultsRoot(
    viewModel: ResultsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResultsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ResultsScreen(
    state: ResultsState,
    onAction: (ResultsAction) -> Unit,
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
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 29.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(53.dp))
                Text(
                    text = "${state.percent}%",
                    style = MaterialTheme.typography.displayLarge,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .rotate(-10f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Example",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .background(Color.Transparent)
                                .shadow(8.dp, RoundedCornerShape(16.dp), clip = false)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        ) {

                        }
                    }


                    Column(
                        modifier = Modifier
                            .offset(y = 24.dp)
                            .rotate(10f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Drawing",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .background(Color.Transparent)
                                .shadow(8.dp, RoundedCornerShape(16.dp), clip = false)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        ) {

                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = state.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnBackgroundVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                AppButton(
                    text = "TRY AGAIN",
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {},
                    enabled = true
                )
                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        ResultsScreen(
            state = ResultsState(),
            onAction = {}
        )
    }
}