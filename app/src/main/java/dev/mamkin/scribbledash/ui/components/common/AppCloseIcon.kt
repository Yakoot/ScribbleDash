package dev.mamkin.scribbledash.ui.components.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.mamkin.scribbledash.R

@Composable
fun AppCloseIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}