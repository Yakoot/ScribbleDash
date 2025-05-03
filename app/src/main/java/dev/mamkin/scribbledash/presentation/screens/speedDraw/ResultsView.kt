package dev.mamkin.scribbledash.presentation.screens.speedDraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.ui.components.PaletteStat
import dev.mamkin.scribbledash.ui.components.RatingText
import dev.mamkin.scribbledash.ui.components.StarHighScore
import dev.mamkin.scribbledash.ui.components.common.AppButton
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.labelXLarge

@Composable
fun SpeedDrawResults(
    modifier: Modifier = Modifier,
    state: SpeedDrawState.Results = SpeedDrawState.Results(),
    onDrawAgainClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Time’s up!",
            style = MaterialTheme.typography.labelXLarge,
            color = OnBackgroundVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    clip = false
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            if (state.newHighScore) {
                StarHighScore(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-10).dp)
                        .zIndex(1f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${state.averageScore}%",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                RatingText(rating = state.rating)
                Spacer(modifier = Modifier.height(16.dp))
                PaletteStat(
                    count = state.drawingsCompleted,
                    newHighScore = state.drawingsCountRecord
                )
                if (!state.drawingsCountRecord) Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            modifier = Modifier.padding(horizontal = 38.dp),
            text = "DRAW AGAIN",
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = onDrawAgainClick,
            enabled = true
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        SpeedDrawResults(
            state = SpeedDrawState.Results(
                drawingsCountRecord = true,
                newHighScore = true,
                averageScore = "20",
                rating = Rating.GREAT
            )
        )
    }
}