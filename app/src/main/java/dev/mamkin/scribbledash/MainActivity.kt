package dev.mamkin.scribbledash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.navgraphs.OneRoundWonderNavGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.contains
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import dev.mamkin.scribbledash.presentation.navigation.BottomBar
import dev.mamkin.scribbledash.ui.theme.ScribbleDashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScribbleDashTheme {
                val navController = rememberNavController()

                val currentDestination: DestinationSpec = navController.currentDestinationAsState().value
                    ?: NavGraphs.root.startDestination

                val isBottomBarVisible = remember(currentDestination) {
                    !OneRoundWonderNavGraph.contains(currentDestination)
                }

                Scaffold(
                    bottomBar = {
                        if (isBottomBarVisible) {
                            BottomBar(navController)
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { contentPadding ->
                    DestinationsNavHost(
                        modifier = Modifier
                            .padding(contentPadding),
                        navController = navController,
                        navGraph = NavGraphs.root
                    )
                }
            }
        }
    }
}

