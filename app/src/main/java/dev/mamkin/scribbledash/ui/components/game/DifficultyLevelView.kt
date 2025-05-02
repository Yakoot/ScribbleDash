package dev.mamkin.scribbledash.ui.components.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.domain.DifficultyLevel
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Composable
fun DifficultyLevelView(
    modifier: Modifier = Modifier,
    onLevelClick: (DifficultyLevel) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(126.dp))
        Text(
            text = "Start drawing!",
            style = MaterialTheme.typography.displayMedium,
            color = OnBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Choose a difficulty setting",
            style = MaterialTheme.typography.bodyMedium,
            color = OnBackgroundVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
        LevelSelector(
            onClick = onLevelClick
        )
    }
}

@Composable
fun LevelSelector(
    modifier: Modifier = Modifier,
    onClick: (DifficultyLevel) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
    ) {
        levels.forEachIndexed { index, it ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (index % 2 == 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                    onClick = { onClick(it.value) }
                ) {
                    Image(
                        painter = painterResource(it.image),
                        contentDescription = it.title
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = OnBackgroundVariant,
                )
            }

        }
    }
}



data class Level(
    val title: String,
    val image: Int,
    val value: DifficultyLevel
)

val levels = listOf<Level>(
    Level(
        title = "Beginner",
        image = R.drawable.img_beginner,
        value = DifficultyLevel.Beginner
    ),
    Level(
        title = "Challenging",
        image = R.drawable.img_challenging,
        value = DifficultyLevel.Challenging
    ),
    Level(
        title = "Master",
        image = R.drawable.img_master,
        value = DifficultyLevel.Master
    )
)

@Preview
@Composable
private fun LevelSelectorPreview() {
    ScribbleDashTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LevelSelector()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        DifficultyLevelView()
    }
}
