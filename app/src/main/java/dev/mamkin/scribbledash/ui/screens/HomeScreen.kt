package dev.mamkin.scribbledash.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientEnd
import dev.mamkin.scribbledash.ui.theme.BackgroundGradientStart
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant
import dev.mamkin.scribbledash.ui.theme.Primary
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme
import dev.mamkin.scribbledash.ui.theme.Success
import dev.mamkin.scribbledash.ui.theme.SurfaceHigh
import dev.mamkin.scribbledash.ui.theme.SurfaceLow

@Composable
fun HomeScreen(
    onOneRoundWonderClick: () -> Unit = {}
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            BackgroundGradientStart,
            BackgroundGradientEnd
        )
    )

    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar()
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Start drawing!",
                        style = MaterialTheme.typography.displayMedium,
                        color = OnBackground
                    )
                    
                    Text(
                        text = "Select game mode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnBackgroundVariant
                    )
                }
                
                GameModeCards(
                    modifier = Modifier.padding(top = 24.dp),
                    onOneRoundWonderClick = onOneRoundWonderClick
                )
            }
        }
    }
}

@Composable
private fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ScribbleDash",
            style = MaterialTheme.typography.headlineMedium,
            color = OnBackground,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BottomNavBar() {
    Surface(
        color = SurfaceHigh,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp),
                tint = SurfaceLow
            )
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFDCE2F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "Play",
                    modifier = Modifier.size(24.dp),
                    tint = Primary
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
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = onOneRoundWonderClick,
            colors = CardDefaults.cardColors(
                containerColor = Success
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceHigh
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "One Round\nWonder",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnBackground,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    )
                    
                    Image(
                        painter = painterResource(id = R.drawable.img_one_round_wonder),
                        contentDescription = "One Round Wonder",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ScribbleDashTheme {
        HomeScreen()
    }
} 