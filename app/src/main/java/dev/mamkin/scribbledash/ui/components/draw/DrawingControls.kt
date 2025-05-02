package dev.mamkin.scribbledash.ui.components.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.presentation.models.DrawState
import dev.mamkin.scribbledash.ui.components.common.AppButton
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

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
                painter = painterResource(R.drawable.ic_redo),
                contentDescription = "Redo"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            onClick = { onAction(DrawAction.OnDoneClick) },
            enabled = state.isDoneEnabled
        )
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
                    isDoneEnabled = true
                )
            )
        }
    }
}
