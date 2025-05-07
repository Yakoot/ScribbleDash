package dev.mamkin.scribbledash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.ui.theme.Error
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.headlineXSmall

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    text: String,
    isRed: Boolean = false
) {
    val textColor = if (isRed) Error else MaterialTheme.colorScheme.onBackground
    Surface(
        modifier = modifier
            .size(56.dp)
            .then(
                if (isRed) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = CircleShape
                    )
                } else Modifier
            )
            .padding(4.dp),
        shadowElevation = 8.dp,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineXSmall,
                color = textColor
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Timer(text = "02:00")
            Timer(text = "02:00", isRed = true)
        }
    }
}