package dev.mamkin.scribbledash.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.headlineXSmall

@Composable
fun StarHighScore(modifier: Modifier = Modifier) {
    val gradientStartColor = Color(0xFFFFDA35)
    val gradientEndColor = Color(0xFFFF9600)
    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(
            gradientStartColor,
            gradientEndColor
        )
    )
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .background(backgroundGradient)
                .padding(
                    start = 24.dp,
                    end = 14.dp,
                    top = 7.dp,
                    bottom = 9.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "New High Score",
                color = Color.White,
                style = MaterialTheme.typography.headlineXSmall
            )
        }
        Image(
            painter = painterResource(id = R.drawable.star_outlined),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = -12.dp)
        )
    }

}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            StarHighScore(modifier = Modifier.padding(horizontal = 20.dp))
        }
    }
}