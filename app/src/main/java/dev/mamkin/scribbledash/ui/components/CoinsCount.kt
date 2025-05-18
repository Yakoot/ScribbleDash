package dev.mamkin.scribbledash.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.headlineXSmall

@Composable
fun CoinsCount(
    modifier: Modifier = Modifier,
    count: Int = 0,
) {
    Box(
        modifier = modifier
            .padding(start = 16.dp)
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerLow, CircleShape)
                .wrapContentWidth()
                .height(28.dp)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 22.dp, end = 12.dp),
                text = count.toString(),
                style = MaterialTheme.typography.headlineXSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Image(
            painter = painterResource(id = R.drawable.coin),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-16).dp)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        Column {
            CoinsCount(count = 300)
        }
    }
}