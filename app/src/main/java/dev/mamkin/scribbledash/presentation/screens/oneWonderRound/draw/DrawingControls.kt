package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.draw

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.Success

@Composable
fun DrawingControls(
    modifier: Modifier = Modifier,
    onAction: (DrawAction) -> Unit,
    state: DrawState
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledIconButton(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(22.dp),
            onClick = { onAction(DrawAction.OnUndo) },
            colors = IconButtonDefaults.filledIconButtonColors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            enabled = state.isUndoEnabled
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_undo),
                contentDescription = "Undo"
            )
        }
        FilledIconButton(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(22.dp),
            colors = IconButtonDefaults.filledIconButtonColors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            onClick = { onAction(DrawAction.OnRedo) },
            enabled = state.isRedoEnabled
        ) {
            Icon(
                modifier = Modifier.graphicsLayer { rotationY = 180f },
                painter = painterResource(R.drawable.ic_undo),
                contentDescription = "Redo"
            )
        }
        ClearCanvasButton(
            onClick = { onAction(DrawAction.OnClearCanvasClick) },
            enabled = state.isClearEnabled

        )
    }
}

@Composable
fun ClearCanvasButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Success,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        enabled = enabled,
        onClick = onClick,
        border = BorderStroke(8.dp, MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "CLEAR CANVAS",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
private fun ClearCanvasButtonPreview() {
    ScribbleDashTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ClearCanvasButton(
                onClick = {},
                enabled = true
            )
            ClearCanvasButton(
                onClick = {},
                enabled = false
            )
        }
    }
}

@Preview
@Composable
private fun DrawControlsPreview() {
    ScribbleDashTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DrawingControls(
                onAction = {},
                state = DrawState()
            )
            DrawingControls(
                onAction = {},
                state = DrawState(
                    isRedoEnabled = true,
                    isUndoEnabled = true,
                    isClearEnabled = true
                )
            )
        }
    }
}