package dev.mamkin.scribbledash.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import dev.mamkin.scribbledash.ui.theme.Pink
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.headlineXSmall

@Composable
fun DrawingsCount(
    modifier: Modifier = Modifier,
    count: Int = 0,
    newHighScore: Boolean = false
) {
    val image = if (newHighScore) {
        R.drawable.palette_outlined
    } else {
        R.drawable.palette
    }

    val backgroundColor = if (newHighScore) {
        Pink
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    val textColor = if (newHighScore) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
    }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .padding(start = 20.dp)
                .wrapContentWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor, CircleShape)
                    .wrapContentWidth()
                    .height(28.dp)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 20.dp),
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineXSmall,
                    color = textColor
                )
            }

            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = (-20).dp)
            )
        }

        if (newHighScore) {
            Text(
                text = "NEW HIGH!",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        Column {
            DrawingsCount()
            DrawingsCount(
                newHighScore = true
            )
        }
    }
}