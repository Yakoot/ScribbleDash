package dev.mamkin.scribbledash.presentation.screens.comingsoon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.navgraphs.OneRoundWonderNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.mamkin.scribbledash.presentation.screens.home.GameModeCard
import dev.mamkin.scribbledash.ui.components.AppTopBar
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientEnd
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientStart
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

@Destination<RootGraph>()
@Composable
fun ComingSoonRoot(
    navigator: DestinationsNavigator
) {
    ComingSoonScreen()
}

@Composable
fun ComingSoonScreen() {
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
                    text = "Coming soon!",
                    style = MaterialTheme.typography.displayMedium,
                    color = OnBackground
                )
            }
        }
    }

}



@Preview
@Composable
private fun Preview() {
    ScribbleDashTheme {
        ComingSoonScreen()
    }
}
