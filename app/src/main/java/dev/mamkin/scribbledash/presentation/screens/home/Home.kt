package dev.mamkin.scribbledash.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.navgraphs.OneRoundWonderNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.ui.components.AppTopBar
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientEnd
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientStart
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.Success
import dev.mamkin.scribbledash.ui.theme.SurfaceHigh

@Destination<RootGraph>(
    start = true
)
@Composable
fun HomeRoot(
    navigator: DestinationsNavigator
) {
    HomeScreen(
        onOneRoundWonderClick = {
            navigator.navigate(OneRoundWonderNavGraph)
        }
    )
}

@Composable
fun HomeScreen(
    onOneRoundWonderClick: () -> Unit,
) {
    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(
            BackgroundGradientStart,
            BackgroundGradientEnd
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) { innerPadding ->
        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ){
            AppTopBar(
                title = {
                    Text(
                        text = "ScribbleDash",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(83.dp))
                Text(
                    text = "Start drawing!",
                    style = MaterialTheme.typography.displayMedium,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select game mode",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnBackgroundVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                GameModeCards(
                    onOneRoundWonderClick = onOneRoundWonderClick
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameModeCards(
    modifier: Modifier = Modifier,
    onOneRoundWonderClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = SurfaceHigh
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            onClick = onOneRoundWonderClick,
            border = BorderStroke(8.dp, Success),
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "One Round\nWonder",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )

                Image(
                    painter = painterResource(id = R.drawable.one_round_wonder),
                    contentDescription = "One Round Wonder",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        HomeScreen(
            onOneRoundWonderClick = {}
        )
    }
}